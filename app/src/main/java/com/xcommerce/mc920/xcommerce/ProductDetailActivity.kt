package com.xcommerce.mc920.xcommerce

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.xcommerce.mc920.xcommerce.cart.CartActivity
import com.xcommerce.mc920.xcommerce.cart.CartHelper
import com.xcommerce.mc920.xcommerce.cart.ReserveTask
import com.xcommerce.mc920.xcommerce.model.CartItem
import com.xcommerce.mc920.xcommerce.model.Product
import com.xcommerce.mc920.xcommerce.utilities.DownloadImageTask
import com.xcommerce.mc920.xcommerce.utilities.formatMoney

import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.content_product_detail.*

class ProductDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        setSupportActionBar(toolbar)

        // back button
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        Log.d("[CART]", CartHelper.retrieveCart().cartItemMap.toString())

        // get intent
        val product = intent.getSerializableExtra("product")

        if (product is Product) {
            fab.setOnClickListener { _ ->
                CartHelper.retrieveCart().add(product, 1, ReserveTask(CartItem(product, 1)))
                finish()
            }

            // TODO: get product by id from back-end
            val codeString = "Código " + product.id.toString()
            this.product_detail_id.text = codeString
            this.product_detail_name.text = product.name
            this.product_detail_brand.text = product.brand
            val priceString = formatMoney(product.price)
            this.product_detail_price.text = priceString
            this.product_detail_category.text = product.category
            this.product_detail_description.text = product.description
            product.imageUrl?.let { DownloadImageTask(this.product_detail_image).execute(product.imageUrl) }
                    ?: this.product_detail_image.setImageResource(R.drawable.image_noimage)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
