package com.xcommerce.mc920.xcommerce.model

import java.io.Serializable

data class Login(val email: String, val password: String) : Serializable

data class Signup(val name: String,
                  val cpf: String,
                  val cep: String,
                  val number: String,
                  val complement: String,
                  val email: String,
                  val password: String) : Serializable

data class UserResponse(val token: String, val user: User) : Serializable

data class Edit(val name: String, val cep: String, val address: String, val email: String, val password: String) : Serializable

data class User(val name: String, val cpf: String, val cep: String, val address: AddressFull, val email: String)