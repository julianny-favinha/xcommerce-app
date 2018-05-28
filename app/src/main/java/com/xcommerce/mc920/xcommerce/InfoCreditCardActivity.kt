package com.xcommerce.mc920.xcommerce

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_info_credit_card.*
import android.content.Intent
import com.xcommerce.mc920.xcommerce.checkout.CheckoutActivity
import kotlinx.android.synthetic.main.content_info_credit_card.*


class InfoCreditCardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_credit_card)
        setSupportActionBar(toolbar)

        // back button
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
        toolbar.setNavigationOnClickListener {
            startActivity(Intent(applicationContext, CheckoutActivity::class.java))
        }

        // save information
        credit_card_button_save.setOnClickListener {
            
        }
    }

    
}
