package com.xcommerce.mc920.xcommerce.checkout

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
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
    private var checkoutTask: CheckoutFetchTask? = null

    private var shipmentPrices: Map<String, Int> = emptyMap()
    private var shipmentPrazos: Map<String, Int> = emptyMap()

    var user: User? = null
    var address: AddressFull? = null

    var subtotal: Int = 0
    var shipment: Int = 0

    private var parcelas: MutableList<String> = mutableListOf()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RETURN_CODE && resultCode == Activity.RESULT_OK) {

            val address = data?.getSerializableExtra("address")

            if (address is AddressFull) {

                val shipmentProducts = CartHelper.retrieveListCart().map {cartItem ->
                    (0..cartItem.quantity).map {
                        cartItem.product
                    }
                }.flatten()

                populateAddress(address)
                task?.let { task = ShipmentPriceFetchTask(this) }
                task?.execute(ShipmentIn(shipmentProducts, address.address.cep))
                this.address = address

                if (isTaskRunning(task)) {
                    showShipmentOptionsProgressBar()
                } else {
                    hideShipmentOptionsProgressBar()
                }
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

        val shipIn = ShipmentIn(shipmentProducts, address?.address?.cep ?: user.address.address.cep)
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
                R.id.checkout_radio_button_pac -> {
                    shipment = shipmentPrices["PAC"]!!
                }
                R.id.checkout_radio_button_sedex -> {
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
        checkout_button.setOnClickListener {

            if (PaymentType.getType(getPaymentMethod()) == PaymentType.CREDIT_CARD && credit_card_number.text.toString().length != 16) {
                credit_card_number.error = "Cartão Inválido"
                credit_card_number.requestFocus()
            } else if (PaymentType.getType(getPaymentMethod()) == PaymentType.CREDIT_CARD && TextUtils.isEmpty(credit_card_name.text.toString())) {
                credit_card_name.error = "Campo Obrigatório"
                credit_card_name.requestFocus()
            } else if (PaymentType.getType(getPaymentMethod()) == PaymentType.CREDIT_CARD && credit_card_code.text.toString().length != 3) {
                credit_card_code.error = "Número Inválido"
                credit_card_code.requestFocus()
            } else if (PaymentType.getType(getPaymentMethod()) == PaymentType.CREDIT_CARD && credit_card_month.text.toString().length != 2) {
                credit_card_month.error = "Inválido"
                credit_card_month.requestFocus()
            } else if (PaymentType.getType(getPaymentMethod()) == PaymentType.CREDIT_CARD && credit_card_year.text.toString().length != 4) {
                credit_card_year.error = "Inválido"
                credit_card_year.requestFocus()
            } else {
                    checkout_button.isEnabled = false
                    progressBarCompletedPurchase.visibility = View.VISIBLE
                    progressBarCompletedPurchase.bringToFront()

                    val creditCardInfo = if (PaymentType.getType(getPaymentMethod()) == PaymentType.CREDIT_CARD) {
                        CreditCardInfo(credit_card_name.text.toString(), credit_card_month.text.toString().toLong(), credit_card_year.text.toString().toLong(), credit_card_code.text.toString(), credit_card_number.text.toString())
                    } else {
                        null
                    }

                    val paymentInfo = PaymentInfo(PaymentType.getType(getPaymentMethod()), creditCardInfo, getInstallments())

                    val shipmentInfo = ShipmentInfo(address?.address?.cep ?: UserHelper.retrieveUser().address.address.cep, ShipmentType.getType(getShipmentMethod()))

                    val cart = CartHelper.retrieveListCart()
                    val cartIn = CartIn(cart)
                    val checkoutIn = CheckoutIn(cartIn, paymentInfo, shipmentInfo)

                    checkoutTask = CheckoutFetchTask(this)
                    checkoutTask!!.execute(checkoutIn)
                }
            }
        }

    fun hideProgressBar() {
        progressBarCompletedPurchase.visibility = View.GONE
    }

    private fun getDelivery(): Delivery {
        if (checkout_radio_button_pac.isChecked) {
            return Delivery("PAC", shipmentPrices["PAC"]!!, shipmentPrazos["PAC"]!!)
        }

        return Delivery("Sedex", shipmentPrices["Sedex"]!!, shipmentPrazos["Sedex"]!!)
    }

    private fun getPaymentMethod(): String {
        if (checkout_radio_button_credit_card.isChecked) {
            return checkout_radio_button_credit_card.text.toString()
        }

        return checkout_radio_button_boleto.text.toString()
    }

    private fun getShipmentMethod(): String {
        if (checkout_radio_button_pac.isChecked) {
            return checkout_radio_button_pac.text.toString()
        }

        return checkout_radio_button_sedex.text.toString()
    }

    private fun getInstallments(): Long {
        return (credit_card_spinner.selectedItemPosition + 1).toLong()
    }

    fun startCompletedPurchase(successful: Boolean, checkoutOut: CheckoutOut = CheckoutOut(CheckoutStatus.FAILED)) {
        val intent = Intent(this, CompletedPurchaseActivity::class.java)

        intent.putExtra("successful", successful)

        if (successful) {
            // address
            intent.putExtra("address", address ?: UserHelper.retrieveUser().address)

            // delivery
            intent.putExtra("delivery", getDelivery())

            // checkout out
            intent.putExtra("paymentMethod", getPaymentMethod())

            // installments
            if (getPaymentMethod() == "Cartão de crédito") {
                intent.putExtra("installments", getInstallments())
                intent.putExtra("creditCardNumber", credit_card_code.text.toString())
            }

            intent.putExtra("checkoutOut", checkoutOut)

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

        shipment = if (checkout_radio_button_pac.isChecked) prices["PAC"]!! else prices["Sedex"]!!

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
