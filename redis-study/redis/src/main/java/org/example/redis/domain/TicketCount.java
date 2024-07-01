package org.example.redis.domain;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(value = "ticket_count", timeToLive = 30)
@Getter
@ToString
public class TicketCount {

    @Id
    @Indexed
    private String id;
    @Indexed
    private String ticketName;
    private int count;

    public TicketCount(String ticketName, int count) {
        this.ticketName = ticketName;
        this.count = count;
    }
}
