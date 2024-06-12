package com.study.concurrency.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Entity
@Slf4j
@Getter
@NoArgsConstructor
public class Ticket {

    @Id
    private Long id;
    @Column
    private Integer quantity;
    @Column
    private Integer stock;

    public Ticket(Long id, Integer quantity) {
        this.id = id;
        this.quantity = quantity;
        this.stock = quantity;
    }

    public static Ticket create(Long id, Integer quantity) {
        return new Ticket(id, quantity);
    }

    public void decrease() {
        if (stock <= 0) {
            throw new RuntimeException("남은 수량 없음");
        }
        this.stock = this.stock - 1;
        log.info("남은 재고 : " + this.stock);
    }


    public int getNumber() {
        return quantity - stock + 1;
    }
}
