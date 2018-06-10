package com.xcommerce.mc920.xcommerce.myorders

import android.os.AsyncTask
import com.xcommerce.mc920.xcommerce.model.Orders
import com.xcommerce.mc920.xcommerce.model.OrderAPI
import com.xcommerce.mc920.xcommerce.utilities.ClientHttpUtil

class MyOrdersFetchTask(private var container: MyOrdersActivity?): AsyncTask<String, Void, Orders>() {


    override fun doInBackground(vararg p0: String?): Orders {
        try {
            return ClientHttpUtil.getRequest(OrderAPI.Orders.PATH)
                    ?: Orders(emptyList())
        } catch (e: Exception) {
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