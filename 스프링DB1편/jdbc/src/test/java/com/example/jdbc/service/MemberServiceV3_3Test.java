package com.example.jdbc.service;

import static com.example.jdbc.connection.ConnectionConst.PASSWORD;
import static com.example.jdbc.connection.ConnectionConst.URL;
import static com.example.jdbc.connection.ConnectionConst.USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.jdbc.domain.Member;
import com.example.jdbc.repository.MemberRepositoryV3;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

/**
 * 트랜잭션 - @Transaactional AOP
 */


//스프링 컨테이너를 사용해야 @Transaction 을 사용할 수 있다. (SpringBootTest - spring 을 하나 띄어서 빈 등록 및 의존관걔 주입)
@Slf4j
@SpringBootTest
class MemberServiceV3_3Test {

    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "ex";

    //스프링 컨테이너로 사용해서, 의존관걔를 주입 받아야함 @AutoWired
    @Autowired
    private MemberRepositoryV3 memberRepository;
    @Autowired
    private MemberServiceV3_3 memberService;


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
        MemberRepositoryV3 memberRepositoryV3(){
            return new MemberRepositoryV3(dataSource());
        }
        @Bean
        MemberServiceV3_3 memberServiceV3_3(){
            return new MemberServiceV3_3(memberRepositoryV3());
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