package com.xcommerce.mc920.xcommerce.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

enum class PaymentType(private val _id: Int, private val _text: String) {
    CREDIT_CARD(1, "Cartão de crédito"),
    BOLETO(2, "Boleto");

    @JsonValue
    fun getId() = _id



    companion object {
        @JvmStatic
        @JsonCreator
        fun forValue(value: Int): PaymentType {
            return (PaymentType::class.java).enumConstants.first { value == it.getId() }
                    ?: throw IllegalArgumentException("Invalid id for enum")
        }

        fun getType(type: String): PaymentType {
            return PaymentType.values().find { it._text == type } ?: throw IllegalStateException("Invalid text of payment type")
        }
    }

}