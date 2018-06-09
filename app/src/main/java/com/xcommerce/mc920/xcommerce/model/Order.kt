package com.xcommerce.mc920.xcommerce.model

data class Order(val products: List<Product>,
                 val orderId: Long,
                 val totalPrice: Long,
                 val freightPrice: Long,
//                 val totalQuantity: Long,
//                 val shipmentInfo: ShipmentInfo,
//                 val shipmentStatus: ShipmentStatus,
//                 val paymentStatus: PaymentStatus,
//                 val paymentType: PaymentType,
                 val barcode: String?,
                 val createdAt: String)


