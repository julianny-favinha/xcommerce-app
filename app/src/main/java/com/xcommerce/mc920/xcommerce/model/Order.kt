package com.xcommerce.mc920.xcommerce.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonValue
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class Order(val products: List<Product>,
                 val orderId: Long,
                 val totalPrice: Long,
                 val freightPrice: Long,
                 val totalQuantity: Long,
                 val shipmentTYpe: ShipmentType,
                 val shipmentStatus: ShipmentStatus,
                 val paymentStatus: PaymentStatus,
                 val paymentType: PaymentType,
                 val barcode: String?,
                 val createdAt: String) : Serializable

data class Orders(val orders: List<Order>)

enum class ShipmentType(private val _id: Int) : Serializable {
    PAC(1),
    SEDEX(2);

    @JsonValue
    fun getId() = _id

    companion object {
        @JvmStatic
        @JsonCreator
        fun forValue(value: Int): ShipmentType {
            return (ShipmentType::class.java).enumConstants.first { value == it.getId() }
                    ?: throw IllegalArgumentException("Invalid id for enum")
        }

    }
}

enum class PaymentType(private val _id: Int) : Serializable {
    CREDIT_CARD(1),
    BOLETO(2);

    @JsonValue
    fun getId() = _id

    companion object {
        @JvmStatic
        @JsonCreator
        fun forValue(value: Int): PaymentType {
            return (PaymentType::class.java).enumConstants.first { value == it.getId() }
                    ?: throw IllegalArgumentException("Invalid id for enum")
        }

    }

}

enum class PaymentStatus(private val _id: Int) : Serializable {
    PENDING(1),
    OK(2),
    EXPIRED(3),
    CANCELED(4),
    ERROR(Integer.MAX_VALUE);

    @JsonValue
    fun getId() = _id

    companion object {
        @JvmStatic
        @JsonCreator
        fun forValue(value: Int): PaymentStatus {
            return (PaymentStatus::class.java).enumConstants.first { value == it.getId() }
                    ?: throw IllegalArgumentException("Invalid id for enum")
        }

    }
}

enum class ShipmentStatus(val _id: Int) : Serializable {
    NOT_STARTED(1),
    PREPARING_FOR_SHIPMENT(2),
    SHIPPED(3),
    DELIVERED(4),
    ERROR(Integer.MAX_VALUE);

    @JsonValue
    fun getId() = _id

    companion object {
        @JvmStatic
        @JsonCreator
        fun forValue(value: Int): ShipmentStatus {
            return (ShipmentStatus::class.java).enumConstants.first { value == it.getId() }
                    ?: throw IllegalArgumentException("Invalid id for enum")
        }

    }
}