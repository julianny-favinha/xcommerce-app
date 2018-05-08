package com.xcommerce.mc920.xcommerce.cart

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.xcommerce.mc920.xcommerce.R
import com.xcommerce.mc920.xcommerce.model.CartItem
import kotlinx.android.synthetic.main.adapter_cart_view.view.*
import kotlinx.android.synthetic.main.content_cart.*

class CartViewAdapter(context: Context, cartItems: List<CartItem>) : ArrayAdapter<CartItem>(context, 0, cartItems) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val cartItem= getItem(position)

        val product = cartItem.product
        val quantity = cartItem.quantity

        val ctx = context as Activity
        val totalValue = ctx.findViewById<TextView>(R.id.total_value)

        val newView = convertView
                ?: LayoutInflater.from(context).inflate(R.layout.adapter_cart_view, parent, false)

        newView.note_item_name.text = product.name
        newView.note_item_val.text = quantity.toString()
        newView.note_item_price.text = "R$" + product.price.toString()

        newView.btn_inc.setOnClickListener {
            Toast.makeText(context, "Quantidade atualizada.", Toast.LENGTH_SHORT).show()
            CartHelper.retrieveCart().add(product, 1)
            newView.note_item_val.text = CartHelper.retrieveProduct(product)!!.quantity.toString()
            totalValue.setText("R$" + CartHelper.retrieveCart().totalPrice)
            Log.d("[CART]", CartHelper.retrieveCart().cartItemMap.toString())
        }

        newView.btn_dec.setOnClickListener {
            if (CartHelper.retrieveCart().isPositive(product)) {
                Toast.makeText(context, "Quantidade atualizada.", Toast.LENGTH_SHORT).show()
                CartHelper.retrieveCart().sub(product, 1)
                newView.note_item_val.text = CartHelper.retrieveProduct(product)!!.quantity.toString()
                totalValue.setText("R$" + CartHelper.retrieveCart().totalPrice)
                Log.d("[CART]", CartHelper.retrieveCart().cartItemMap.toString())
            }
        }

        newView.btn_remove.setOnClickListener {
            remove(cartItem)
            CartHelper.retrieveCart().remove(product)
            totalValue.setText("R$0,00")
            this.notifyDataSetChanged()
        }

        return newView
    }
}
