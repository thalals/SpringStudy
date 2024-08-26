package com.study.concurrency.domain.Repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TicketCountRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public Long increment(String key) {
        return redisTemplate
            .opsForValue()
            .increment(key, 1L);
    }

    public void clear() {
        redisTemplate.
            getConnectionFactory()
            .getConnection()
            .serverCommands()
            .flushAll();
    }
}
