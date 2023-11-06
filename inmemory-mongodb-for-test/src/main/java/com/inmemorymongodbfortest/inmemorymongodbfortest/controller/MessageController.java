package com.inmemorymongodbfortest.inmemorymongodbfortest.controller;

import com.inmemorymongodbfortest.inmemorymongodbfortest.documents.Message;
import com.inmemorymongodbfortest.inmemorymongodbfortest.service.MessageService;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService service;

    @GetMapping("/findAll")
    public ResponseEntity<List<Message>> get() {

        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping("/post/{sender}/{message}")
    public ResponseEntity<Objects> save(
        @PathVariable String sender,
        @PathVariable String message) {

        service.create(sender, message);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
