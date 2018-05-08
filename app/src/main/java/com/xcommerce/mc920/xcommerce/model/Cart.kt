package com.xcommerce.mc920.xcommerce.model

class Cart {
    var cartItemMap = emptyMap<Product, Int>().toMutableMap()
    var totalPrice = 0
    var totalQuantity = 0

    fun add(product: Product, quantity: Int) {
        if (cartItemMap.containsKey(product)) {
            cartItemMap[product] = cartItemMap[product]!! + quantity
        } else {
            cartItemMap[product] = quantity
        }

        totalPrice += product.price * quantity
        totalQuantity += quantity
    }

    fun update(product: Product, newQuantity: Int) {
        if(!cartItemMap.containsKey(product)) throw IllegalStateException("Product not found in cart!")
        if(newQuantity < 0) throw IllegalStateException("Quantity to update is not valid!")

        val currentQuantity = cartItemMap[product]!!
        val currentPrice = currentQuantity * product.price
        val newPrice = newQuantity * product.price

        cartItemMap[product] = newQuantity
        totalPrice = totalPrice - currentPrice + newPrice
        totalQuantity = totalQuantity - currentQuantity + newQuantity
    }

    fun sub(product: Product, quantity: Int) {
        if(!cartItemMap.containsKey(product)) throw IllegalStateException("Product not found in cart!")
        val currentQuantity = cartItemMap[product]!!

        if(currentQuantity < 0 || currentQuantity < quantity) throw IllegalStateException("Quantity to decrease is not valid!")

        if(currentQuantity == quantity){
            cartItemMap.remove(product)
        } else {
            cartItemMap[product] = cartItemMap[product]!! - quantity
        }

        totalPrice -= product.price * quantity
        totalQuantity -= quantity
    }

    fun remove(product: Product) {
        if(!cartItemMap.containsKey(product)) throw IllegalStateException("Product not found in cart!")
        val quantity = cartItemMap[product]!!

        cartItemMap.remove(product)
        totalPrice -= product.price * quantity
        totalQuantity -= quantity
    }

    fun clear() {
        cartItemMap.clear()
        totalPrice = 0
        totalQuantity = 0
    }

    fun isPositive(product: Product): Boolean {
        if(!cartItemMap.containsKey(product)) throw IllegalStateException("Product not found in cart!")
        val quantity = cartItemMap[product]!!

        return quantity > 1
    }
}