package com.galaxy.order.config

import com.galaxy.foundation.security.WebSecurityConfig
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfigExt : WebSecurityConfig() {

//    //override this method to implement security at the service level
//    @Throws(Exception::class)
//    override fun configure(httpSecurity: HttpSecurity) {
//    }
}