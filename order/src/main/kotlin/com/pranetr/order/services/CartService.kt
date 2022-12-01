
package com.pranetr.order.services

import com.pranetr.order.codegen.types.Cart
import com.pranetr.order.codegen.types.ReturnCart
import org.springframework.stereotype.Service

@Service
class CartService {
    fun getCart(orderId:String) : ReturnCart {
        return ReturnCart(orderId,0.0, 0.0, 0.0, 0.0 ,
            listOf(),null)
    }

    fun createCart(userId: String?, skuid: String, quantity: Int, fulfillment: String): ReturnCart {

        return ReturnCart("CART1",0.0, 0.0, 0.0, 0.0 ,
            listOf(),null)
    }
}