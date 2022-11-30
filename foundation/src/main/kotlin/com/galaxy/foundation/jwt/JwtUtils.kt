package com.galaxy.foundation.jwt

import com.galaxy.foundation.constants.*
import com.galaxy.foundation.jwt.JwtProperties
import com.galaxy.foundation.jwt.JwtUser
import com.galaxy.foundation.jwt.UserType
import com.galaxy.foundation.spring.ext.CustomUserDetails
import io.jsonwebtoken.*
import io.jsonwebtoken.security.SignatureException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*


@Component
class JwtUtils (private val jwtProperties: JwtProperties){

    private val logger: Logger = LoggerFactory.getLogger(JwtUtils::class.qualifiedName)


    fun generateAccessToken(user: JwtUser): String {
        val role: String? = user.roles?.joinToString (",")

        val token = Jwts.builder()
            .setIssuer(JWT_ISSUER)
            .setSubject(user.name)
            .claim(JWT_NAME, user.name)
            .claim(JWT_USERID, user.userId)
            .claim(JWT_ROLE, role)
            .claim(JWT_EMAIL, user.email)
            .claim(JWT_LOCATION, user.location)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + JWT_EXPIRATION))
            .signWith(SignatureAlgorithm.HS512, jwtProperties.secretkey)
            .compact()
        return token
    }
    fun getUserNameFromJwtToken(token: String?): String? {
        return Jwts.parser().setSigningKey(jwtProperties.secretkey).parseClaimsJws(token).getBody().getSubject()
    }
    fun validateJwtToken(token: String?): Boolean {
        try {
            Jwts.parser().setSigningKey(jwtProperties.secretkey).parseClaimsJws(token)
            return true
        } catch (e: SignatureException) {
            logger.error("Invalid JWT signature: {}", e.message)
        } catch (e: MalformedJwtException) {
            logger.error("Invalid JWT token: {}", e.message)
        } catch (e: ExpiredJwtException) {
            logger.error("JWT token is expired: {}", e.message)
        } catch (e: UnsupportedJwtException) {
            logger.error("JWT token is unsupported: {}", e.message)
        } catch (e: IllegalArgumentException) {
            logger.error("JWT claims string is empty: {}", e.message)
        }
        return false
    }

    fun validateToken(jwtToken: String?, userDetails: CustomUserDetails): Boolean? {

        return true
    }

    fun getJwtUser(jwtToken: String?): JwtUser? {
        if (jwtToken != null) {
            val claims = Jwts.parser().setSigningKey(jwtProperties.secretkey).parseClaimsJws(jwtToken).getBody()

            val roleArray = claims.get(JWT_ROLE).toString().split(",");
            val roles: List<UserType>? =
                roleArray.map { role -> UserType.valueOf(role) }
                    .toList();

            return JwtUser(
                claims.get(JWT_USERID) as String?,
                claims.get(JWT_NAME) as String?,
                claims.get(JWT_EMAIL) as String,
                claims.get(JWT_LOCATION) as String,
                roles
            )
        }
        return null
    }
}

