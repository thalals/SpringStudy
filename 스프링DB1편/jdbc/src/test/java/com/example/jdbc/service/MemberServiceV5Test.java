package com.example.jdbc.service;

import com.example.jdbc.domain.Member;
import com.example.jdbc.repository.MemberRepository;
import com.example.jdbc.repository.MemberRepositoryV4_1;
import com.example.jdbc.repository.MemberRepositoryV5;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

import static com.example.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * jdbc template
 */


@Slf4j
@SpringBootTest
class MemberServiceV5Test {

    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "ex";

    //스프링 컨테이너로 사용해서, 의존관걔를 주입 받아야함 @AutoWired
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberServiceV4 memberService;


    //di 해줄 빈을 등록해 주어야함
    @TestConfiguration
    static class TestConfig {
        @Bean
        DataSource dataSource(){
            return  new DriverManagerDataSource(URL,
                USERNAME, PASSWORD);
        }
        @Bean
        PlatformTransactionManager transactionManager(){
            return new DataSourceTransactionManager(dataSource());
        }
        @Bean
        MemberRepository memberRepository(){
            return new MemberRepositoryV5(dataSource());
        }
        @Bean
        MemberServiceV4 memberServiceV4(){
            return new MemberServiceV4(memberRepository());
        }
    }

    //각각 테스트 종료 후
    @AfterEach
    void after() throws SQLException {
        memberRepository.delete(MEMBER_A);
        memberRepository.delete(MEMBER_B);
        memberRepository.delete(MEMBER_EX);
    }

    @Test
    void AopCheck() {
        log.info("memberService class={}", memberService.getClass());
        log.info("memberRepository class={}", memberRepository.getClass());

        //aop - Transactional 이 걸리는지 확인
        assertThat(AopUtils.isAopProxy(memberService)).isTrue();
        assertThat(AopUtils.isAopProxy(memberRepository)).isFalse();
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