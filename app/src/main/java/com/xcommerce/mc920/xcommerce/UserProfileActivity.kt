package com.xcommerce.mc920.xcommerce

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class UserProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        /**TODO: Check if user is logged in
        if (User.id != null){
            findViewById<TextView>(R.id.name).apply { text = User.name }
            findViewById<TextView>(R.id.email).apply { text = User.email }
            findViewById<TextView>(R.id.cpf).apply { text = User.cpf }
            findViewById<TextView>(R.id.cep).apply { text = User.cep }
            findViewById<TextView>(R.id.address).apply { text = User.address }
        } else {
            finish()
        }*/

    }
}
