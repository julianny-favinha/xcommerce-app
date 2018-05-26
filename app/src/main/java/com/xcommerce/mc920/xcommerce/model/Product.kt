package com.xcommerce.mc920.xcommerce.model

import java.io.Serializable

//data class Product(val id: Int, val name: String, val brand: String, val price: Int, val category: String?, val description: String, val imageUrl: String?): Serializable

data class Product(val id: Int, val name: String, val brand: String, val price: Int, val weight: Long, val length: Long, val width: Long,
                   val height: Long, val category: String?, val description: String, val imageUrl: String?): Serializable

data class LightProduct(val id: Int, val name: String, val price: Int, val imageUrl: String?)

data class Highlights(val highlights: List<Product>)

data class Categories(var categories: List<Category>)