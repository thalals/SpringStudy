package com.example.jdbc.service;

import static com.example.jdbc.connection.ConnectionConst.PASSWORD;
import static com.example.jdbc.connection.ConnectionConst.URL;
import static com.example.jdbc.connection.ConnectionConst.USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.jdbc.domain.Member;
import com.example.jdbc.repository.MemberRepositoryV3;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 트랜잭션 - 트랜잭션 매니저 테스트
 */
class MemberServiceV3_2Test {

    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "ex";

    private MemberRepositoryV3 memberRepository;
    private MemberServiceV3_2 memberService;

    @BeforeEach
    void before() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL,
            USERNAME, PASSWORD);
        memberRepository = new MemberRepositoryV3(dataSource);

        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        memberService = new MemberServiceV3_2(transactionManager, memberRepository);
    }

    //각각 테스트 종료 후
    @AfterEach
    void after() throws SQLException {
        memberRepository.delete(MEMBER_A);
        memberRepository.delete(MEMBER_B);
        memberRepository.delete(MEMBER_EX);
    }

    @Test
    @DisplayName("정상 이체 케이스")
    void accountTransfer() throws SQLException {
        //given
        Member member_A = new Member(MEMBER_A, 10000);
        Member member_B = new Member(MEMBER_B, 10000);
        memberRepository.save(member_A);
        memberRepository.save(member_B);

        //when
        int money = 2000;
        memberService.accountTransfer(member_A.getMemberId(), member_B.getMemberId(), money);

        //then
        Member findMemberA = memberRepository.findById(member_A.getMemberId());
        Member findMemberB = memberRepository.findById(member_B.getMemberId());
        assertThat(findMemberA.getMoney()).isEqualTo(10000 - money);
        assertThat(findMemberB.getMoney()).isEqualTo(10000 + money);
    }

    @Test
    @DisplayName("이체중 예외 발생 시 롤백")
    void accountTransferEx() throws SQLException {
        //given
        Member member_A = new Member(MEMBER_A, 10000);
        Member member_B = new Member(MEMBER_EX, 10000);
        memberRepository.save(member_A);
        memberRepository.save(member_B);

        //when
        int money = 2000;
        assertThatThrownBy(
            () -> memberService.accountTransfer(member_A.getMemberId(), member_B.getMemberId(),
                money)).isInstanceOf(IllegalStateException.class);

        //then
        Member findMemberA = memberRepository.findById(member_A.getMemberId());
        Member findMemberB = memberRepository.findById(member_B.getMemberId());
        assertThat(findMemberA.getMoney()).isEqualTo(10000 );
        assertThat(findMemberB.getMoney()).isEqualTo(10000 );
    }
}