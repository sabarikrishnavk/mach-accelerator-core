package com.pranetr.order.controller

import com.pranetr.foundation.context.CustomContext
import com.pranetr.foundation.logger.EventLogger
import com.pranetr.order.codegen.types.Cart
import com.pranetr.order.codegen.types.ReturnCart
import com.pranetr.order.services.CartService
import com.pranetr.order.util.OrderEvent
import com.netflix.graphql.dgs.*
import com.netflix.graphql.dgs.context.DgsContext
import graphql.schema.DataFetchingEnvironment
import org.springframework.security.access.prepost.PreAuthorize

@DgsComponent
class CartController(private val cartService: CartService, val eventLogger: EventLogger) {
    /**
     * This datafetcher resolves the shows field on Query.
     * It uses an @InputArgument to get the titleFilter from the Query if one is defined.
     */

    @DgsQuery
    @PreAuthorize("hasAnyRole('ROLE_REGISTERED','ROLE_GUEST')")
    fun findcart(@InputArgument cartid : String): ReturnCart {
        eventLogger.log( OrderEvent.CART_FIND, "find cart", cartid)
        return cartService.getCart(cartid)
    }


    @DgsMutation
    @PreAuthorize("hasAnyRole('ROLE_REGISTERED','ROLE_GUEST')")
    fun addcartitem(skuid:String , quantity:Int, fulfillment:String,
                          dfe: DataFetchingEnvironment?): ReturnCart {

        eventLogger.log(OrderEvent.CART_FIND, "addcartitem: ", skuid, quantity, fulfillment)

        val context = DgsContext.getCustomContext<CustomContext>(dfe!!);
        return cartService.createCart(context.userId, skuid, quantity, fulfillment)
    }
}