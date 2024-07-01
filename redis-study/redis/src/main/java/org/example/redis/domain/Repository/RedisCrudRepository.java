package org.example.redis.domain.Repository;


import org.example.redis.domain.TicketCount;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RedisCrudRepository extends CrudRepository<TicketCount, String> {

    Optional<TicketCount> findByTicketName(String ticketName);
}
