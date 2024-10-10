package com.example.shedlock;

import static java.lang.Thread.sleep;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduledTask {

    @Scheduled(fixedRate = 1000)
    public void taskOne() throws InterruptedException {
        log.info("Task one start.");
        sleep(10000);
        log.info("Task one done.");
    }

    @Scheduled(fixedRate = 1000)
    public void taskTwo() throws InterruptedException {
        log.info("Task two ⭐️.");
        sleep(2000);
        log.info("Task two ⭐⭐.");
    }
}
