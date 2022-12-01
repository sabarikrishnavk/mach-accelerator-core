package com.pranetr.foundation.spring.ext

import com.pranetr.foundation.jwt.JwtUser
import com.pranetr.foundation.jwt.UserType
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

@Component
class CustomUserDetailsService: UserDetailsService {
    override fun loadUserByUsername(username: String): CustomUserDetails {
        return CustomUserDetails(username, mutableListOf(UserType.GUEST))
    }
    fun loadDetails(jwtUser: JwtUser): CustomUserDetails {
        return CustomUserDetails(jwtUser.email, jwtUser.roles)
    }
}

