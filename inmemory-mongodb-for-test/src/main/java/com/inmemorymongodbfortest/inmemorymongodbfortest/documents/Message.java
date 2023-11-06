package com.inmemorymongodbfortest.inmemorymongodbfortest.documents;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
@Getter
@ToString
public class Message {

    @Id
    private String id;

    @Field
    private String sender;

    @Field
    private String contents;

    public Message(String sender, String contents) {
        this.id = null;
        this.sender = sender;
        this.contents = contents;
    }

    public static Message create(String sender, String contents) {
        return new Message(sender, contents);
    }
}
