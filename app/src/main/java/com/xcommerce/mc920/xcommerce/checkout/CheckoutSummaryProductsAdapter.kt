package com.xcommerce.mc920.xcommerce.checkout

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.xcommerce.mc920.xcommerce.R
import com.xcommerce.mc920.xcommerce.model.Product
import com.xcommerce.mc920.xcommerce.utilities.DownloadImageTask
import com.xcommerce.mc920.xcommerce.utilities.formatMoney
import kotlinx.android.synthetic.main.adapter_summary_product.view.*

class CheckoutSummaryProductsAdapter(context: Context, products: List<Product>): ArrayAdapter<Product>(context, 0, products){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val product = getItem(position)
        val ctx = context as Activity
        val newView = convertView ?: LayoutInflater.from(context).inflate(R.layout.adapter_summary_product, parent, false)
        newView.product_name.text = product.name
        val priceString = formatMoney(product.price)
        newView.product_price.text = priceString
        product.imageUrl?.let { DownloadImageTask(newView.product_image).execute(product.imageUrl) }
                ?: newView.product_image.setImageResource(R.drawable.image_noimage)

        return newView
    }
}