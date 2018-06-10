package com.xcommerce.mc920.xcommerce.myorders

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.xcommerce.mc920.xcommerce.R
import com.xcommerce.mc920.xcommerce.model.Order
import com.xcommerce.mc920.xcommerce.utilities.formatMoney

import kotlinx.android.synthetic.main.activity_order_detail.*
import kotlinx.android.synthetic.main.order_detail.*
import kotlinx.android.synthetic.main.order_payment_method.*
import kotlinx.android.synthetic.main.order_products.*
import kotlinx.android.synthetic.main.order_shipping.*

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

            // Payment method
            if(order.paymentType.type == "BOLETO") {
                this.method_val.text = "Boleto bancário"
                this.boleto_num_val.text = order.barcode
            } else if(order.paymentType.type == "CREDIT_CARD"){
                this.method_val.text = "Cartão de cŕedito"
                this.boleto_num_val.visibility = View.GONE
                this.boleto_num_label.visibility = View.GONE
                this.boleto_venc_label.visibility = View.GONE
                this.boleto_venc_val.visibility = View.GONE
            }

            // Product
            for(product in order.products) {
                this.product_name.text = product.name
                this.product_price.text = formatMoney(product.price)
            }

            // Shipping info
            this.shipping_address_val.text = order.shipmentInfo.address
            this.shipping_type_val.text = order.shipmentInfo.type
            this.shipping_status_val.text = order.shipmentStatus.status
        }

    }



}
