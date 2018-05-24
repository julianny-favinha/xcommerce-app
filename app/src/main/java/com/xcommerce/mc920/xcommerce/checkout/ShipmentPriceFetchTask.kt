package com.xcommerce.mc920.xcommerce.checkout

import android.os.AsyncTask
import com.xcommerce.mc920.xcommerce.CheckoutActivity
import com.xcommerce.mc920.xcommerce.model.LogisticAPI
import com.xcommerce.mc920.xcommerce.model.ShipmentIn
import com.xcommerce.mc920.xcommerce.model.ShipmentOut
import com.xcommerce.mc920.xcommerce.utilities.ClientHttpUtil

class ShipmentPriceFetchTask(private var container: CheckoutActivity?): AsyncTask<ShipmentIn, Void, ShipmentOut>() {

    override fun doInBackground(vararg p0: ShipmentIn): ShipmentOut {
        try {
            return ClientHttpUtil.postRequest(LogisticAPI.ShipmentPrice.PATH, p0)
                    ?: ShipmentOut(emptyMap())
        } catch (e: Exception) {
            return ShipmentOut(emptyMap())
        }
    }

    override fun onPreExecute() {
        super.onPreExecute()
        //container?.showProgressBar()
    }

    override fun onPostExecute(result: ShipmentOut?) {
        super.onPostExecute(result)
        if (result != null) container?.populateResult(result.prices)
        //container?.hideProgressBar()
        this.container = null
    }
}