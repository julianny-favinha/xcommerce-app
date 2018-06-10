package com.xcommerce.mc920.xcommerce.checkout

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import com.xcommerce.mc920.xcommerce.cart.CartItemAdapter
import com.xcommerce.mc920.xcommerce.user.AddressActivity
import com.xcommerce.mc920.xcommerce.R
import com.xcommerce.mc920.xcommerce.cart.CartHelper
import com.xcommerce.mc920.xcommerce.model.*
import com.xcommerce.mc920.xcommerce.user.UserHelper
import com.xcommerce.mc920.xcommerce.utilities.UIUtils
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.content_checkout.*
import com.xcommerce.mc920.xcommerce.utilities.formatMoney
import com.xcommerce.mc920.xcommerce.utilities.utilDays
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
    private var shipmentPrazos: Map<String, Int> = emptyMap()

    var user: User? = null

    var subtotal: Int = 0
    var shipment: Int = 0

    private var parcelas: MutableList<String> = mutableListOf()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RETURN_CODE && resultCode == Activity.RESULT_OK) {

            val address = data?.getSerializableExtra("address")

            if (address is AddressFull) {
                populateAddress(address)
            }
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
        this.user = user
        populateAddress(user.address)

        val cart = CartHelper.retrieveListCart()

        // calculate shipment prices
        val shipmentProducts = cart.map {cartItem ->
            (0..cartItem.quantity).map {
                cartItem.product
            }
        }.flatten()

        val shipIn = ShipmentIn(shipmentProducts, user.address.address.cep)
        task = ShipmentPriceFetchTask(this)
        task?.execute(shipIn)

        if (isTaskRunning(task)) {
            showShipmentOptionsProgressBar()
        } else {
            hideShipmentOptionsProgressBar()
        }

        // set list of products adapter
        val adapter = CartItemAdapter(this, cart)
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
            startCompletedPurchase(true)
        }
    }

    private fun getDelivery(): Delivery {
        if (checkout_pac.isChecked) {
            return Delivery("PAC", shipmentPrices["PAC"]!!, shipmentPrazos["PAC"]!!)
        }

        return Delivery("Sedex", shipmentPrices["Sedex"]!!, shipmentPrazos["Sedex"]!!)
    }

    private fun startCompletedPurchase(successful: Boolean) {
        val intent = Intent(this, CompletedPurchaseActivity::class.java)

        intent.putExtra("successful", successful)

        if (successful) {
            // delivery
            intent.putExtra("delivery", getDelivery())

            // TODO:  Método de pagamento (Boleto ou cartão)

            // total
            intent.putExtra("total", subtotal + shipment)
        }

        startActivity(intent)
    }

    private fun spinnerAdapter() {
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, parcelas)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        credit_card_spinner!!.adapter = aa
        credit_card_spinner!!.setSelection(0, true)
    }

    fun populateResult(prices: Map<String, Int>, prazos: Map<String, Int>) {
        shipmentPrices = prices
        shipmentPrazos = prazos

        val pricePacString = formatMoney(prices["PAC"]!!)
        checkout_price_pac.text = pricePacString
        val priceSedexString = formatMoney(prices["Sedex"]!!)
        checkout_price_sedex.text = priceSedexString

        val prazoPac = prazos["PAC"]!!
        val prazoPacString = prazoPac.toString() + utilDays(prazoPac)
        checkout_prazo_pac.text = prazoPacString
        val prazoSedex = prazos["Sedex"]!!
        val prazoSedexString = prazoSedex.toString() + utilDays(prazoSedex)
        checkout_prazo_sedex.text = prazoSedexString

        checkout_price_pac.visibility = View.VISIBLE
        checkout_price_sedex.visibility = View.VISIBLE
        checkout_prazo_pac.visibility = View.VISIBLE
        checkout_prazo_sedex.visibility = View.VISIBLE

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

    private fun populateAddress(address: AddressFull) {
        val complemento = (if (address.complement != "") "Complemento " + address.complement else "")
        val logradouro = address.address.logradouro + ", " + address.number + " " + complemento
        val cityState = address.address.city + ", " + address.address.state

        address_text_view_logradouro.text = logradouro
        address_text_view_neighborhood.text = address.address.neighborhood
        address_text_view_city_state.text = cityState
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

    override fun onResume() {
        super.onResume()

        if (!UserHelper.isLoggedIn()){
            finish()
        }
    }
}
