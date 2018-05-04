package com.xcommerce.mc920.xcommerce.model

import java.io.Serializable

data class Product(val id: Int, val name: String, val brand: String, val price: Int, val category: String, val description: String, val imageUrl: String?): Serializable

data class Highlights(val highlights: List<Product>)
