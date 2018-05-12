package com.xcommerce.mc920.xcommerce.cart

import com.xcommerce.mc920.xcommerce.model.Cart
import com.xcommerce.mc920.xcommerce.model.CartItem
import com.xcommerce.mc920.xcommerce.model.Product

class CartHelper {
    companion object {
        private var cart: Cart? = Cart()

        fun retrieveCart(): Cart {
            return cart ?: Cart()
        }

        fun retrieveListCart(): List<CartItem> {
            return retrieveCart().cartItemMap.map { (k, v) -> CartItem(k, v) }
        }

        fun retrieveProduct(product: Product): CartItem? {
            return retrieveCart().cartItemMap.filter { (k, _) -> k == product }.map { (k, v) -> CartItem(k, v) }.firstOrNull()
        }
    }
}