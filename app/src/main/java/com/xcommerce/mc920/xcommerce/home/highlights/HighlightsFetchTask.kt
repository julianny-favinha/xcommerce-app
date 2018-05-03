package com.xcommerce.mc920.xcommerce.home.highlights

import android.os.AsyncTask
import com.xcommerce.mc920.xcommerce.client.ClientHttpHelper
import com.xcommerce.mc920.xcommerce.model.Highlights
import com.xcommerce.mc920.xcommerce.model.Product
import com.xcommerce.mc920.xcommerce.model.ProductAPI


class HighlightsFetchTask(private var container: HighlightsFragment?) : AsyncTask<String, Void, Highlights>() {

    override fun doInBackground(vararg p0: String?): Highlights {
        try {
            return ClientHttpHelper.getRequest(ProductAPI.Highlights.PATH)
                    ?: Highlights(emptyList())
            //return Highlights(listOf(Product(id=15, name="Sofá", brand="Marca de um sofá", price=100, category = "Eletrodomésticos", description = "Sofá marrom de 2 lugares que abre para deitar. Ou seja, trata-se de um sofá-cama de 2 (dois) lugares.", imageUrl="http://www.ic.unicamp.br/~helio/imagens_inclinadas_png/neg_4.png")))
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