package com.example.rest_client.application

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class HttpInterfaceTest(
    @Autowired
    var demoHttpInterface: DemoHttpInterface,
) {
    @Test
    fun testGet() {
        val posts = demoHttpInterface.getPosts()
        println(posts)
    }
}
