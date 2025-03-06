package com.example.rest_client.application

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Component
class DemoHttpInterfaceProxy {
    @Bean
    fun demoHttpInterface(): DemoHttpInterface {
        val restClient = RestClient.builder().baseUrl("https://jsonplaceholder.typicode.com").build()
        val adapter = RestClientAdapter.create(restClient)
        val factory = HttpServiceProxyFactory.builderFor(adapter).build()

        return factory.createClient(DemoHttpInterface::class.java)
    }
}
