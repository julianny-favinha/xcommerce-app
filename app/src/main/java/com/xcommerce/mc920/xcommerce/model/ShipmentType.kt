package com.xcommerce.mc920.xcommerce.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

enum class ShipmentType(private val _id: Int, private val _text: String) {
    PAC(1, "PAC"),
    SEDEX(2, "Sedex");

    @JsonValue
    fun getId() = _id

    companion object {
        @JvmStatic
        @JsonCreator
        fun forValue(value: Int): ShipmentType {
            return (ShipmentType::class.java).enumConstants.first { value == it.getId() }
                    ?: throw IllegalArgumentException("Invalid id for enum")
        }

        fun getType(type: String): ShipmentType {
            return ShipmentType.values().find { it._text == type } ?: throw IllegalStateException("Invalid text of shipment type")
        }
    }

}