package com.xcommerce.mc920.xcommerce.model

import android.util.Log
import com.xcommerce.mc920.xcommerce.cart.CartActivity
import com.xcommerce.mc920.xcommerce.cart.ReleaseTask
import com.xcommerce.mc920.xcommerce.cart.ReserveTask

data class CartIn(val cartItems: List<CartItem>)

class Cart {
    var cartItemMap = emptyMap<Product, Int>().toMutableMap()
    var totalPrice = 0
    var totalQuantity = 0

    fun add(product: Product, quantity: Int, task: ReserveTask?) {
        if (cartItemMap.containsKey(product)) {
            cartItemMap[product] = cartItemMap[product]!! + quantity
        } else {
            cartItemMap[product] = quantity
        }

        totalPrice += product.price * quantity
        totalQuantity += quantity

        task?.execute(CartItem(product, quantity))
    }

    fun sub(product: Product, quantity: Int, task: ReleaseTask?) {
        if(!cartItemMap.containsKey(product)) throw IllegalStateException("Product not found in cart!")
        val currentQuantity = cartItemMap[product]!!

        if(currentQuantity < 0 || currentQuantity < quantity) throw IllegalStateException("Quantity to decrease is not valid!")

        if(currentQuantity == quantity) {
            cartItemMap.remove(product)
        } else {
            cartItemMap[product] = cartItemMap[product]!! - quantity
        }

        totalPrice -= product.price * quantity
        totalQuantity -= quantity

        Log.d("quantity", quantity.toString())
        task?.execute(CartItem(product, quantity))
    }

    fun remove(product: Product) {
        if(!cartItemMap.containsKey(product)) throw IllegalStateException("Product not found in cart!")
        val quantity = cartItemMap[product]!!

        val task = ReleaseTask()
        task?.execute(CartItem(product, quantity))

        cartItemMap.remove(product)
        totalPrice -= product.price * quantity
        totalQuantity -= quantity
    }

    fun clear() {
        for ((product, _) in cartItemMap){
            remove(product)
        }
        cartItemMap.clear()
        totalPrice = 0
        totalQuantity = 0
    }

    fun isPositive(product: Product): Boolean {
        if(!cartItemMap.containsKey(product)) throw IllegalStateException("Product not found in cart!")
        val quantity = cartItemMap[product]!!

        return quantity > 1
    }

    fun isEmpty(): Boolean {
        return cartItemMap.isEmpty()
    }
}