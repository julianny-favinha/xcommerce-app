package com.xcommerce.mc920.xcommerce.home.highlights

import android.os.AsyncTask
import com.xcommerce.mc920.xcommerce.model.Highlights
import com.xcommerce.mc920.xcommerce.model.ProductAPI
import com.xcommerce.mc920.xcommerce.utilities.ClientHttpUtil

class HighlightsFetchTask(private var container: HighlightsFragment?) : AsyncTask<String, Void, Highlights>() {
    override fun doInBackground(vararg p0: String?): Highlights {
        try {
            return ClientHttpUtil.getRequest(ProductAPI.Highlights.PATH)
                    ?: Highlights(emptyList())
        } catch (e: Exception) {
            return Highlights(emptyList())
        }
    }

    override fun onPreExecute() {
        super.onPreExecute()
        container?.showProgressBar()
    }

    override fun onPostExecute(result: Highlights?) {
        super.onPostExecute(result)
        if (result != null) container?.populateResult(result.highlights)
        container?.hideProgressBar()
        this.container = null
    }
}