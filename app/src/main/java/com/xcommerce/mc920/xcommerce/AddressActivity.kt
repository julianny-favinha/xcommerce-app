package com.xcommerce.mc920.xcommerce

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

import kotlinx.android.synthetic.main.activity_address.*
import kotlinx.android.synthetic.main.content_address.*

class AddressActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)
        setSupportActionBar(toolbar)

        // back button
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
        toolbar.setNavigationOnClickListener {
            startActivity(Intent(applicationContext, CheckoutActivity::class.java))
        }

        // validate CEP
        address_button_validate.setOnClickListener {
            address_info.visibility = View.VISIBLE
        }

        // save address
        address_button_save.setOnClickListener {

        }
    }

}
