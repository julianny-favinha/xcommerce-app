package com.xcommerce.mc920.xcommerce.cart

import android.os.AsyncTask
import com.xcommerce.mc920.xcommerce.model.CartItem
import com.xcommerce.mc920.xcommerce.model.ProductAPI
import com.xcommerce.mc920.xcommerce.utilities.ClientHttpUtil

class ReserveTask internal constructor(val cartItem: CartItem) : AsyncTask<Void, Void, Boolean>() {

    override fun doInBackground(vararg p0: Void?): Boolean {
        return ClientHttpUtil.postRequest(ProductAPI.Reserve.PATH, cartItem) ?: false
    }

    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)

        if (result == false){
            CartHelper.retrieveCart().sub(cartItem.product, cartItem.quantity, null)
        }
    }
}