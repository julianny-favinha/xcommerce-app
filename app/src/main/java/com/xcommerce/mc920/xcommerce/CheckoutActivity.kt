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
import com.xcommerce.mc920.xcommerce.model.Product
import com.xcommerce.mc920.xcommerce.model.ShipmentIn
import com.xcommerce.mc920.xcommerce.user.UserHelper
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.content_checkout.*
import android.widget.ArrayAdapter
import com.xcommerce.mc920.xcommerce.checkout.CheckoutSummaryProductsAdapter
import com.xcommerce.mc920.xcommerce.model.LightProduct
import android.support.v7.widget.CardView




class CheckoutActivity : AppCompatActivity() {
    companion object {
        const val RETURN_CODE = 1
    }

    var prices: Map<String, Int> = emptyMap()
    var products: List<Product> = emptyList()
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

        // get info from user
        val user = UserHelper.retrieveUser() ?: throw IllegalStateException ("Usuario deveria estar logado para chegar na tela de Checkout")
        checkout_delivery_address.text = user.cep

        // calculate shipment prices
        val cartItems = CartHelper.retrieveListCart()
        products = cartItems.map {cartItem ->
            (0..cartItem.quantity).map {
                cartItem.product
            }
        }.flatten()
        val shipIn = ShipmentIn(products, user.cep)
        task = ShipmentPriceFetchTask(this)
        task?.execute(shipIn)

        // set list of products adapter
        val lightProducts = products.map {
            LightProduct(it.id, it.name, it.price, it.imageUrl)
        }
        val adapter = CheckoutSummaryProductsAdapter(this, lightProducts)
        checkout_list_products.adapter = adapter

        checkout_list_products.isExpanded = true

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
