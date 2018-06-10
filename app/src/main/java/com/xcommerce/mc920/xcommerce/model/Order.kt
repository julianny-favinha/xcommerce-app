package com.xcommerce.mc920.xcommerce.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class Order(val products: List<Product>,
                 val orderId: Long,
                 val totalPrice: Long,
                 val freightPrice: Long,
                 val totalQuantity: Long,
                 val shipmentInfo: ShipmentInfo,
                 val shipmentStatus: ShipmentStatus,
                 val paymentStatus: PaymentStatus,
                 val paymentType: PaymentType,
                 val barcode: String?,
                 val createdAt: String) : Serializable

data class Orders(val orders: List<Order>)

data class ShipmentInfo(val type: String, val address: String) : Serializable

data class ShipmentStatus(val status: String) : Serializable

data class PaymentType(val type: String) : Serializable

data class PaymentStatus(val status: String) : Serializable