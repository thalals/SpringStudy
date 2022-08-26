package com.example.jdbc.connection;

//상수 데이터만 담은 class - 객체 생성하지 않기위해 추상클래스로
public abstract class ConnectionConst {
    public static final String URL = "jdbc:h2:tcp://localhost/~/test";
    public static final String USERNAME = "sa";
    public static final String PASSWORD = "";
}
