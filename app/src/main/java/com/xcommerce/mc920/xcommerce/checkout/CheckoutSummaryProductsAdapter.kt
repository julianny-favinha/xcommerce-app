package com.xcommerce.mc920.xcommerce.checkout

import android.animation.LayoutTransition
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.xcommerce.mc920.xcommerce.R
import com.xcommerce.mc920.xcommerce.model.LightProduct
import com.xcommerce.mc920.xcommerce.utilities.DownloadImageTask
import kotlinx.android.synthetic.main.adapter_summary_product.view.*
import kotlinx.android.synthetic.main.content_checkout.*

class CheckoutSummaryProductsAdapter(context: Context, lightProducts: List<LightProduct>): ArrayAdapter<LightProduct>(context, 0, lightProducts){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val product = getItem(position)
        val ctx = context as Activity
        val newView = convertView ?: LayoutInflater.from(context).inflate(R.layout.adapter_summary_product, parent, false)
        newView.product_name.text = product.name
        val priceString = "R$" + ("%.2f".format((product.price / 100.0))).toString()
        newView.product_price.text = priceString
        DownloadImageTask(newView.product_image).execute(product.imageUrl)

        return newView
    }
}