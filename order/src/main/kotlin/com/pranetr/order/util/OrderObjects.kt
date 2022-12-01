package com.pranetr.order.util

import com.pranetr.foundation.logger.EventType

class OrderObjects {
}
enum class OrderEvent: EventType {
    ORDER_SAVE ,
    ORDER_UPDATE,
    ORDER_FIND,
    CART_FIND,
    CART_CREATE,
    CART_UPDATE,
    CART_CONFIRM

}