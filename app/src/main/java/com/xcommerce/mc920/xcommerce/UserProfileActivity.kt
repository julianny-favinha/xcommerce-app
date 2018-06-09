package com.xcommerce.mc920.xcommerce

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.xcommerce.mc920.xcommerce.user.UserHelper
import kotlinx.android.synthetic.main.activity_user_profile.*

class UserProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
    }

    override fun onResume() {
        super.onResume()

        if (UserHelper.isLoggedIn()){
            val usr = UserHelper.retrieveUser()
            name.setText(usr.name, TextView.BufferType.EDITABLE)
            email.setText(usr.email, TextView.BufferType.EDITABLE)
            cpf.setText(usr.cpf, TextView.BufferType.EDITABLE)
            cep.setText(usr.address.address.cep, TextView.BufferType.EDITABLE)
            val complemento = (if (usr.address.complement != "") "Complemento " + usr.address.complement else "")
            val logradouro = usr.address.address.logradouro + ", " + usr.address.number + " " + complemento
            profile_logradouro.setText(logradouro, TextView.BufferType.EDITABLE)
            profile_neighborhood.setText(usr.address.address.neighborhood, TextView.BufferType.EDITABLE)
            val cityStateString = usr.address.address.city + " " + usr.address.address.state
            profile_city_state.setText(cityStateString, TextView.BufferType.EDITABLE)
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
