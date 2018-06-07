package com.xcommerce.mc920.xcommerce.myorders

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.xcommerce.mc920.xcommerce.R

import kotlinx.android.synthetic.main.activity_my_orders.*

class MyOrdersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_orders)
        setSupportActionBar(toolbar)
        
        // back button
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
