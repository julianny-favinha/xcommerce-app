package com.xcommerce.mc920.xcommerce

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.content_address.*
import kotlinx.android.synthetic.main.content_checkout.*

class CheckoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        setSupportActionBar(toolbar)

        checkout_radio_payment.setOnCheckedChangeListener{ _, optionId ->
            val button = findViewById<Button>(R.id.checkout_button_add_info_card)

            when (optionId) {
                R.id.checkout_radio_button_credit_card -> {
                    button.isEnabled = true
                }

                R.id.checkout_radio_button_boleto -> {
                    button.isEnabled = false
                }
            }
        }

        // add new address
        checkout_button_add_address.setOnClickListener {
            val intent = Intent(this, AddressActivity::class.java)
            startActivity(intent)
        }

        // calculate shipping rates
        checkout_button_calculate_shipping.setOnClickListener {
            checkout_price_pac.visibility = View.VISIBLE
            checkout_price_sedex.visibility = View.VISIBLE
        }

        // add credit card info
        checkout_button_add_info_card.setOnClickListener {
            val intent = Intent(this, InfoCreditCardActivity::class.java)
            startActivity(intent)
        }

        // finish shopping
        checkout_button.setOnClickListener{

        }
    }

}
