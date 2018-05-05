package com.xcommerce.mc920.xcommerce.model

import java.io.Serializable

data class Login(val email:String, val password:String) : Serializable

data class Signup(val name:String, val cpf:String, val cep:String, val address:String, val email:String, val password:String) : Serializable

data class CEP(val cep:String) : Serializable

data class Response(val success:Boolean) : Serializable