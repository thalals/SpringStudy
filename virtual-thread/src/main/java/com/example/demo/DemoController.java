package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DemoController {

    @GetMapping("/")
    public ResponseEntity<Void> threadRun() throws InterruptedException {

        log.info("{}, threadRun - Start", Thread.currentThread().getName());

        Thread.sleep(1000);

        log.info("{}, threadRun - Finished", Thread.currentThread().getName());

        return ResponseEntity.ok().build();
    }
}
