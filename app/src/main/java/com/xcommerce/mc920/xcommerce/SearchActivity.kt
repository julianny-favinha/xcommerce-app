package com.xcommerce.mc920.xcommerce

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.xcommerce.mc920.xcommerce.home.search.recycler_view.ProductsAdapter
import com.xcommerce.mc920.xcommerce.model.Product
import com.xcommerce.mc920.xcommerce.model.ProductAPI
import com.xcommerce.mc920.xcommerce.model.Search
import com.xcommerce.mc920.xcommerce.utilities.ClientHttpUtil

import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.content_search.*

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        var emptyResults = emptyList<Product>().toMutableList()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)

        // back button
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
        toolbar.setNavigationOnClickListener {
            finish()
            //startActivity(Intent(applicationContext, MainActivity::class.java))
        }

        search_recycler_view.layoutManager = LinearLayoutManager(this)
        search_recycler_view.adapter = ProductsAdapter(emptyResults, this)

        search_button.setOnClickListener{searchOnClick()}
    }

    fun searchOnClick(){
        val query = search_field.text.toString()

        var search = SearchTask(query, this)

        search.execute()
    }

    inner class SearchTask internal constructor(private val query: String, context: Context) : AsyncTask<Void, Void, Search>(){

        private var resultsShown = emptyList<Product>().toMutableList()
        private var currentContext = context

        private fun populateResult(results: List<Product>) {
            if(search_recycler_view.adapter != null) {
                resultsShown.clear()
                resultsShown.addAll(results)
                search_recycler_view.adapter.notifyDataSetChanged()
            }
        }

        override fun onPreExecute() {
            super.onPreExecute()

            search_progress.visibility = View.VISIBLE
            search_recycler_view.visibility = View.GONE
            no_results_txt.visibility = View.GONE

            search_recycler_view.swapAdapter(ProductsAdapter(resultsShown, currentContext), true)
        }

        override fun doInBackground(vararg p0: Void?): Search? {
            return ClientHttpUtil.postRequest(ProductAPI.Search.PATH, query)?: Search(emptyList())
            /*return Search(listOf(
                    Product(
                        id=16,
                        name="Sofá1",
                        brand="Marca de um sofá",
                        price=100,
                        category = "Eletrodomésticos",
                        description = "Sofá marrom de 2 lugares que abre para deitar. Ou seja, trata-se de um sofá-cama de 2 (dois) lugares.",
                        imageUrl="http://www.ic.unicamp.br/~helio/imagens_inclinadas_png/neg_4.png"),
                    Product(
                        id=17,
                        name="Sofá2",
                        brand="Marca de um sofá",
                        price=10000,
                        category = "Eletrodomésticos",
                        description = "Sofá marrom de 2 lugares que abre para deitar. Ou seja, trata-se de um sofá-cama de 2 (dois) lugares.",
                        imageUrl="http://www.ic.unicamp.br/~helio/imagens_inclinadas_png/neg_4.png"),
                    Product(
                            id=18,
                            name="Sofá3",
                            brand="Marca de um sofáa",
                            price=100000,
                            category = "Eletrodomésticos",
                            description = "Sofá marrom de 2 lugares que abre para deitar. Ou seja, trata-se de um sofá-cama de 2 (dois) lugares.",
                            imageUrl="http://www.ic.unicamp.br/~helio/imagens_inclinadas_png/neg_4.png"),
                    Product(
                            id=19,
                            name="Sofá4",
                            brand="Marca de um sofá",
                            price=100000,
                            category = "Eletrodomésticos",
                            description = "Sofá marrom de 2 lugares que abre para deitar. Ou seja, trata-se de um sofá-cama de 2 (dois) lugares.",
                            imageUrl="http://www.ic.unicamp.br/~helio/imagens_inclinadas_png/neg_4.png"),
                    Product(
                            id=20,
                            name="Sofá5",
                            brand="Marca de um sofáa",
                            price=1000000,
                            category = "Eletrodomésticos",
                            description = "Sofá marrom de 2 lugares que abre para deitar. Ou seja, trata-se de um sofá-cama de 2 (dois) lugares.",
                            imageUrl="http://www.ic.unicamp.br/~helio/imagens_inclinadas_png/neg_4.png")
            ))*/
            //return Search(emptyList())
        }

        override fun onPostExecute(result: Search?) {
            super.onPostExecute(result)
            search_progress.visibility = View.GONE

            if (result != null) {
                if(!(result.result.isEmpty())) {
                    search_recycler_view.visibility = View.VISIBLE
                    no_results_txt.visibility = View.GONE
                    populateResult(result.result)
                } else {
                    search_recycler_view.visibility = View.GONE
                    no_results_txt.visibility = View.VISIBLE
                }
            }
        }

        override fun onCancelled() {
            super.onCancelled()
            finish()
        }
    }

}
