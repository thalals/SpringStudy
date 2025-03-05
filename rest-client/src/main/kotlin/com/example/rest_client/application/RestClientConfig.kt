package com.example.rest_client.application

import org.springframework.http.HttpStatusCode
import org.springframework.web.client.RestClient

class RestClientConfig {
    fun getRestClient(): RestClient = RestClient.create()

    fun getRestClient2(): RestClient =
        RestClient
            .builder()
            .baseUrl("/")
            .build()

    fun getApi() {
        val restClient = getRestClient()
        val result =
            restClient
                .get()
                .uri("https://jsonplaceholder.typicode.com/posts")
                .retrieve()
                .toEntity(String::class.java)

        println("Response status: " + result.statusCode)
        println("Response headers: " + result.headers)
        println("Contents: " + result.body)
    }

    fun getApiError() {
        val restClient = getRestClient()

        // 404
        val result =
            restClient
                .get()
                .uri("https://jsonplaceholder.typicode.com/donot")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError) { _, response ->
                    throw RuntimeException(response.statusText)
                }.toEntity(String::class.java)

        println("Response status: " + result.statusCode)
        println("Response headers: " + result.headers)
        println("Contents: " + result.body)
    }

    fun getApiExchange() {
        val restClient = getRestClient()

        // 404
        val result =
            restClient
                .get()
                .uri("https://jsonplaceholder.typicode.com/donot")
                .exchange { request, response ->
                    if (response.getStatusCode().is4xxClientError()) {
                        throw RuntimeException(response.statusText)
                    } else {
                        // return Custom Response Object
//                    val pet: Pet = convertResponse(response)
//                    pet
                    }
                }
    }
}
