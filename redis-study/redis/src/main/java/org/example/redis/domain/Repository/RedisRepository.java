package org.example.redis.domain.Repository;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final RedisTemplate<String, String> redisTemplate;
    // inject the template as ListOperations
    @Resource(name="redisTemplate")
    private ListOperations<String, String> listOps;

    public void forList() {
        listOps.leftPush("forList", "1");
    }

    public void forSet() {
        redisTemplate.opsForSet().add("forSet", "1");
    }

    public void forString() {
        redisTemplate.opsForValue().increment("forString", 1);
    }

}
