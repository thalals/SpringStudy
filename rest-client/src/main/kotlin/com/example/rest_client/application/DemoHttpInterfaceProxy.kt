package com.example.rest_client.application

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Component
class DemoHttpInterfaceProxy {
    @Bean
    fun restClient(): RestClient =
        RestClient
            .builder()
            .baseUrl("https://jsonplaceholder.typicode.com")
            .defaultHeaders { headers ->
                headers.contentType = MediaType.APPLICATION_JSON
                headers.accept = listOf(MediaType.APPLICATION_JSON)
                headers.setBasicAuth("jwt-token")
            }.build()

    @Bean
    fun demoHttpInterface(
        @Qualifier("restClient") restClient: RestClient,
    ): DemoHttpInterface {
        val adapter = RestClientAdapter.create(restClient)
        val factory = HttpServiceProxyFactory.builderFor(adapter).build()

        return factory.createClient(DemoHttpInterface::class.java)
    }
}
