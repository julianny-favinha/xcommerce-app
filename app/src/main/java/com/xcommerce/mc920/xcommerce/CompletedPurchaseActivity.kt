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
import com.xcommerce.mc920.xcommerce.utilities.UIUtils
import com.xcommerce.mc920.xcommerce.utilities.formatMoney
import kotlinx.android.synthetic.main.content_completed_purchase.*

class CompletedPurchaseActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_purchase)

        // TODO: receber intent

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

            // --- entrega ---
            val (shipmentType, prazo) = getShipmentInfo() // SEDEX ou PAC
            ship_type.text = shipmentType
            ship_date.text = prazo

            val (cep, address) = getAddressInfo()
            ship_address.text = address
            ship_cep.text = cep

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

//    private fun getSubtotal(cart: List<CartItem>): Int {
//        var total = 0
//
//        for(i in cart.indices) {
//            total += (cart[i].quantity*cart[i].product.price)
//        }
//
//        return total
//    }
//
//    private fun getShipmentPrice(): Int {
//        return 1499
//    }

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

    private fun getShipmentInfo(): Pair<String, String> {
        return Pair("SEDEX", "01/07/2018")
    }

    private fun getAddressInfo(): Pair<String, String> {
        return Pair("13.214-717", "Rua Alceu de Toledo Pontes, 510")
    }

    private fun copyToClipboard(activity: Activity, text: String) {
        val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("copy", text)
        clipboard.primaryClip = clip
    }

//    inner class CompletedCartViewAdapter(context: Context, cartItems: List<CartItem>): ArrayAdapter<CartItem>(context, 0, cartItems) {
//        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//            val cartItem= getItem(position)
//
//            val product = cartItem.product
//            val quantity = cartItem.quantity
//
//            val newView = convertView
//                    ?: LayoutInflater.from(context).inflate(R.layout.adapter_cart_item, parent, false)
//
//            if (quantity > 1) {
//                newView.note_item_quantidade.text = "unidades"
//            } else {
//                newView.note_item_quantidade.text = "unidade"
//            }
//
//            newView.note_item_name.text = product.name
//            newView.note_item_val.text = quantity.toString()
//            newView.note_item_price.text = formatMoney(product.price)
//            product.imageUrl?.let { DownloadImageTask(newView.note_item_image).execute(product.imageUrl) }
//                    ?: newView.note_item_image.setImageResource(R.drawable.image_noimage)
//
//            return newView
//        }
//    }
}
