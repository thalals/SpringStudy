package org.example.redis;


import org.example.redis.domain.Repository.RedisCrudRepository;
import org.example.redis.domain.TicketCount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisTest {

    @Autowired
    RedisCrudRepository redisCrudRepository;

    @Test
    void test() {
        TicketCount ticketCount = new TicketCount(
            "흠뻑쑈티켓",
            0
        );

        //create
        redisCrudRepository.save(ticketCount);

        //read
        String id = ticketCount.getId();
        System.out.println(id);
        System.out.println(redisCrudRepository.findByTicketName(ticketCount.getTicketName()));
        System.out.println(redisCrudRepository.findById(id));

    }
}
