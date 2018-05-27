package com.xcommerce.mc920.xcommerce.user

import android.os.AsyncTask
import android.provider.Telephony
import com.xcommerce.mc920.xcommerce.model.Address
import com.xcommerce.mc920.xcommerce.model.AddressAPI
import com.xcommerce.mc920.xcommerce.utilities.ClientHttpUtil

class AddressFetchTask(private var container: AddressActivity?): AsyncTask<String, Void, Address>() {
    override fun doInBackground(vararg p0: String?): Address? {
        try {
            return ClientHttpUtil.getRequest(AddressAPI.CheckCep.of(p0.first()!!))
                    ?: null
        } catch (e: Exception) {
            return null
        }
    }

    override fun onPreExecute() {
        super.onPreExecute()
        container?.showProgressBar()
    }

    override fun onPostExecute(result: Address) {
        super.onPostExecute(result)
        if (result != null) container?.populateResult(result)
        container?.hideProgressBar()
        this.container = null
    }
}