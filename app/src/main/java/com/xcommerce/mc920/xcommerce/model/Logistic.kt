package com.xcommerce.mc920.xcommerce.model

data class ShipmentOut(val prices: Map<String, Int>)

data class ShipmentIn(val products: List<Product>, val cepDst: String)

data class OrderIn(val product: Product, val cepDst: String)
