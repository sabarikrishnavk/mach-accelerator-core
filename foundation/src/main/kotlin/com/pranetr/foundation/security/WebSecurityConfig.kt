package com.pranetr.foundation.security
//
//import com.pranetr.foundation.jwt.config.JwtAuthenticationEntryPoint
//import com.pranetr.foundation.jwt.config.JwtRequestFilter
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.context.annotation.Bean
//import org.springframework.security.authentication.AuthenticationManager
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.config.http.SessionCreationPolicy
//import org.springframework.security.core.userdetails.UserDetailsService
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
//import org.springframework.security.crypto.password.PasswordEncoder
//import org.springframework.security.web.SecurityFilterChain
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
//
//
//open class WebSecurityConfig {
//    @Autowired
//    val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint? = null
//
//    @Autowired
//    val jwtUserDetailsService: UserDetailsService? = null
//
//    @Autowired
//    val jwtRequestFilter: JwtRequestFilter? = null
//    @Autowired
//    @Throws(Exception::class)
//    fun configureGlobal(auth: AuthenticationManagerBuilder) {
//        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder())
//    }
//
//    @Bean
//    open fun passwordEncoder(): PasswordEncoder {
//        return BCryptPasswordEncoder()
//    }
//    @Bean
//    @Throws(java.lang.Exception::class)
//    open fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager? {
//        return authenticationConfiguration.authenticationManager
//    }
//    @Bean
//    @Throws(java.lang.Exception::class)
//    open fun filterChain(httpSecurity: HttpSecurity): SecurityFilterChain? {
//            httpSecurity.csrf().disable()// dont authenticate this particular request
//                .authorizeHttpRequests()
//                .requestMatchers("/graphiql").permitAll()
//                .requestMatchers("/actuator").permitAll()
//                .requestMatchers("/actuator/*").permitAll()
//                .requestMatchers("/graphql").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .exceptionHandling()
//                .authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//
//// Add a filter to validate the tokens with every request
//            httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java)
//        return httpSecurity.build()
//    }
//}


import com.pranetr.foundation.jwt.config.JwtAuthenticationEntryPoint
import com.pranetr.foundation.jwt.config.JwtRequestFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

open class WebSecurityConfig {
    @Autowired
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint? = null

    @Autowired
    var userDetailsService: UserDetailsService? = null

    @Autowired
    private val jwtRequestFilter: JwtRequestFilter? = null
    @Bean
    @Throws(Exception::class)
    open fun filterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        // We don't need CSRF for this example
        httpSecurity.csrf().disable() // don't authenticate this particular request
            .authorizeHttpRequests()
            .antMatchers("/graphiql").permitAll()
            .antMatchers("/actuator").permitAll()
            .antMatchers("/actuator/*").permitAll()
            .antMatchers("/graphql").permitAll()
            .anyRequest().authenticated().and() // make sure we use stateless session; session won't be used to
            // store user's state.
            .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        // Add a filter to validate the tokens with every request
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java)
        return httpSecurity.build()
    }

    @Bean
    @Throws(Exception::class)
    open fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    open fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
