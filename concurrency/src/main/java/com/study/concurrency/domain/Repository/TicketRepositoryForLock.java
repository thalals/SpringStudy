package com.study.concurrency.domain.Repository;

import com.study.concurrency.domain.Ticket;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TicketRepositoryForLock extends JpaRepository<Ticket, Long> {

    @Lock(value = LockModeType.PESSIMISTIC_READ)
    @Query("select t from Ticket t where t.id = :id")
    Optional<Ticket> findByWithPessimisticLock(Long id);

    @Lock(value = LockModeType.OPTIMISTIC)
    @Query("select t from Ticket t where t.id = :id")
    Optional<Ticket> findByWithOptimisticLock(Long id);

    @Query(value = "select get_lock(:key, 3000)", nativeQuery = true)
    String getLock(String key);

    @Query(value = "select release_lock(:key)", nativeQuery = true)
    String releaseLock(String key);

}
