package com.xcommerce.mc920.xcommerce

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.xcommerce.mc920.xcommerce.cart.CartHelper
import com.xcommerce.mc920.xcommerce.model.AddressFull
import com.xcommerce.mc920.xcommerce.model.Delivery
import com.xcommerce.mc920.xcommerce.utilities.UIUtils
import com.xcommerce.mc920.xcommerce.utilities.formatMoney
import com.xcommerce.mc920.xcommerce.utilities.utilDays
import kotlinx.android.synthetic.main.content_address.*
import kotlinx.android.synthetic.main.content_completed_purchase.*

class CompletedPurchaseActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_purchase)

        // recebe intent de delivery
        val delivery = intent.getSerializableExtra("delivery")
        if (delivery is Delivery) {
            ship_type.text = delivery.type
            val prazoString = delivery.prazo.toString() + utilDays(delivery.prazo)
            ship_date.text = prazoString
        }

        // recebe intent de endereco
        val address = intent.getSerializableExtra("address")
        if (address is AddressFull) {
            populateAddress(address)
        }

        val cartItems = CartHelper.retrieveListCart() // itens da compra

        // dados mockados
        val total = 0

        val purchaseSuccessful: Boolean = isSuccessful()

        if (purchaseSuccessful) {
            completed_scroll_view.visibility = View.VISIBLE
            purchase_failed.visibility = View.GONE

            // método de pagamento
            val paymentType = getPaymentType() // Boleto ou Cartao

            if (paymentType == "Boleto") {
                val (boletoNumber, vencimento) = getBoletoInfo(total)
                boleto_vencimento_date.text = vencimento
                boleto_number.text = boletoNumber

                card_view_payment_boleto.visibility = View.VISIBLE
                card_view_payment_card.visibility = View.GONE

                button_copy.setOnClickListener{
                    copyToClipboard(this, boletoNumber)

                    val duration = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(applicationContext, R.string.copy_boleto_number, duration)
                    toast.show()
                }

            } else { // Pagamento por Cartão
                val cardEnd = getCardEnd()
                card_end.text = cardEnd

                val (parcelaNum, parcelaValue) = getParcelas()
                val parcelas = parcelaNum + "x " + formatMoney(parcelaValue.toInt())
                card_parcelas.text = parcelas

                card_view_payment_card.visibility = View.VISIBLE
                card_view_payment_boleto.visibility = View.GONE
            }

            // --- resumo da compra ---
            val adapter = CartItemAdapter(this, cartItems)
            list_view_completed.adapter = adapter
            UIUtils.setListViewHeightBasedOnItems(list_view_completed)

            price_total.text = formatMoney(total)

        } else {
            completed_scroll_view.visibility = View.GONE
            purchase_failed.visibility = View.VISIBLE
        }
    }

    // TODO: definir o que é isSuccessful"
    private fun isSuccessful(): Boolean {
        return true
    }

    private fun populateAddress(address: AddressFull) {
        val complemento = (if (address.complement != "") "Complemento " + address.complement else "")
        val logradouro = address.address.logradouro + ", " + address.number + " " + complemento
        val cityState = address.address.city + ", " + address.address.state

        ship_logradouro.text = logradouro
        ship_neighborhood.text = address.address.neighborhood
        ship_city_state.text = cityState
    }

    private fun getPaymentType(): String {
        return "Boleto"
        //return "Cartão de crédito"
    }

    private fun getBoletoInfo(price: Int): Pair<String, String> {
        return Pair("34191790010104351004791020150008775410026000", "01/07/2018")
    }

    private fun getCardEnd(): String {
        return "3419"
    }

    private fun getParcelas(): Pair<String, String> {
        return Pair("3", "1499")
    }

    private fun copyToClipboard(activity: Activity, text: String) {
        val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("copy", text)
        clipboard.primaryClip = clip
    }
}
