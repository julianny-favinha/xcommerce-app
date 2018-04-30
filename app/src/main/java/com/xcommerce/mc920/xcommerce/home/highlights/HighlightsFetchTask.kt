package com.xcommerce.mc920.xcommerce.home.highlights

import android.os.AsyncTask
import android.util.Log
import com.xcommerce.mc920.xcommerce.model.Highlights
import com.xcommerce.mc920.xcommerce.model.Product


class HighlightsFetchTask(private var container: HighlightsFragment?) : AsyncTask<String, Void, Highlights>() {

    override fun doInBackground(vararg p0: String?): Highlights {
        try {
            Thread.sleep(3000)
            return Highlights(listOf(Product(id=0, name="Sofá", brand="Marca de um sofá", price=100L, category = "Eletrodomésticos", description = "Sofá marrom de 2 lugares que abre para deitar.", imageUrl="mock_sofa"),
                    Product(id=1, name="Sofá", brand="Marca de um sofá", price=100L, category = "Eletrodomésticos", description = "Sofá marrom de 2 lugares que abre para deitar.", imageUrl="mock_sofa"),
                    Product(id=2, name="Sofá", brand="Marca de um sofá", price=100L, category = "Eletrodomésticos", description = "Sofá marrom de 2 lugares que abre para deitar.", imageUrl="mock_sofa"),
                    Product(id=3, name="Sofá", brand="Marca de um sofá", price=100L, category = "Eletrodomésticos", description = "Sofá marrom de 2 lugares que abre para deitar.", imageUrl="mock_sofa"),
                    Product(id=4, name="Sofá", brand="Marca de um sofá", price=100L, category = "Eletrodomésticos", description = "Sofá marrom de 2 lugares que abre para deitar.", imageUrl="mock_sofa"),
                    Product(id=5, name="Sofá", brand="Marca de um sofá", price=100L, category = "Eletrodomésticos", description = "Sofá marrom de 2 lugares que abre para deitar.", imageUrl="mock_sofa"),
                    Product(id=6, name="Sofá", brand="Marca de um sofá", price=100L, category = "Eletrodomésticos", description = "Sofá marrom de 2 lugares que abre para deitar.", imageUrl="mock_sofa"),
                    Product(id=7, name="Sofá", brand="Marca de um sofá", price=100L, category = "Eletrodomésticos", description = "Sofá marrom de 2 lugares que abre para deitar.", imageUrl="mock_sofa"),
                    Product(id=8, name="Sofá", brand="Marca de um sofá", price=100L, category = "Eletrodomésticos", description = "Sofá marrom de 2 lugares que abre para deitar.", imageUrl="mock_sofa"),
                    Product(id=9, name="Sofá", brand="Marca de um sofá", price=100L, category = "Eletrodomésticos", description = "Sofá marrom de 2 lugares que abre para deitar.", imageUrl="mock_sofa"),
                    Product(id=10, name="Sofá", brand="Marca de um sofá", price=100L, category = "Eletrodomésticos", description = "Sofá marrom de 2 lugares que abre para deitar.", imageUrl="mock_sofa"),
                    Product(id=11, name="Sofá", brand="Marca de um sofá", price=100L, category = "Eletrodomésticos", description = "Sofá marrom de 2 lugares que abre para deitar.", imageUrl="mock_sofa"),
                    Product(id=12, name="Sofá", brand="Marca de um sofá", price=100L, category = "Eletrodomésticos", description = "Sofá marrom de 2 lugares que abre para deitar.", imageUrl="mock_sofa")))
        } catch (e: Exception) {
            Log.e("[FETCH TASK]", e.toString())
            return Highlights(emptyList())
        }
    }

    override fun onPreExecute() {
        super.onPreExecute()
        container?.showProgressBar()
    }

    override fun onPostExecute(result: Highlights?) {
        super.onPostExecute(result)
        Log.d("[FETCH TASK]", result.toString())
        if(result != null) container?.populateResult(result.products)
        container?.hideProgressBar()
        this.container = null
    }
}