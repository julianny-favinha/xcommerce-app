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
import com.xcommerce.mc920.xcommerce.utilities.DownloadImageTask
import com.xcommerce.mc920.xcommerce.utilities.formatMoney
import kotlinx.android.synthetic.main.adapter_cart_view.view.*
import kotlinx.android.synthetic.main.old_content_cart.*

class CartViewAdapter(context: Context, cartItems: List<CartItem>) : ArrayAdapter<CartItem>(context, 0, cartItems) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var cartItem= getItem(position)

        val ctx = context as Activity
        val totalValue = ctx.findViewById<TextView>(R.id.total_value)

        val newView = convertView
                ?: LayoutInflater.from(context).inflate(R.layout.adapter_cart_view, parent, false)

        newView.note_item_name.text = cartItem.product.name
        newView.note_item_val.text = cartItem.quantity.toString()
        newView.note_item_price.text = formatMoney(cartItem.product.price)
        cartItem.product.imageUrl?.let { DownloadImageTask(newView.note_item_image).execute(cartItem.product.imageUrl) }
                ?: newView.note_item_image.setImageResource(R.drawable.image_noimage)

        newView.btn_inc.setOnClickListener {
            CartHelper.retrieveCart().add(cartItem.product, 1, ReserveTask())
            Toast.makeText(context, "Quantidade atualizada.", Toast.LENGTH_SHORT).show()
            newView.note_item_val.text = CartHelper.retrieveProduct(cartItem.product)!!.quantity.toString()
            totalValue.text = formatMoney(CartHelper.retrieveCart().totalPrice)
            cartItem.quantity += 1
            Log.d("[CART]", CartHelper.retrieveCart().cartItemMap.toString())
        }

        newView.btn_dec.setOnClickListener {
            if (CartHelper.retrieveCart().isPositive(cartItem.product)) {
                CartHelper.retrieveCart().sub(cartItem.product, 1, ReleaseTask())
                Toast.makeText(context, "Quantidade atualizada.", Toast.LENGTH_SHORT).show()
                newView.note_item_val.text = CartHelper.retrieveProduct(cartItem.product)!!.quantity.toString()
                totalValue.text = formatMoney(CartHelper.retrieveCart().totalPrice)
                cartItem.quantity -= 1
                Log.d("[CART]", CartHelper.retrieveCart().cartItemMap.toString())
            }
        }

        newView.btn_remove.setOnClickListener {
            Log.d("cartItem", cartItem.toString())
            remove(cartItem)
            CartHelper.retrieveCart().remove(cartItem.product)
            totalValue.text = formatMoney(0)
            this.notifyDataSetChanged()
        }

        return newView
    }
}
