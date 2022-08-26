package com.example.jdbc.repository;

import static com.example.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.jdbc.connection.ConnectionConst;
import com.example.jdbc.domain.Member;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Slf4j
class MemberRepositoryV1Test {

    MemberRepositoryV1 repositoryV1;

    @BeforeEach
    void beforeEach(){
        //기본 DriverManager - 항상 새로운 커넥션 회득
//        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        //이러면 속도가 너무 느리니, 커넥션 풀을 이용

        //커넥션 풀링
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);

        repositoryV1 = new MemberRepositoryV1(dataSource);
    }

    @Test
    void crud() throws SQLException, InterruptedException {

        //save
        Member memberV0 = new Member("memberV0", 100000);
        repositoryV1.save(memberV0);

        //findById
        Member findMember = repositoryV1.findById(memberV0.getMemberId());
        log.info("findMember = {}", findMember);

        //2개는 다른 인스턴스지만, isEqualTo 가  equals() 를 호출해 값만 비교 하기 때문에 true
        //@Data 롬복은, 모든 상태 값을 비교할수 있는 equals 와 고유한 hashCode 를 자동으로 만들어준다.
        assertThat(findMember).isEqualTo(memberV0);


        //update
        int updateMoney = 200000;
        repositoryV1.update(memberV0.getMemberId(), updateMoney);
        Member updateMember = repositoryV1.findById(memberV0.getMemberId());
        assertThat(updateMember.getMoney()).isEqualTo(updateMoney);


        //delete
        repositoryV1.delete(memberV0.getMemberId());
        assertThatThrownBy(() -> repositoryV1.findById(memberV0.getMemberId()))
            .isInstanceOf(NoSuchElementException.class);

        //커넥션 풀 채우는 거 볼려고
        Thread.sleep(1000);
    }
}