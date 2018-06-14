package com.xcommerce.mc920.xcommerce.cart

import android.os.AsyncTask
import com.xcommerce.mc920.xcommerce.model.Cart
import com.xcommerce.mc920.xcommerce.model.CartItem
import com.xcommerce.mc920.xcommerce.model.ProductAPI
import com.xcommerce.mc920.xcommerce.utilities.ClientHttpUtil

class ReleaseTask() : AsyncTask<CartItem, Void, Boolean>() {

    private var cartItem: CartItem? = null

    override fun doInBackground(vararg p0: CartItem): Boolean {
        cartItem = p0[0]
        return ClientHttpUtil.postRequest(ProductAPI.Release.PATH, cartItem) ?: false
    }

    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)

        if (result == false) {
            CartHelper.retrieveCart().add(cartItem!!.product, cartItem!!.quantity, null)
        }
    }

}