package com.example.rest_client.application

import kotlin.test.Test

class RestClientConfigTest {
    private val restClientConfig = RestClientConfig()

    @Test
    fun `RestClient api call`() {
        restClientConfig.getApiError()
    }
}
