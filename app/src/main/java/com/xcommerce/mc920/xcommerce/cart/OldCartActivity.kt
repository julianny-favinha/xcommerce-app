package com.xcommerce.mc920.xcommerce.cart

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.xcommerce.mc920.xcommerce.checkout.CheckoutActivity
import com.xcommerce.mc920.xcommerce.R
import com.xcommerce.mc920.xcommerce.utilities.formatMoney
import kotlinx.android.synthetic.main.old_activity_cart.*
import kotlinx.android.synthetic.main.old_content_cart.*

class OldCartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            setContentView(R.layout.old_activity_cart)
            setSupportActionBar(toolbar)

            // Set the visibility of empty cart message
            if (CartHelper.retrieveCart().isEmpty()) {
                empty_cart_txt.visibility = View.VISIBLE
                list_view.visibility = View.GONE
                total_value.text = formatMoney(0)
                cart_button_next.isEnabled = false
            } else {
                empty_cart_txt.visibility = View.GONE
                list_view.visibility = View.VISIBLE
                cart_button_next.isEnabled = true
                total_value.text = formatMoney(CartHelper.retrieveCart().totalPrice)
            }

            val cartItems = CartHelper.retrieveListCart()

            val adapter = CartViewAdapter(this, cartItems)
            list_view.adapter = adapter

            // next button clicked
            cart_button_next.setOnClickListener {
                val intent = Intent(this, CheckoutActivity::class.java)
                startActivity(intent)
        }
    }
}
