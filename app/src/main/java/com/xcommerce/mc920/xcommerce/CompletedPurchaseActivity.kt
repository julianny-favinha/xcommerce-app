package com.xcommerce.mc920.xcommerce

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.xcommerce.mc920.xcommerce.model.CartItem
import com.xcommerce.mc920.xcommerce.model.Product
import com.xcommerce.mc920.xcommerce.utilities.DownloadImageTask
import com.xcommerce.mc920.xcommerce.utilities.formatMoney
import kotlinx.android.synthetic.main.adapter_cart_view_completed.view.*
import kotlinx.android.synthetic.main.content_completed_purchase.*


class CompletedPurchaseActivity : AppCompatActivity() {

    private fun isSuccessful() : Boolean{
        return true
        //return false
    }
    private fun getSubtotal(cart : List<CartItem>) : Int{
        var total = 0

        for(i in cart.indices){
            total += (cart[i].quantity*cart[i].product.price)
        }

        return total
    }

    private fun getShipmentPrice() : Int{
        return 1499
    }

    private fun getPaymentType() : String{
        return "Boleto"
        // return "Cartão"
    }

    private fun getBoletoInfo(price: Int) : Pair<String, String>{
        return Pair("34191790010104351004791020150008775410026000", "01/07/2018")
    }

    private fun getCardEnd() : String{
        return "3419"
    }

    private fun getParcelas() : Pair<String, String>{
        return Pair("3", "1499")
    }

    private fun getShipmentInfo() : Pair<String, String>{
        return Pair("SEDEX", "01/07/2018")
    }

    private fun getAddressInfo() : Pair<String, String>{
        return Pair("13.214-717", "Rua Alceu de Toledo Pontes, 510")
    }

    private fun getCart() : List<CartItem>{
        return listOf(
                CartItem(Product(
                        id=16,
                        name="Sofá1",
                        brand="Marca de um sofá",
                        price=100,
                        category = "Eletrodomésticos",
                        description = "Sofá marrom de 2 lugares que abre para deitar. Ou seja, trata-se de um sofá-cama de 2 (dois) lugares.",
                        imageUrl="http://www.ic.unicamp.br/~helio/imagens_inclinadas_png/neg_4.png",
                        weight = 1000,
                        length = 10,
                        width = 1000,
                        height = 100), 10),
                CartItem(Product(
                        id=17,
                        name="Sofá1",
                        brand="Marca de um sofá",
                        price=100,
                        category = "Eletrodomésticos",
                        description = "Sofá marrom de 2 lugares que abre para deitar. Ou seja, trata-se de um sofá-cama de 2 (dois) lugares.",
                        imageUrl="http://www.ic.unicamp.br/~helio/imagens_inclinadas_png/neg_4.png",
                        weight = 1000,
                        length = 10,
                        width = 1000,
                        height = 100), 10),
                CartItem(Product(
                        id=18,
                        name="Sofá1",
                        brand="Marca de um sofá",
                        price=100,
                        category = "Eletrodomésticos",
                        description = "Sofá marrom de 2 lugares que abre para deitar. Ou seja, trata-se de um sofá-cama de 2 (dois) lugares.",
                        imageUrl="http://www.ic.unicamp.br/~helio/imagens_inclinadas_png/neg_4.png",
                        weight = 1000,
                        length = 10,
                        width = 1000,
                        height = 100), 10),
                CartItem(Product(
                        id=19,
                        name="Sofá1",
                        brand="Marca de um sofá",
                        price=100,
                        category = "Eletrodomésticos",
                        description = "Sofá marrom de 2 lugares que abre para deitar. Ou seja, trata-se de um sofá-cama de 2 (dois) lugares.",
                        imageUrl="http://www.ic.unicamp.br/~helio/imagens_inclinadas_png/neg_4.png",
                        weight = 1000,
                        length = 10,
                        width = 1000,
                        height = 100), 10),
                CartItem(Product(
                        id=20,
                        name="Sofá1",
                        brand="Marca de um sofá",
                        price=100,
                        category = "Eletrodomésticos",
                        description = "Sofá marrom de 2 lugares que abre para deitar. Ou seja, trata-se de um sofá-cama de 2 (dois) lugares.",
                        imageUrl="http://www.ic.unicamp.br/~helio/imagens_inclinadas_png/neg_4.png",
                        weight = 1000,
                        length = 10,
                        width = 1000,
                        height = 100), 10))

    }

    private fun copyToClipboard(activity: Activity, text: String) {
        val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("copy", text)
        clipboard.primaryClip = clip
    }

    private fun setListViewHeightBasedOnItems(listView: ListView): Boolean {

        val listAdapter = listView.adapter
        if (listAdapter != null) {

            val numberOfItems = listAdapter.count

            // Get total height of all items.
            var totalItemsHeight = 0
            for (itemPos in 0 until numberOfItems) {
                val item = listAdapter.getView(itemPos, null, listView)
                item.measure(0, 0)
                totalItemsHeight += item.measuredHeight
            }

            // Get total height of all item dividers.
            val totalDividersHeight = listView.dividerHeight * (numberOfItems - 1)

            // Set list height.
            val params = listView.layoutParams
            params.height = totalItemsHeight + totalDividersHeight
            listView.layoutParams = params
            listView.requestLayout()

            return true

        } else {
            return false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_purchase)

        val cartItems = getCart() // itens da compra

        // dados mockados
        val subtotal = getSubtotal(cartItems)
        val shipmentPrice = getShipmentPrice() // preço de frete
        val total = subtotal + shipmentPrice

        val purchaseSucessful: Boolean = isSuccessful()

        if(purchaseSucessful) {
            completed_scroll_view.visibility = View.VISIBLE
            purchase_failed.visibility = View.GONE

            // método de pagamento
            val paymentType = getPaymentType() // Boleto ou Cartao

            if (paymentType == "Boleto") {
                val (boletoNumber, vencimento) = getBoletoInfo(total)
                findViewById<TextView>(R.id.boleto_vencimento_date).apply { text = vencimento }
                findViewById<TextView>(R.id.boleto_number).apply { text = boletoNumber }

                card_view_payment_boleto.visibility = View.VISIBLE
                card_view_payment_card.visibility = View.GONE

                button_copy.setOnClickListener{
                    copyToClipboard(this, boletoNumber)

                    val duration = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(applicationContext, "Número de Boleto copiado.", duration)
                    toast.show()
                }

            } else { // Pagamento por Cartão
                val cardEnd = getCardEnd()
                findViewById<TextView>(R.id.card_end).apply { text = cardEnd }

                val (parcelaNum, parcelaValue) = getParcelas()
                val parcelas = parcelaNum + "x " + formatMoney(parcelaValue.toInt())
                findViewById<TextView>(R.id.card_parcelas).apply { text = parcelas }

                card_view_payment_card.visibility = View.VISIBLE
                card_view_payment_boleto.visibility = View.GONE
            }

            // --- entrega ---
            val (shipmentType, prazo) = getShipmentInfo() // SEDEX ou PAC
            findViewById<TextView>(R.id.ship_type).apply { text = shipmentType }
            findViewById<TextView>(R.id.ship_date).apply { text = prazo }

            val (cep, address) = getAddressInfo()
            findViewById<TextView>(R.id.ship_address).apply { text = address }
            findViewById<TextView>(R.id.ship_cep).apply { text = cep }

            // --- resumo da compra ---
            val adapter = CompletedCartViewAdapter(this, cartItems)
            list_view_completed.adapter = adapter
            setListViewHeightBasedOnItems(list_view_completed)

            findViewById<TextView>(R.id.price_ship).apply { text = formatMoney(shipmentPrice) }
            findViewById<TextView>(R.id.price_cart).apply { text = formatMoney(subtotal) }
            findViewById<TextView>(R.id.price_total).apply { text = formatMoney(total) }

        } else {
            completed_scroll_view.visibility = View.GONE
            purchase_failed.visibility = View.VISIBLE
        }
    }

    inner class CompletedCartViewAdapter(context: Context, cartItems: List<CartItem>) : ArrayAdapter<CartItem>(context, 0, cartItems) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val cartItem= getItem(position)

            val product = cartItem.product
            val quantity = cartItem.quantity

            val newView = convertView
                    ?: LayoutInflater.from(context).inflate(R.layout.adapter_cart_view_completed, parent, false)

            if(quantity > 1){
                newView.note_item_quantidade.text = "unidades"
            } else {
                newView.note_item_quantidade.text = "unidade"
            }

            newView.note_item_name.text = product.name
            newView.note_item_val.text = quantity.toString()
            newView.note_item_price.text = formatMoney(product.price)
            product.imageUrl?.let { DownloadImageTask(newView.note_item_image).execute(product.imageUrl) }
                    ?: newView.note_item_image.setImageResource(R.drawable.image_noimage)

            return newView
        }
    }


}
