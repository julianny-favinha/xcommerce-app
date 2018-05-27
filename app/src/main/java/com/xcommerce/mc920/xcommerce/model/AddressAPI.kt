package com.xcommerce.mc920.xcommerce.model

class AddressAPI {
    object CheckCep {
        const val PATH = "/address/check"

        fun of(cep: String): String {
            return PATH + "/" + cep
        }
    }
}