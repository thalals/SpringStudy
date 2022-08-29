package com.example.jdbc.service;

import com.example.jdbc.domain.Member;
import com.example.jdbc.repository.MemberRepositoryV1;
import com.example.jdbc.repository.MemberRepositoryV2;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 트랜잭션 적용 -  파라미터 연동, 풀을 고려한 종료
 */
@RequiredArgsConstructor
@Slf4j
public class MemberServiceV2 {

    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepositoryV2;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Connection connection = dataSource.getConnection();

        try {
            connection.setAutoCommit(false); //트랜잭션 시작 //비즈니스 로직
            bizLogic(connection, fromId, toId, money);
            connection.commit(); //성공시 커밋
        } catch (Exception e) {
            connection.rollback(); //실패시 롤백
            throw new IllegalStateException(e);
        } finally {
            release(connection);
        }

    }

    private void bizLogic(Connection con, String fromId, String toId, int money)
        throws SQLException {
        Member fromMember = memberRepositoryV2.findById(con, fromId);
        Member toMember = memberRepositoryV2.findById(con, toId);
        memberRepositoryV2.update(con, fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepositoryV2.update(con, toId, toMember.getMoney() + money);
    }

    private void release(Connection con) {
        if (con != null) {
            try {
                con.setAutoCommit(true); //커넥션 풀 고려
                con.close();
            } catch (Exception e) {
                log.info("error", e);
            }
        } }

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("error");
        }
    }
}
