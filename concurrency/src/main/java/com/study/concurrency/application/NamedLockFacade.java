package com.study.concurrency.application;

import com.study.concurrency.domain.Repository.TicketRepositoryForLock;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NamedLockFacade {

    private final TicketService ticketService;
    private final TicketRepositoryForLock ticketRepositoryForLock;

    @Transactional
    public void setNamedLock(Long ticketId) {

        try {
            ticketRepositoryForLock.getLock("ticket");
            ticketService.reservationToNamedLock(ticketId);
        } finally {
            ticketRepositoryForLock.releaseLock("ticket");
        }
    }
}
