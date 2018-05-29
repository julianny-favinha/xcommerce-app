package com.xcommerce.mc920.xcommerce.myorders

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.xcommerce.mc920.xcommerce.R

import kotlinx.android.synthetic.main.activity_order_detail.*

class OrderDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)
        setSupportActionBar(toolbar)

    }

}
