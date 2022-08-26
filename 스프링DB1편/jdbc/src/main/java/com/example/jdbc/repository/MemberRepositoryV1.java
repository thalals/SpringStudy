package com.example.jdbc.repository;

import com.example.jdbc.connection.DBConnectionUtil;
import com.example.jdbc.domain.Member;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

/**
 * JDBC - DataSource 사용, JdbcUtils 사용
 */

@Slf4j
public class MemberRepositoryV1 {

    private final DataSource dataSource;

    public MemberRepositoryV1(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values (?, ?)";

        Connection connection = null;
        PreparedStatement preparedStatement = null; // 이걸로 쿼리를 날림?

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);

            //파라미터 바인딩
            preparedStatement.setString(1, member.getMemberId());
            preparedStatement.setInt(2, member.getMoney());
            preparedStatement.executeUpdate();
            return member;

        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            //db 연결을 끊어줘야함
            preparedStatement.close();
            //여기서 익셉션이 터지면 connection 이 안끊어짐 그래서 다 묵어야함

            connection.close();


            //요래 해야함
            close(connection, preparedStatement, null);
        }
    }

    public Member findById(String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, memberId);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Member member = new Member();
                member.setMemberId(resultSet.getString("member_id"));
                member.setMoney(resultSet.getInt("money"));
                return member;
            }else{
                throw new NoSuchElementException("member not found memberId : " + memberId);
            }
        } catch (SQLException e) {
            log.error("error : ", e);
            throw e;
        }finally {
            close(connection,preparedStatement,resultSet);
        }
    }

    public void update(String memberId, int money) throws SQLException {
        String sql = "update member set money=? where member_id=?";


        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, money);
            preparedStatement.setString(2, memberId);

            int resultSize = preparedStatement.executeUpdate();
            log.info("resultSize = {}", resultSize);

        } catch (SQLException e) {
            log.error("error : ", e);
            throw e;
        }finally {
            close(connection,preparedStatement,null);
        }
    }

    public void delete(String memberId) throws SQLException {
        String sql = "delete from member where member_id=?";


        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, memberId);

            int resultSize = preparedStatement.executeUpdate();
            log.info("resultSize = {}", resultSize);

        } catch (SQLException e) {
            log.error("error : ", e);
            throw e;
        }finally {
            close(connection,preparedStatement,null);
        }
    }
    private void close(Connection con, Statement stmt, ResultSet resultSet) {
        //JdbcUtils 에서 제공하는 close 메소드 사용
        JdbcUtils.closeResultSet(resultSet);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(con);
    }

    private  Connection getConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        log.info("get connection={}, clas={}", connection, connection.getClass());
        return connection;
    }
}
