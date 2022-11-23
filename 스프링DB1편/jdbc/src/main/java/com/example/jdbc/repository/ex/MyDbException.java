package com.example.jdbc.repository.ex;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MyDbException extends RuntimeException {
    public MyDbException(String message) {
        super(message);
    }

    public MyDbException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyDbException(Throwable cause) {
        super(cause);
    }

}
