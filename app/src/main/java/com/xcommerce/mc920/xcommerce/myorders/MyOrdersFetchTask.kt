package com.xcommerce.mc920.xcommerce.myorders

import android.os.AsyncTask
import android.util.Log
import com.xcommerce.mc920.xcommerce.model.*
import com.xcommerce.mc920.xcommerce.utilities.ClientHttpUtil

class MyOrdersFetchTask(private var container: MyOrdersActivity?): AsyncTask<String, Void, Orders>() {


    override fun doInBackground(vararg p0: String?): Orders {
//        val prod = Product(12, "Nome", "Marca", 1345, 22, 56, 585, 585, 232, "Categ", "descrição", "imagem")
//        val prod2 = Product(124, "Produto 2", "Marca 2", 5878, 22, 56, 585, 585, 232, "Categ", "descrição", "imagem")
//
//        val order1 = Order(mapOf(prod to 2L, prod2 to 1L), 1234567, 45544, 343, 2, ShipmentType.SEDEX, ShipmentStatus.PREPARING_FOR_SHIPMENT, PaymentStatus.PENDING, PaymentType.BOLETO,  barcode = "54585458545855", createdAt = "01/01/1979")
//        val order2 = Order(mapOf(prod to 5L), 12345678, 45545, 343, 2, ShipmentType.PAC, ShipmentStatus.DELIVERED, PaymentStatus.OK, PaymentType.CREDIT_CARD,"", "01/01/1979")
//
//        return Orders(listOf(order1, order2))

        try {

            val orders = ClientHttpUtil.getRequest(OrderAPI.Orders.PATH, true)
                    ?: Orders(emptyList())
            Log.d("getOrders", orders.toString())
            return orders
        } catch (e: Exception) {
            Log.d("getOrders", e.toString())
            return Orders(emptyList())
        }
    }

    override fun onPreExecute() {
        super.onPreExecute()
        container?.showProgressBar()
    }

    override fun onPostExecute(result: Orders?) {
        super.onPostExecute(result)
        if (result != null) container?.populateResult(result.orders.distinct())
        container?.hideProgressBar()
        this.container = null
    }
}