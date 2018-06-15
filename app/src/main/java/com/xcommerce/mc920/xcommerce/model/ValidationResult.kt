package com.xcommerce.mc920.xcommerce.model

enum class ValidationStatus: java.io.Serializable {
    OK,
    INVALID_USER,
    INVALID_PRODUCTS,
    PRODUCTS_UNAVAILABLE,
    INVALID_PAYMENT_PARAMETERS,
    INVALID_SHIPMENT_PARAMETERS;

    fun success() = this == OK
}