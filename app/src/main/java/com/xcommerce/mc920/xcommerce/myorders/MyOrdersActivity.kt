package com.xcommerce.mc920.xcommerce.myorders

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.xcommerce.mc920.xcommerce.R
import com.xcommerce.mc920.xcommerce.user.LoginActivity
import com.xcommerce.mc920.xcommerce.user.UserHelper

import kotlinx.android.synthetic.main.activity_my_orders.*

class MyOrdersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_orders)
        setSupportActionBar(toolbar)

        if (!UserHelper.isLoggedIn()) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // back button
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume(){
        super.onResume()

        if (!UserHelper.isLoggedIn()) {
            finish()
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
