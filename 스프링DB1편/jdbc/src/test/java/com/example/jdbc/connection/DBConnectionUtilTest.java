package com.example.jdbc.connection;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class DBConnectionUtilTest {

    @Test
    void connection(){
        //h2가 제공하는(현재 존재하는 DB driver) 의 db 드라이버를 구현체로써 가져옴 (jdbc Connection)
        Connection connection = DBConnectionUtil.geConnection();
        assertThat(connection).isNotNull();
    }
}
