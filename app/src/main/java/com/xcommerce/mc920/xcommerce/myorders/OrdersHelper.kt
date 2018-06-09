package com.xcommerce.mc920.xcommerce.myorders

class OrdersHelper {

    companion object {
        private var orders: Orders? = Orders()

        fun retrieveOrders(): Orders {
            return orders ?: Orders()
        }
    }
}