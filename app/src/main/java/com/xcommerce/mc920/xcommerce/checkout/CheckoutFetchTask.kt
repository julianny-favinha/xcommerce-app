package com.xcommerce.mc920.xcommerce.checkout

import android.os.AsyncTask
import android.widget.Toast
import com.xcommerce.mc920.xcommerce.model.CartAPI
import com.xcommerce.mc920.xcommerce.model.CheckoutIn
import com.xcommerce.mc920.xcommerce.model.CheckoutOut
import com.xcommerce.mc920.xcommerce.model.CheckoutStatus
import com.xcommerce.mc920.xcommerce.utilities.ClientHttpUtil

class CheckoutFetchTask(private var container: CheckoutActivity?): AsyncTask<CheckoutIn, Void, CheckoutOut>() {
    override fun doInBackground(vararg params: CheckoutIn?): CheckoutOut? {
        try {
            return ClientHttpUtil.postRequest(CartAPI.Checkout.PATH, params.first(), true)
        } catch (e: Exception) {
            return null
        }
    }

    override fun onPostExecute(result: CheckoutOut?) {
        super.onPostExecute(result)

        when (result?.status) {
            CheckoutStatus.OK, CheckoutStatus.PENDING -> container!!.startCompletedPurchase(true, result)
            CheckoutStatus.BAD_INPUT -> Toast.makeText(container, "Algo deu errado.", Toast.LENGTH_SHORT).show()
            CheckoutStatus.FAILED -> container!!.startCompletedPurchase(false)
        }
    }

}