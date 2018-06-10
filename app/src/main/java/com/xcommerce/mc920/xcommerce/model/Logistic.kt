package com.xcommerce.mc920.xcommerce.model

import java.io.Serializable

data class ShipmentOut(val prices: Map<String, Int>, val prazos: Map<String, Int>)

data class Delivery(val type: String, val price: Int, val prazo: Int): Serializable

data class ShipmentIn(val products: List<Product>, val cepDst: String)

data class OrderIn(val product: Product, val cepDst: String)
