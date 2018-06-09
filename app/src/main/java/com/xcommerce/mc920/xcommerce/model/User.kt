package com.xcommerce.mc920.xcommerce.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable

data class Login(val email: String, val password: String) : Serializable


data class Signup(val user: User)

data class UserResponse(val token: String, val user: User) : Serializable

data class Update(val name: String,
                  val password: String,
                  val birthDate: String?,
                  val gender: String?,
                  val telephone: String?,
                  val address: AddressFull)
//data class Edit(val name: String, val cep: String, val address: String, val email: String, val password: String) : Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class User(val name: String,
                val email: String,
                val password: String,
                val birthDate: String?,
                val cpf: String,
                val address: AddressFull,
                val gender: String?,
                val telephone: String?)