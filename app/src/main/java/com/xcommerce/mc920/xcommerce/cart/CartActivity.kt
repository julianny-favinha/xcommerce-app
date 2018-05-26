package com.xcommerce.mc920.xcommerce.cart

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.UserHandle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.xcommerce.mc920.xcommerce.CheckoutActivity
import com.xcommerce.mc920.xcommerce.LoginActivity
import com.xcommerce.mc920.xcommerce.R
import com.xcommerce.mc920.xcommerce.model.CartItem
import com.xcommerce.mc920.xcommerce.model.Product
import com.xcommerce.mc920.xcommerce.model.ProductAPI
import com.xcommerce.mc920.xcommerce.user.UserHelper
import com.xcommerce.mc920.xcommerce.utilities.ClientHttpUtil

import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.content_cart.*

class CartActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        setSupportActionBar(toolbar)

        // Set the visibility of empty cart message
        if(CartHelper.retrieveCart().isEmpty()) {
            empty_cart_txt.setVisibility(View.VISIBLE)
            list_view.setVisibility(View.GONE)
            total_value.setText("0")
        } else {
            empty_cart_txt.setVisibility(View.GONE)
            list_view.setVisibility(View.VISIBLE)
            total_value.setText("R$" + CartHelper.retrieveCart().totalPrice)
        }

//
//        val values = listOf(CartItem(product = Product(
//                id = 1,
//                name = "blabal",
//                brand = "balbal",
//                price = 3000,
//                category = "blablba",
//                description = "blablba",
//                imageUrl = null
//        ), quantity = 3), CartItem(product = Product(
//                id = 1,
//                name = "blabal",
//                brand = "balbal",
//                price = 3000,
//                category = "blablba",
//                description = "blablba",
//                imageUrl = null
//        ), quantity = 2), CartItem(product = Product(
//                id = 1,
//                name = "blabal",
//                brand = "balbal",
//                price = 3000,
//                category = "blablba",
//                description = "blablba",
//                imageUrl = null
//        ), quantity = 1), CartItem(product = Product(
//                id = 1,
//                name = "blabal",
//                brand = "balbal",
//                price = 3000,
//                category = "blablba",
//                description = "blablba",
//                imageUrl = null
//        ), quantity = 7))

        val cartItems = CartHelper.retrieveListCart()

        val adapter = CartViewAdapter(this, cartItems)
        list_view.adapter = adapter

        // next button clicked
        cart_button_next.setOnClickListener {
            if (UserHelper.retrieveUser() == null) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, CheckoutActivity::class.java)
                startActivity(intent)
            }
        }
    }

    inner class ReserveTask internal constructor(val cartItem: CartItem) : AsyncTask<Void, Void, Boolean>() {

        override fun doInBackground(vararg p0: Void?): Boolean {
            return ClientHttpUtil.postRequest(ProductAPI.Reserve.PATH, cartItem) ?: false
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)

            if (result == false){
                CartHelper.retrieveCart().sub(cartItem.product, cartItem.quantity, null)
            }
        }
    }

}
