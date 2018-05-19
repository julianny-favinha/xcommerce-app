package com.xcommerce.mc920.xcommerce

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.xcommerce.mc920.xcommerce.model.ProductAPI
import com.xcommerce.mc920.xcommerce.model.Search
import com.xcommerce.mc920.xcommerce.utilities.ClientHttpUtil

import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)

        // back button
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
        toolbar.setNavigationOnClickListener {
            finish()
            //startActivity(Intent(applicationContext, MainActivity::class.java))
        }

        search_button.setOnClickListener{searchOnClick()}
    }

    fun searchOnClick(){
        val query = search_field.text.toString()

        var search = SearchTask(query)

        search.execute()
    }

    inner class SearchTask internal constructor(private val query: String) : AsyncTask<Void, Void, Search>(){

        override fun onPreExecute() {
            super.onPreExecute()
            search_progress.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg p0: Void?): Search? {
            return ClientHttpUtil.postRequest(ProductAPI.Search.PATH, query)
        }

        override fun onPostExecute(result: Search?) {
            super.onPostExecute(result)
            search_progress.visibility = View.GONE
        }

        override fun onCancelled() {
            super.onCancelled()
            finish()
        }
    }

}
