package com.study.concurrency.infra;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class TicketReservationProducer {

    private static final Logger log = LoggerFactory.getLogger(TicketReservationProducer.class);
    private final KafkaTemplate<String, Long> kafkaTemplate;

    public void reserve(Long ticketId, Long count) {
        log.info("Produce Reserving ticket id {}, count {}", ticketId, count);
        CompletableFuture<SendResult<String, Long>> future = kafkaTemplate.send("ticket-reservation", ticketId);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("[SUCCESS] Produce Reserving ticket id {}, count {}", ticketId, count);
            } else {
                log.error("[ERROR] Reserving ticket id {}, count {}", ticketId, count);
            }
        });
    }
}
