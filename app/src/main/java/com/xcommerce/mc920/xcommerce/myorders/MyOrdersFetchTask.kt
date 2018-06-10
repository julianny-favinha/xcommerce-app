package com.xcommerce.mc920.xcommerce.myorders

import android.os.AsyncTask
import android.util.Log
import com.xcommerce.mc920.xcommerce.model.Order
import com.xcommerce.mc920.xcommerce.model.Orders
import com.xcommerce.mc920.xcommerce.model.OrderAPI
import com.xcommerce.mc920.xcommerce.model.Product
import com.xcommerce.mc920.xcommerce.utilities.ClientHttpUtil

class MyOrdersFetchTask(private var container: MyOrdersActivity?): AsyncTask<String, Void, Orders>() {


    override fun doInBackground(vararg p0: String?): Orders {
        val prod = Product(12, "Nome", "Marca", 1345, 22, 56, 585, 585, "Categ", "descrição", "imagem")
        val order1 = Order(listOf(prod), 1234567, 45544, 343, 2, "54585458545855", "01/01/1979")
        val order2 = Order(listOf(prod), 12345678, 45545, 343, 2, "54585458545855", "01/01/1979")

        return Orders(listOf(order1, order2))

//        try {
//            return ClientHttpUtil.getRequest(OrderAPI.Orders.PATH)
//                    ?: Orders(emptyList())
//        } catch (e: Exception) {
//            return Orders(emptyList())
//        }
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