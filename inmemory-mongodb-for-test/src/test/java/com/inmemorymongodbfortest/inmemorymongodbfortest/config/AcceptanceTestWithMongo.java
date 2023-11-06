package com.inmemorymongodbfortest.inmemorymongodbfortest.config;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@EnableMongoTestServer
@EnableMongoRepositories("com.inmemorymongodbfortest.inmemorymongodbfortest.repository")
public abstract class AcceptanceTestWithMongo {

    @Autowired
    MongoTemplate mongoTemplate;

    @BeforeEach
    public void setup() {

        mongoTemplate.getDb().drop();
    }
}
