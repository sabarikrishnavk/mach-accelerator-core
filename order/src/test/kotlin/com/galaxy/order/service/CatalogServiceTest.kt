package com.galaxy.promotion.service

import com.galaxy.foundation.context.CustomContext
import com.galaxy.foundation.logger.EventLogger
import com.galaxy.order.services.CatalogService
import com.galaxy.order.util.UrlProperties
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.TestPropertySource
import org.springframework.web.client.RestTemplate

@SpringBootTest(classes = [CatalogService::class, EventLogger::class,RestTemplate::class])
@EnableConfigurationProperties(UrlProperties::class)
@TestPropertySource("classpath:application.yaml")
class CatalogServiceTest {
    @Autowired
    lateinit var  eventLogger: EventLogger
    @Autowired
    lateinit var catalogService: CatalogService
    @Autowired
    lateinit var restTemplate: RestTemplate


    @Test
    fun evalulateSkuRequest() {
        val skuids = listOf<String>("SKU1","SKU2")
        val context = CustomContext()
        context.bearerToken ="Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJHYWxheHkiLCJzdWIiOiJ0ZXN0dXNlciIsIm5hbWUiOiJ0ZXN0dXNlciIsInVzZXJJZCI6ImM0NmM3OTA0LTJmZjMtNDYyOC1iZmRlLWI1YjlmMjExNTc2YSIsInJvbGUiOiJSRUdJU1RFUkVEIiwiZW1haWwiOiJ0ZXN0QHRlc3QuY29tIiwibG9jYXRpb24iOiJTVFIxIiwiaWF0IjoxNjIyNDgwOTA5LCJleHAiOjE2MjMwODU3MDl9.p183U8byDw8JX-mCbvqdJO66TXJWet4kf0WGkTu2PsbzfC5mH7VeTGB3YkLWzw-Yx8sxla7JguX29FmfhutKKQ"
        val skuMap= catalogService.getSkuDetails(skuids,context)
        for (sku in skuMap!!){
            println(sku)
        }

    }
}