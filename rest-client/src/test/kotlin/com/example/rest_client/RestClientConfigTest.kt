package com.example.rest_client

import org.springframework.web.client.RestClient
import kotlin.test.Test

class RestClientConfigTest{

    private val restClientConfig = RestClientConfig()

    @Test
    fun `RestClient api call`() {
        restClientConfig.getApiError()
    }
}