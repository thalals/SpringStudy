package com.inmemorymongodbfortest.inmemorymongodbfortest.service;

import com.inmemorymongodbfortest.inmemorymongodbfortest.config.AcceptanceTestWithMongo;
import com.inmemorymongodbfortest.inmemorymongodbfortest.documents.Message;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CRUDServiceTest extends AcceptanceTestWithMongo {

    @Autowired
    MessageService messageService;

    @Test
    public void run1() {

        messageService.create("홍길동", "니가가라 하와이");
        messageService.create("임창정", "내가 임마!");
        messageService.create("전창조", "1AM");

        List<Message> list = messageService.findAll();

        for (Message message : list) {
            System.out.println(message.toString());
        }

        Assertions.assertThat(list).hasSize(3);
    }

    @Test
    public void run2() {

        messageService.create("아델", "이츠미~");

        List<Message> list = messageService.findAll();

        for (Message message : list) {
            System.out.println(message.toString());
        }

        Assertions.assertThat(list).hasSize(1);

    }
}