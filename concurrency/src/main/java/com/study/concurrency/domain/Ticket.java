package com.study.concurrency.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String name;
    @Column
    private Integer quantity;
    @Column
    private Integer stock;
    @Column
    private LocalDateTime performanceDate;
    @Column
    @CreatedDate
    private LocalDateTime createdAt;

    private Ticket(String name, Integer quantity, LocalDateTime performanceDate) {
        this.name = name;
        this.quantity = quantity;
        this.stock = quantity;
        this.performanceDate = performanceDate;
    }

    public static Ticket create(String name, int quantity, LocalDateTime performanceDate) {
        return new Ticket(name, quantity, performanceDate);
    }

    public void decrease() {
        if (stock <= 0) {
            throw new RuntimeException("남은 수량 없음");
        }
        stock -= 1;
    }


    public int getNumber() {
        return quantity - stock + 1;
    }
}
