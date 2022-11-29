package com.galaxy.foundation.spring.ext

import com.galaxy.foundation.jwt.UserType
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * Extended Spring User Details.
 */
class CustomUserDetails(val user:String)  : UserDetails {

    var roles:List<UserType> = listOf(UserType.ADMIN);
    constructor(user: String, roles: List<UserType>?) : this(user){
        if (roles != null) {
            this.roles = roles
        }
    }

    override fun getUsername(): String {
        return user
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
       val authorities=  roles.map{
            it -> SimpleGrantedAuthority("ROLE_"+it.name)
        };
        return  authorities.toMutableList();
        //return mutableListOf(roles.map { it => {SimpleGrantedAuthority(it)} })
    }

    override fun getPassword(): String {
        return "";
    }
}