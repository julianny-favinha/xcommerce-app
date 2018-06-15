package com.xcommerce.mc920.xcommerce.myorders

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.xcommerce.mc920.xcommerce.R
import com.xcommerce.mc920.xcommerce.cart.CartItemAdapter
import com.xcommerce.mc920.xcommerce.model.*
import com.xcommerce.mc920.xcommerce.utilities.UIUtils
import com.xcommerce.mc920.xcommerce.utilities.formatMoney

import kotlinx.android.synthetic.main.activity_order_detail.*
import kotlinx.android.synthetic.main.order_detail.*
import kotlinx.android.synthetic.main.order_payment_method.*
import kotlinx.android.synthetic.main.order_products.*
import kotlinx.android.synthetic.main.order_shipping.*
import kotlinx.android.synthetic.main.summary_products.*

class OrderDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)
        setSupportActionBar(toolbar)

        // back button
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val order = intent.getSerializableExtra("order")

        if (order is Order) {
            // Order detail
            this.number_val.text = order.orderId.toString()
            this.date_val.text = order.createdAt
            this.total_val.text = formatMoney(order.totalPrice.toInt())
            this.shipping_address_val.text = order.shipmentInfo.cepDst


            // Payment method
            if (order.paymentType == PaymentType.BOLETO) {
                // calculate boleto expiration based on date of creation (+5 days from that)
                val createdSplit = order.createdAt.split("/")
                val expBoleto = (createdSplit[0].toInt() + 5).toString() + "/" + createdSplit[1] + "/" + createdSplit[2]
                this.method_val.text = "Boleto bancário"
                this.boleto_venc_val.text = expBoleto
                this.boleto_num_val.text = order.barcode
            } else if (order.paymentType == PaymentType.CREDIT_CARD) {
                this.method_val.text = "Cartão de cŕedito"
                this.boleto_num_val.visibility = View.GONE
                this.boleto_num_label.visibility = View.GONE
                this.boleto_venc_label.visibility = View.GONE
                this.boleto_venc_val.visibility = View.GONE
            }

            if (order.paymentStatus == PaymentStatus.CANCELED) {
                this.payment_status_val.text = "Cancelado"
            } else if (order.paymentStatus == PaymentStatus.ERROR) {
                this.payment_status_val.text = "Erro na cobrança"
            } else if (order.paymentStatus == PaymentStatus.EXPIRED) {
                this.payment_status_val.text = "Prazo expirado"
            } else if (order.paymentStatus == PaymentStatus.OK) {
                this.payment_status_val.text = "Pagamento aprovado"
            } else if (order.paymentStatus == PaymentStatus.PENDING) {
                this.payment_status_val.text = "Pagamento pendente"
            }

            // Products
            val adapter = CartItemAdapter(this, order.productsByQuantity)
            list_view_completed.adapter = adapter
            UIUtils.setListViewHeightBasedOnItems(list_view_completed)

            // Shipping info
            if (order.shipmentStatus == ShipmentStatus.PREPARING_FOR_SHIPMENT) {
                this.shipping_status_val.text = "Preparando para entrega"
            } else if (order.shipmentStatus == ShipmentStatus.DELIVERED) {
                this.shipping_status_val.text = "Entregue"
            } else if (order.shipmentStatus == ShipmentStatus.NOT_STARTED) {
                this.shipping_status_val.text = "Não iniciada"
            } else if (order.shipmentStatus == ShipmentStatus.SHIPPED) {
                this.shipping_status_val.text = "Saiu para entrega"
            } else if (order.shipmentStatus == ShipmentStatus.ERROR) {
                this.shipping_status_val.text = "Houve um erro"
            }

            if (order.shipmentInfo.shipmentType == ShipmentType.PAC) {
                this.shipping_type_val.text = "PAC"
            } else if (order.shipmentInfo.shipmentType == ShipmentType.SEDEX) {
                this.shipping_type_val.text = "Sedex"
            }

            if (order.shipmentStatus == ShipmentType.PAC) {
                this.shipping_type_val.text = "PAC"
            } else if (order.shipmentStatus == ShipmentType.SEDEX) {
                this.shipping_type_val.text = "Sedex"
            }


        }
    }
}
