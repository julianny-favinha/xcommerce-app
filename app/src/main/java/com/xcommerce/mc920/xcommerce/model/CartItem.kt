package com.xcommerce.mc920.xcommerce.model

import java.io.Serializable

data class CartItem(val product: Product, var quantity: Int) : Serializable
