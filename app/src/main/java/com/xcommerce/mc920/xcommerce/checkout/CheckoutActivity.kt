package com.xcommerce.mc920.xcommerce.checkout

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import com.xcommerce.mc920.xcommerce.AddressActivity
import com.xcommerce.mc920.xcommerce.InfoCreditCardActivity
import com.xcommerce.mc920.xcommerce.R
import com.xcommerce.mc920.xcommerce.cart.CartHelper
import com.xcommerce.mc920.xcommerce.model.Product
import com.xcommerce.mc920.xcommerce.model.ShipmentIn
import com.xcommerce.mc920.xcommerce.user.UserHelper
import com.xcommerce.mc920.xcommerce.utilities.UIUtils
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.content_checkout.*
import com.xcommerce.mc920.xcommerce.utilities.formatMoney
import kotlinx.android.synthetic.main.address.*
import kotlinx.android.synthetic.main.payment_method.*
import kotlinx.android.synthetic.main.shipment_options.*
import kotlinx.android.synthetic.main.summary_price_checkout.*
import kotlinx.android.synthetic.main.summary_products.*

class CheckoutActivity : AppCompatActivity() {
    companion object {
        const val RETURN_CODE = 1
    }

    var products: List<Product> = emptyList()
    private var task: ShipmentPriceFetchTask? = null

    var shipmentPrices: Map<String, Int> = emptyMap()

    var subtotal: Int = 0
    var shipment: Int = 0
    var total: Int = 0

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RETURN_CODE && resultCode == Activity.RESULT_OK) {
            checkout_delivery_address.text = data?.extras?.getString("cep") ?: ""
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        setSupportActionBar(toolbar)

        // set subtotal
        subtotal = CartHelper.retrieveCart().totalPrice
        populateSubtotal()

        // get info from user
        val user = UserHelper.retrieveUser()
        checkout_delivery_address.text = user.cep

        val cart = CartHelper.retrieveListCart()

        // calculate shipment prices
        val shipmentProducts = cart.map {cartItem ->
            (0..cartItem.quantity).map {
                cartItem.product
            }
        }.flatten()

        val shipIn = ShipmentIn(shipmentProducts, user.cep)
        task = ShipmentPriceFetchTask(this)
        task?.execute(shipIn)

        // set list of products adapter
        val products = cart.map { it.product }
        val adapter = CheckoutSummaryProductsAdapter(this, products)
        checkout_list_products.adapter = adapter
        UIUtils.setListViewHeightBasedOnItems(checkout_list_products)

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

        // set shipment price
        checkout_radio_pac_sedex.setOnCheckedChangeListener { _, optionId ->
            when (optionId) {
                R.id.checkout_pac -> {
                    shipment = shipmentPrices["PAC"]!!
                }
                R.id.checkout_sedex -> {
                    shipment = shipmentPrices["Sedex"]!!
                }
            }

            populateShipment()
            populateSubtotal()
            populateTotal()
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
        shipmentPrices = prices

        val pacString = formatMoney(prices["PAC"]!!)
        checkout_price_pac.text = pacString
        val sedexString = formatMoney(prices["Sedex"]!!)
        checkout_price_sedex.text = sedexString
        checkout_price_pac.visibility = View.VISIBLE
        checkout_price_sedex.visibility = View.VISIBLE

        shipment = if (checkout_pac.isChecked) prices["PAC"]!! else prices["Sedex"]!!

        populateShipment()
        populateSubtotal()
        populateTotal()

        Log.d("Calculate shipment", prices.toString())
    }

    private fun populateShipment() {
        val priceString = formatMoney(shipment)
        checkout_shipment.text = priceString
    }

    private fun populateSubtotal() {
        val priceString = formatMoney(subtotal)
        checkout_subtotal.text = priceString
    }

    private fun populateTotal() {
        val priceString = formatMoney(subtotal + shipment)
        checkout_total.text = priceString
    }

    private fun isTaskRunning(task: ShipmentPriceFetchTask?): Boolean {
        return task?.status != AsyncTask.Status.FINISHED
    }

}
