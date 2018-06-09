package com.xcommerce.mc920.xcommerce.myorders
import android.os.AsyncTask
import com.xcommerce.mc920.xcommerce.categories.CategoriesFragment
import com.xcommerce.mc920.xcommerce.model.Categories
import com.xcommerce.mc920.xcommerce.model.ProductAPI
import com.xcommerce.mc920.xcommerce.utilities.ClientHttpUtil

class OrdersFetchTask(private var container: OrdersFragment?): AsyncTask<String, Void, Orders>() {

        override fun doInBackground(vararg p0: String?): Orders {
            try {
                return ClientHttpUtil.getRequest(ProductAPI.Orders.PATH)
                        ?: Order(emptyList())
            } catch (e: Exception) {
                return Orders(emptyList())
            }
        }

        override fun onPreExecute() {
            super.onPreExecute()
            container?.showProgressBar()
        }

        override fun onPostExecute(result: Categories?) {
            super.onPostExecute(result)
            if (result != null) container?.populateResult(result.categories.distinct())
            container?.hideProgressBar()
            this.container = null
        }
    }
}