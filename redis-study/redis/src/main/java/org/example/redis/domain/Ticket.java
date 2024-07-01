package org.example.redis.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Entity
@Slf4j
@Getter
@ToString
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

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }
}
