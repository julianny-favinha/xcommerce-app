package com.xcommerce.mc920.xcommerce

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import com.xcommerce.mc920.xcommerce.cart.CartHelper
import com.xcommerce.mc920.xcommerce.checkout.ShipmentPriceFetchTask
import com.xcommerce.mc920.xcommerce.model.ShipmentIn
import com.xcommerce.mc920.xcommerce.user.UserHelper
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.content_checkout.*

class CheckoutActivity : AppCompatActivity() {
    companion object {
        const val RETURN_CODE = 1
    }

    var prices: Map<String, Int> = emptyMap()
    private var task: ShipmentPriceFetchTask? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RETURN_CODE && resultCode == Activity.RESULT_OK) {
            checkout_delivery_address.text = data?.extras?.getString("cep") ?: ""
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        setSupportActionBar(toolbar)

        val user = UserHelper.retrieveUser() ?: throw IllegalStateException ("Usuario deveria estar logado para chegar na tela de Checkout")
        checkout_delivery_address.text = user.cep

        // calculate shipment prices
        val cartItems = CartHelper.retrieveListCart()
        val items = cartItems.map {cartItem ->
            (0..cartItem.quantity).map {
                cartItem.product
            }
        }.flatten()
        val shipIn = ShipmentIn(items, user.cep)

        task = ShipmentPriceFetchTask(this)
        task?.execute(shipIn)

        // set payment method
        checkout_radio_payment.setOnCheckedChangeListener{ _, optionId ->
            val button = findViewById<Button>(R.id.checkout_button_add_info_card)

            when (optionId) {
                R.id.checkout_radio_button_credit_card -> {
                    button.isEnabled = true
                }

                R.id.checkout_radio_button_boleto -> {
                    button.isEnabled = false
                }
            }
        }

        // add new address
        checkout_button_add_address.setOnClickListener {
            val intent = Intent(this, AddressActivity::class.java)
            startActivityForResult(intent, RETURN_CODE)
        }

        // add credit card info
        checkout_button_add_info_card.setOnClickListener {
            val intent = Intent(this, InfoCreditCardActivity::class.java)
            startActivity(intent)
        }

        // finish shopping
        checkout_button.setOnClickListener{

        }
    }

//    fun showProgressBar(){
//        progressbar.visibility = View.VISIBLE
//        recycler_view_categories.visibility = View.GONE
//    }
//
//    fun hideProgressBar(){
//        progressbar.visibility = View.GONE
//        recycler_view_categories.visibility = View.VISIBLE
//    }

    fun populateResult(prices: Map<String, Int>) {
        checkout_price_pac.text = prices["PAC"].toString()
        checkout_price_sedex.text = prices["Sedex"].toString()
        checkout_price_pac.visibility = View.VISIBLE
        checkout_price_sedex.visibility = View.VISIBLE

        Log.d("Calculate shipment", prices.toString())
    }

    private fun isTaskRunning(task: ShipmentPriceFetchTask?): Boolean {
        return task?.status != AsyncTask.Status.FINISHED
    }

}
