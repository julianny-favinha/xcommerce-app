package com.xcommerce.mc920.xcommerce.cart

import android.os.AsyncTask
import com.xcommerce.mc920.xcommerce.model.CartItem
import com.xcommerce.mc920.xcommerce.model.ProductAPI
import com.xcommerce.mc920.xcommerce.utilities.ClientHttpUtil


class ReleaseTask internal constructor(val cartItem: CartItem) : AsyncTask<Void, Void, Boolean>() {

    override fun doInBackground(vararg params: Void?): Boolean {
        return ClientHttpUtil.postRequest(ProductAPI.Release.PATH, cartItem) ?: false
    }

    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)

        if (result == false){
            CartHelper.retrieveCart().add(cartItem.product, cartItem.quantity, null)
        }
    }

}