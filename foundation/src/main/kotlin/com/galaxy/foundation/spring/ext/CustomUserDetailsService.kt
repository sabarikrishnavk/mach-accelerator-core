package com.galaxy.foundation.spring.ext

import com.galaxy.foundation.jwt.JwtUser
import com.galaxy.foundation.jwt.UserType
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

