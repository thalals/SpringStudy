package com.inmemorymongodbfortest.inmemorymongodbfortest.service;

import com.inmemorymongodbfortest.inmemorymongodbfortest.documents.Message;
import com.inmemorymongodbfortest.inmemorymongodbfortest.repository.MessageRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    //create
    public Message create(String sender, String contents) {

        return messageRepository.save(Message.create(sender, contents));
    }

    //read
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

}
