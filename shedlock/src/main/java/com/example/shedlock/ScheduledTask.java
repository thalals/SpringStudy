package com.example.shedlock;

import static java.lang.Thread.sleep;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduledTask {

    @Scheduled(fixedRate = 1000)
    @SchedulerLock(name = "scheduledTaskName")
    public void taskOne() throws InterruptedException {
        log.info("{}, Task one start. ⚙️", LocalDateTime.now());
        sleep(2000);
        log.info("{}, Task one done. ⚙️⚙️", LocalDateTime.now());
    }

    @Scheduled(fixedRate = 1000)
    @SchedulerLock(name = "scheduledTaskName")
    public void taskTwo() throws InterruptedException {
        log.info("{}, Task two start️. ⭐️", LocalDateTime.now());
        sleep(2000);
        log.info("{}, Task two done. ⭐⭐️️", LocalDateTime.now());
    }
}
