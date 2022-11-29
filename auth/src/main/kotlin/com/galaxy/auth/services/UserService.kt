
package com.galaxy.auth.services

import com.galaxy.auth.codegen.types.AuthPayload
import com.galaxy.foundation.JwtUtils
import com.galaxy.foundation.jwt.JwtProperties
import com.galaxy.foundation.jwt.JwtUser
import com.galaxy.foundation.jwt.UserType
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Service
import java.util.*


@Service
class UserService (private val jwtProperties: JwtProperties) {

    fun signup(email: String, password: String, username: String,location: String ): AuthPayload? {

        val jwtUser= JwtUser( UUID.randomUUID().toString(),username,email,location, listOf(UserType.REGISTERED));
        val token = JwtUtils(jwtProperties).generateAccessToken(jwtUser);
        return AuthPayload(token,true)
    }
     fun signin(email: String, password: String, location: String ): AuthPayload? {

         val username = "dbuser" //Authenticate and get username and other details.
         val jwtUser= JwtUser( UUID.randomUUID().toString(),username,email, location, listOf(UserType.REGISTERED));
         val token = JwtUtils(jwtProperties).generateAccessToken(jwtUser);
         return AuthPayload(token,true)
    }
}