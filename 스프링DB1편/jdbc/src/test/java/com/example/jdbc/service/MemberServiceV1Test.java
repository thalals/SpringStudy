package com.example.jdbc.service;

import static com.example.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.example.jdbc.connection.ConnectionConst;
import com.example.jdbc.connection.DBConnectionUtil;
import com.example.jdbc.domain.Member;
import com.example.jdbc.repository.MemberRepositoryV1;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * 기본 동작, 트랜잭션 중 예외 처리
 */
class MemberServiceV1Test {

    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "ex";

    private MemberRepositoryV1 memberRepositoryV1;
    private MemberServiceV1 memberServiceV1;

    @BeforeEach
    void before(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        memberRepositoryV1 = new MemberRepositoryV1(dataSource);
        memberServiceV1 = new MemberServiceV1(memberRepositoryV1);
    }

    //각각 테스트 종료 후
    @AfterEach
    void after() throws SQLException {
        memberRepositoryV1.delete(MEMBER_A);
        memberRepositoryV1.delete(MEMBER_B);
        memberRepositoryV1.delete(MEMBER_EX);
    }

    @Test
    @DisplayName("정상 이체 케이스")
    void accountTransfer() throws SQLException {
        //given
        Member member_A = new Member(MEMBER_A, 10000);
        Member member_B = new Member(MEMBER_B, 10000);
        memberRepositoryV1.save(member_A);
        memberRepositoryV1.save(member_B);

        //when
        int money = 2000;
        memberServiceV1.accountTransfer(member_A.getMemberId(), member_B.getMemberId(), money);

        //then
        Member findMemberA = memberRepositoryV1.findById(member_A.getMemberId());
        Member findMemberB = memberRepositoryV1.findById(member_B.getMemberId());
        assertThat(findMemberA.getMoney()).isEqualTo(10000 + money);
        assertThat(findMemberB.getMoney()).isEqualTo(10000 - money);
    }

    @Test
    @DisplayName("이체중 예외 발생")
    void accountTransferEx() throws SQLException {
        //given
        Member member_A = new Member(MEMBER_A, 10000);
        Member member_B = new Member(MEMBER_EX, 10000);
        memberRepositoryV1.save(member_A);
        memberRepositoryV1.save(member_B);

        //when
        int money = 2000;
        assertThatThrownBy(
            () -> memberServiceV1.accountTransfer(member_A.getMemberId(), member_B.getMemberId(),
                money)).isInstanceOf(IllegalStateException.class);

        //then
        Member findMemberA = memberRepositoryV1.findById(member_A.getMemberId());
        Member findMemberB = memberRepositoryV1.findById(member_B.getMemberId());
        assertThat(findMemberA.getMoney()).isEqualTo(10000 + money);
        assertThat(findMemberB.getMoney()).isEqualTo(10000 - money);
    }
}