package com.xcommerce.mc920.xcommerce.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.xcommerce.mc920.xcommerce.R
import com.xcommerce.mc920.xcommerce.model.CartItem
import com.xcommerce.mc920.xcommerce.utilities.DownloadImageTask
import com.xcommerce.mc920.xcommerce.utilities.formatMoney
import kotlinx.android.synthetic.main.adapter_cart_item.view.*

class CartItemAdapter(context: Context, cartItems: List<CartItem>): ArrayAdapter<CartItem>(context, 0, cartItems) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val cartItem= getItem(position)

        val product = cartItem.product
        val quantity = cartItem.quantity

        val newView = convertView
                ?: LayoutInflater.from(context).inflate(R.layout.adapter_cart_item, parent, false)

        if (quantity > 1) {
            newView.note_item_quantidade.text = context.getString(R.string.units_product)
        } else {
            newView.note_item_quantidade.text = context.getString(R.string.unit_product)
        }

        newView.note_item_name.text = product.name
        newView.note_item_val.text = quantity.toString()
        newView.note_item_price.text = formatMoney(product.price)
        product.imageUrl?.let { DownloadImageTask(newView.note_item_image).execute(product.imageUrl) }
                ?: newView.note_item_image.setImageResource(R.drawable.image_noimage)

        return newView
    }
}