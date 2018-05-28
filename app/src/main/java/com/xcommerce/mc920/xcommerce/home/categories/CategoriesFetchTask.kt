package com.xcommerce.mc920.xcommerce.home.categories

import android.os.AsyncTask
import com.xcommerce.mc920.xcommerce.categories.CategoriesFragment
import com.xcommerce.mc920.xcommerce.model.Categories
import com.xcommerce.mc920.xcommerce.model.ProductAPI
import com.xcommerce.mc920.xcommerce.utilities.ClientHttpUtil

class CategoriesFetchTask(private var container: CategoriesFragment?): AsyncTask<String, Void, Categories>() {
    override fun doInBackground(vararg p0: String?): Categories {
        try {
            return ClientHttpUtil.getRequest(ProductAPI.Categories.PATH)
                    ?: Categories(emptyList())
        } catch (e: Exception) {
            return Categories(emptyList())
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