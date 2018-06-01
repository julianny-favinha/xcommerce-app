package com.xcommerce.mc920.xcommerce.checkout

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import com.xcommerce.mc920.xcommerce.CompletedPurchaseActivity
import com.xcommerce.mc920.xcommerce.user.AddressActivity
import com.xcommerce.mc920.xcommerce.R
import com.xcommerce.mc920.xcommerce.cart.CartHelper
import com.xcommerce.mc920.xcommerce.model.Address
import com.xcommerce.mc920.xcommerce.model.AddressFull
import com.xcommerce.mc920.xcommerce.model.Product
import com.xcommerce.mc920.xcommerce.model.ShipmentIn
import com.xcommerce.mc920.xcommerce.user.UserHelper
import com.xcommerce.mc920.xcommerce.utilities.UIUtils
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.content_checkout.*
import com.xcommerce.mc920.xcommerce.utilities.formatMoney
import kotlinx.android.synthetic.main.address.*
import kotlinx.android.synthetic.main.credit_card.*
import kotlinx.android.synthetic.main.payment_method.*
import kotlinx.android.synthetic.main.shipment_options.*
import kotlinx.android.synthetic.main.summary_price_checkout.*
import kotlinx.android.synthetic.main.summary_products.*

class CheckoutActivity : AppCompatActivity() {
    companion object {
        const val RETURN_CODE = 1
    }

    private var task: ShipmentPriceFetchTask? = null

    private var shipmentPrices: Map<String, Int> = emptyMap()

    var subtotal: Int = 0
    var shipment: Int = 0
    var total: Int = 0

    private var parcelas: MutableList<String> = mutableListOf()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RETURN_CODE && resultCode == Activity.RESULT_OK) {

            val address = data?.getSerializableExtra("address")

            if (address is AddressFull) {
                populateAddress(address)
            }
        }
    }

    private fun populateAddress(address: AddressFull) {
        val complemento = (if (address.complement != "") "Complemento " + address.complement else "")
        val logradouro = address.address.logradouro + ", " + address.number + " " + complemento
        val cityState = address.address.city + ", " + address.address.state

        address_text_view_logradouro.text = logradouro
        address_text_view_neighborhood.text = address.address.neighborhood
        address_text_view_city_state.text = cityState
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
        // TODO: populate address view

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

        if (isTaskRunning(task)) {
            showShipmentOptionsProgressBar()
        } else {
            hideShipmentOptionsProgressBar()
        }

        // set list of products adapter
        val products = cart.map { it.product }
        val adapter = CheckoutSummaryProductsAdapter(this, products)
        checkout_list_products.adapter = adapter
        UIUtils.setListViewHeightBasedOnItems(checkout_list_products)

        // set payment method
        checkout_radio_payment.setOnCheckedChangeListener{ _, optionId ->
            when (optionId) {
                R.id.checkout_radio_button_credit_card -> {
                    payment_method_credit_card.visibility = View.VISIBLE
                }
                R.id.checkout_radio_button_boleto -> {
                    payment_method_credit_card.visibility = View.GONE

                }
            }
        }

        // set list of parcelas adapter
        spinnerAdapter()

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

        // finish shopping
        checkout_button.setOnClickListener{
            val intent = Intent(this, CompletedPurchaseActivity::class.java)
            // TODO: enviar endereço, método de entrega (PAC ou Sedex), Método de pagamento (Boleto ou cartão), valor total da compra
            startActivity(intent)
        }
    }

    private fun spinnerAdapter() {
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, parcelas)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        credit_card_spinner!!.adapter = aa
        credit_card_spinner!!.setSelection(0, true)
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

        hideShipmentOptionsProgressBar()

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

        populateSpinnerParcelas()
    }

    private fun populateSpinnerParcelas() {
        parcelas.clear()
        for (i in 1..5) {
            val text = i.toString() + "x de " + formatMoney((subtotal + shipment) / i)
            parcelas.add(text)
        }
        spinnerAdapter()
    }

    private fun isTaskRunning(task: ShipmentPriceFetchTask?): Boolean {
        return task?.status != AsyncTask.Status.FINISHED
    }

    fun showShipmentOptionsProgressBar() {
        progress_bar_shipment_options.visibility = View.VISIBLE
        linear_layout_shipment_options.visibility = View.GONE
    }

    fun hideShipmentOptionsProgressBar() {
        progress_bar_shipment_options.visibility = View.GONE
        linear_layout_shipment_options.visibility = View.VISIBLE
    }
}
