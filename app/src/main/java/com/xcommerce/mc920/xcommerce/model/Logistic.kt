package com.xcommerce.mc920.xcommerce.model

data class ShipmentOut(val prices: Map<String, Int>)

data class ShipmentIn(val typeShip: String, val cepFrom: String, val cepDst: String, val weight: Long, val typePack: String, val length: Long, val height: Long, val width: Long)

data class OrderIn(val product: Product, val cepDst: String)
