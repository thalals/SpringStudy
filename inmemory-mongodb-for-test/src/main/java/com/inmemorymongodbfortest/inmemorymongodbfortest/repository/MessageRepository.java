package com.inmemorymongodbfortest.inmemorymongodbfortest.repository;

import com.inmemorymongodbfortest.inmemorymongodbfortest.documents.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {

}
