package com.xcommerce.mc920.xcommerce.cart

import com.xcommerce.mc920.xcommerce.model.Cart

class CartHelper {
    companion object {
        private var cart: Cart? = Cart()

        fun retrieveCart(): Cart {
            return cart ?: Cart()
        }
    }
}