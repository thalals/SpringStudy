package com.example.jdbc.connection;

import static com.example.jdbc.connection.ConnectionConst.*;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Slf4j
public class ConnectionTest {

    @Test
    void driverManager() throws SQLException {
        Connection connection1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Connection connection2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        log.info("connection={}, class={}", connection1, connection1.getClass());
        log.info("connection={}, class={}", connection2, connection2.getClass());
    }

    @Test
    void dataSourceDriverManager() throws SQLException {
        //DriverManagerSource - 항상 새로운 커넥션을 획득
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        useDataSource(dataSource);
    }

    @Test
    void dataSourceConnectionPool() throws SQLException, InterruptedException {
        //hikari pool 을 사용해서 커넥션 pooling
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);

        dataSource.setMaximumPoolSize(10);
        dataSource.setPoolName("MyPool");

        useDataSource(dataSource);

        //커넥션에서 커넥션 풀이 생성되는 걸 보기위해
        Thread.sleep(1000);

    }

    private void useDataSource(DataSource dataSource) throws SQLException {
        Connection connection1 = dataSource.getConnection();
        Connection connection2 = dataSource.getConnection();
        log.info("connection={}, class={}", connection1, connection1.getClass());
        log.info("connection={}, class={}", connection2, connection2.getClass());
    }
}
