package com.example.jdbc.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.jdbc.domain.Member;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class MemberRepositoryV0Test {

    MemberRepositoryV0 repositoryV0 = new MemberRepositoryV0();

    @Test
    void crud() throws SQLException {

        //save
        Member memberV0 = new Member("memberV0", 100000);
        repositoryV0.save(memberV0);

        //findById
        Member findMember = repositoryV0.findById(memberV0.getMemberId());
        log.info("findMember = {}", findMember);

        //2개는 다른 인스턴스지만, isEqualTo 가  equals() 를 호출해 값만 비교 하기 때문에 true
        //@Data 롬복은, 모든 상태 값을 비교할수 있는 equals 와 고유한 hashCode 를 자동으로 만들어준다.
        assertThat(findMember).isEqualTo(memberV0);


        //update
        int updateMoney = 200000;
        repositoryV0.update(memberV0.getMemberId(), updateMoney);
        Member updateMember = repositoryV0.findById(memberV0.getMemberId());
        assertThat(updateMember.getMoney()).isEqualTo(updateMoney);


        //delete
        repositoryV0.delete(memberV0.getMemberId());
        assertThatThrownBy(() -> repositoryV0.findById(memberV0.getMemberId()))
            .isInstanceOf(NoSuchElementException.class);
    }
}