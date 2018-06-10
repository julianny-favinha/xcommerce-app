package com.xcommerce.mc920.xcommerce.myorders

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.xcommerce.mc920.xcommerce.R
import com.xcommerce.mc920.xcommerce.model.Order
import com.xcommerce.mc920.xcommerce.utilities.formatMoney
import kotlinx.android.synthetic.main.adapter_my_orders_view.view.*

class MyOrdersViewAdapter(context: Context, orderItems: List<Order>) : ArrayAdapter<Order>(context, 0, orderItems) {


     override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val orderItem= getItem(position)

            val orderId = orderItem.orderId
            val totalPrice = orderItem.totalPrice
            val createdAt = orderItem.createdAt
            val totalQuantity = orderItem.totalQuantity

            val ctx = context as Activity

            val newView = convertView
                    ?: LayoutInflater.from(context).inflate(R.layout.adapter_my_orders_view, parent, false)

            newView.product_order_value.text =  orderId.toString()
            newView.product_nitems_value.text = totalQuantity.toString()
            newView.product_date_value.text = createdAt
            newView.product_price_value.text = formatMoney(totalPrice.toInt())

            newView.order_element.setOnClickListener {
                   val intent = Intent(ctx, OrderDetailActivity::class.java)
                   intent.putExtra("order", orderItem)
                   ctx.startActivity(intent)
            }

            return newView
     }
}