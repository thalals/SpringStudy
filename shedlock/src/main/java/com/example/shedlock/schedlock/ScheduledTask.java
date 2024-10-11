package com.example.shedlock.schedlock;

import static java.lang.Thread.sleep;

import com.example.shedlock.enitty.TestEntityService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScheduledTask {

    private final TestEntityService testEntityService;

//    @Scheduled(fixedRate = 1000)
//    @SchedulerLock(name = "scheduledTaskName")
//    public void taskOne() throws InterruptedException {
//        log.info("{}, Task one start. ⚙️", LocalDateTime.now());
//
//        testEntityService.createEntity("taskOne");
//
//        log.info("{}, Task one done. ⚙️⚙️", LocalDateTime.now());
//    }

    @Scheduled(fixedRate = 1000)
    @SchedulerLock(name = "scheduledTaskName")
    public void taskTwo() throws InterruptedException {
        log.info("{}, Task two start️. ⭐️", LocalDateTime.now());

        testEntityService.createEntity(LocalDateTime.now() + " : Entity 생성");

        log.info("{}, Task two done. ⭐⭐️️", LocalDateTime.now());
    }
}
