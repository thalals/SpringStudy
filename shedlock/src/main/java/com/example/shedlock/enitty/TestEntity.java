package com.example.shedlock.enitty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class TestEntity {

    @Id
    private Long id;

    @Column
    private String name;

    @Column
    private LocalDateTime createdAt;

    public TestEntity(String name, LocalDateTime createdAt) {
        this.id = null;
        this.name = name;
        this.createdAt = createdAt;
    }

    public static TestEntity of(String name, LocalDateTime createdAt) {
        return new TestEntity(name, createdAt);
    }
}
