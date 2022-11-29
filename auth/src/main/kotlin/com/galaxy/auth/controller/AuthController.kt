package com.galaxy.auth.controller

import com.galaxy.auth.codegen.types.AuthPayload
import com.galaxy.auth.services.UserService
import com.galaxy.foundation.context.CustomContext
import com.galaxy.foundation.logger.EventLogger
import com.galaxy.foundation.constants.DEFAULT_LOCATION
import com.netflix.graphql.dgs.*
import com.netflix.graphql.dgs.context.DgsContext
import graphql.schema.DataFetchingEnvironment
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@DgsComponent
class AuthController(private val userService:   UserService ,
                     private val bCryptPasswordEncoder: BCryptPasswordEncoder,
                     private val eventLogger: EventLogger) {

    @DgsMutation
    fun signup(@InputArgument email: String,
               @InputArgument password: String,
               @InputArgument username: String,
               dfe: DataFetchingEnvironment?): AuthPayload
    {
        val context= DgsContext.getCustomContext<CustomContext>(dfe!!);
        val location = context.location ?: DEFAULT_LOCATION
//        eventLogger.log()
        return userService.signup(email,bCryptPasswordEncoder.encode(password),username,location)?: AuthPayload()
    }
    @DgsMutation
    fun signin(@InputArgument email: String,
               @InputArgument password: String,
               dfe: DataFetchingEnvironment?): AuthPayload
    {
        val context= DgsContext.getCustomContext<CustomContext>(dfe!!);
        val location = context.location ?: DEFAULT_LOCATION
        return userService.signin(email,bCryptPasswordEncoder.encode(password),location)?: AuthPayload()
    }
}
