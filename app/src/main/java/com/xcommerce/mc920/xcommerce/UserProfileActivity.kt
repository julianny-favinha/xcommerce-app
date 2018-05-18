package com.xcommerce.mc920.xcommerce

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toolbar
import com.xcommerce.mc920.xcommerce.user.UserHelper

class UserProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
    }

    override fun onResume() {
        super.onResume()

        val usr = UserHelper.retrieveUser()
        if (usr != null){
            findViewById<TextView>(R.id.name).apply { text = usr.name }
            findViewById<TextView>(R.id.email).apply { text = usr.email }
            findViewById<TextView>(R.id.cpf).apply { text = usr.cpf }
            findViewById<TextView>(R.id.cep).apply { text = usr.cep }
            findViewById<TextView>(R.id.address).apply { text = usr.address }
        } else {
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.profile_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_edit -> {
                val intent = Intent(this, EditProfileActivity::class.java)
                startActivity(intent)
            }
            else -> return super.onOptionsItemSelected(item)
        }

        return false

    }

}
