package com.xcommerce.mc920.xcommerce.home.highlights

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xcommerce.mc920.xcommerce.R
import com.xcommerce.mc920.xcommerce.home.highlights.recycler_view.ProductsAdapter
import com.xcommerce.mc920.xcommerce.model.Product
import kotlinx.android.synthetic.main.fragment_highlights.*

class HighlightsFragment: Fragment() {

    private var highlights = emptyList<Product>().toMutableList()
    private var task: HighlightsFetchTask? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_highlights, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        recycler_view.layoutManager = LinearLayoutManager(this.context)
        recycler_view.adapter = ProductsAdapter(highlights, this)

        task = HighlightsFetchTask(this)
        task?.execute()

        if (isTaskRunning(task)) {
            showProgressBar()
        } else {
            hideProgressBar()
        }

        if (highlights.isNotEmpty()) {
            populateResult(highlights)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    fun showProgressBar() {
        progressbar.visibility = View.VISIBLE
        recycler_view.visibility = View.GONE
    }

    fun hideProgressBar() {
        progressbar.visibility = View.GONE
        recycler_view.visibility = View.VISIBLE
    }

    fun populateResult(highlights: List<Product>) {
        this.highlights.clear()
        this.highlights.addAll(highlights)
        recycler_view.adapter.notifyDataSetChanged()
    }

    private fun isTaskRunning(task: HighlightsFetchTask?): Boolean {
        return task?.status != AsyncTask.Status.FINISHED
    }
}