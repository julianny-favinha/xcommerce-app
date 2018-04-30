package com.xcommerce.mc920.xcommerce.home.highlights

import android.os.AsyncTask
import android.util.Log
import com.xcommerce.mc920.xcommerce.model.Highlights
import com.xcommerce.mc920.xcommerce.model.Product


class HighlightsFetchTask(private var container: HighlightsFragment?) : AsyncTask<String, Void, Highlights>() {

    override fun doInBackground(vararg p0: String?): Highlights {
        try {
            TODO("do request and use jackson to parse, make it generic!")
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
        if (result != null) container?.populateResult(result.products)
        container?.hideProgressBar()
        this.container = null
    }
}