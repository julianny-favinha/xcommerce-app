package com.xcommerce.mc920.xcommerce.model

import java.io.Serializable

data class Address(val cep: String, val logradouro: String, val neighborhood: String, val city: String, val state: String): Serializable

data class AddressFull(val address: Address, val number: Int, val complement: String?): Serializable