package com.study.concurrency.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedissonLockFacade {

    private final TicketService ticketService;
    private final RedissonClient redissonClient;

    public void tryRedissonLock(Long ticketId) {

        //key 로 Lock 객체 가져옴
        String lockKey = "reservation-redisson-lock-" + ticketId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            //획득시도 시간, 락 점유 시간
            boolean lockable = lock.tryLock(5, 1, TimeUnit.SECONDS);
            if (!lockable) {
                log.error("Lock 획득 실패={}", lockKey);
                return;
            }
            log.info("Redisson Lock 획득");
            ticketService.reservationToRedisson(ticketId);

        } catch (InterruptedException e) {
            log.error("Redisson 락 점유 에러");
            throw new RuntimeException(e);
        } finally {
            log.info("락 해제");
            lock.unlock();
        }
    }

    @Transactional
    public void releaseRedissonLockForTransactional(Long ticketId) {
        ticketService.reservationToRedisson(ticketId);
    }
}
