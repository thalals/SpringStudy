package com.study.concurrency.domain.Repository;

import com.study.concurrency.domain.TicketCount;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RedisCrudRepository extends CrudRepository<TicketCount, String> {

    Optional<TicketCount> findByTicketName(String ticketName);
}
