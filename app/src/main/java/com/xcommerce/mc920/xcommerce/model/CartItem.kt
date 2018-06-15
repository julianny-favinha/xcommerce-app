package com.xcommerce.mc920.xcommerce.model

<<<<<<< HEAD
data class CartItem(val product: Product, var quantity: Int)
=======
import java.io.Serializable

data class CartItem(val product: Product, val quantity: Int) : Serializable
>>>>>>> my_orders
