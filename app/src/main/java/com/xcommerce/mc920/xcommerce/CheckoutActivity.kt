package com.xcommerce.mc920.xcommerce

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.RadioGroup

import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.content_checkout.*

class CheckoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        setSupportActionBar(toolbar)

        checkout_radio_payment.setOnCheckedChangeListener{ _, optionId ->
            val button = findViewById<Button>(R.id.checkout_add_info_credit_card)

            when (optionId) {
                R.id.checkout_radio_button_credit_card -> {
                    button.isEnabled = true
                }

                R.id.checkout_radio_button_boleto -> {
                    button.isEnabled = false
                }
            }
        }

    }

}
