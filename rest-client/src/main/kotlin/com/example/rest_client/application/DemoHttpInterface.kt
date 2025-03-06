package com.example.rest_client.application

import org.springframework.http.ResponseEntity
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange

@HttpExchange
interface DemoHttpInterface {
    @GetExchange("/posts")
    fun getPosts(): ResponseEntity<String>
}
