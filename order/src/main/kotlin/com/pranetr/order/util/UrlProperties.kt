package com.pranetr.order.util

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding


@ConstructorBinding
@ConfigurationProperties("pranetr.url")
data class UrlProperties(var catalog: String) ;