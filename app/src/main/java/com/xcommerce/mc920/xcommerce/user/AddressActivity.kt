package com.xcommerce.mc920.xcommerce.user

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import com.xcommerce.mc920.xcommerce.R
import com.xcommerce.mc920.xcommerce.checkout.CheckoutActivity
import com.xcommerce.mc920.xcommerce.model.Address
import com.xcommerce.mc920.xcommerce.model.AddressFull
import com.xcommerce.mc920.xcommerce.model.User

import kotlinx.android.synthetic.main.activity_address.*
import kotlinx.android.synthetic.main.content_address.*

class AddressActivity : AppCompatActivity() {
    private var task: AddressFetchTask? = null
    private var address: Address? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)
        setSupportActionBar(toolbar)

        // back button
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        // validate CEP
        address_button_validate.setOnClickListener {
            task = AddressFetchTask(this)
            task?.execute(address_edit_text_cep.text.toString())

            if(isTaskRunning(task)){
                showProgressBar()
            } else {
                hideProgressBar()
            }
        }

        // save address
        address_button_save.setOnClickListener {
            val number = address_edit_text_number.text.toString()

            if (TextUtils.isEmpty(number)) {
                address_edit_text_number.error = "Número obrigatório"
                address_edit_text_number.requestFocus()
            }
            else {
                val newAddress = AddressFull(address!!, number.toInt(), address_edit_text_complement.text.toString())

                if (address_checkbox_default.isChecked) {
                    val user = UserHelper.retrieveUser()
                    val newUser = User(user.name, user.email, user.password, user.birthDate, user.cpf, newAddress, user.gender, user.telephone)
                    UserHelper.updateUser(newUser)

                    // TODO: task para update do user
                }

                val returnIntent = Intent()
                returnIntent.putExtra("address", newAddress!!)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
        }
    }

    fun populateResult(address: Address) {
        this.address = address
        address_text_view_logradouro.text = address.logradouro
        address_text_view_neighborhood.text = address.neighborhood
        address_text_view_city_state.text = address.city + ", " + address.state
    }

    fun showProgressBar(){
        progressbar.visibility = View.VISIBLE
        address_info.visibility = View.GONE
    }

    fun hideProgressBar(){
        progressbar.visibility = View.GONE
        address_info.visibility = View.VISIBLE
    }

    private fun isTaskRunning(task: AddressFetchTask?): Boolean {
        return task?.status != AsyncTask.Status.FINISHED
    }

}
