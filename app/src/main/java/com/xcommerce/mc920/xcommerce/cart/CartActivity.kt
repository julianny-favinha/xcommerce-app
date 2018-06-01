package com.xcommerce.mc920.xcommerce.cart

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.UserHandle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.xcommerce.mc920.xcommerce.LoginActivity
import com.xcommerce.mc920.xcommerce.R
import com.xcommerce.mc920.xcommerce.model.CartItem
import com.xcommerce.mc920.xcommerce.model.Product
import com.xcommerce.mc920.xcommerce.model.ProductAPI
import com.xcommerce.mc920.xcommerce.user.UserHelper
import com.xcommerce.mc920.xcommerce.utilities.ClientHttpUtil

import com.xcommerce.mc920.xcommerce.checkout.CheckoutActivity
import com.xcommerce.mc920.xcommerce.utilities.formatMoney
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.content_cart.*

class CartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        setSupportActionBar(toolbar)

        // Set the visibility of empty cart message
        if (CartHelper.retrieveCart().isEmpty()) {
            text_view_no_items.visibility = View.VISIBLE
            list_view.visibility = View.GONE
            linear_layout.visibility = View.GONE
            total_value.text = formatMoney(0)
            cart_button_next.isEnabled = false
        } else {
            text_view_no_items.visibility = View.GONE
            list_view.visibility = View.VISIBLE
            linear_layout.visibility = View.VISIBLE
            cart_button_next.isEnabled = true
            total_value.text = formatMoney(CartHelper.retrieveCart().totalPrice)
        }

        val cartItems = CartHelper.retrieveListCart()

        val adapter = CartViewAdapter(this, cartItems)
        list_view.adapter = adapter

        // next button clicked
        cart_button_next.setOnClickListener {
            if (UserHelper.retrieveNullableUser() == null) {
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
