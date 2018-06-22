package com.xcommerce.mc920.xcommerce.categories

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xcommerce.mc920.xcommerce.R
import com.xcommerce.mc920.xcommerce.home.categories.CategoriesFetchTask
import com.xcommerce.mc920.xcommerce.home.categories.recycler_view.CategoriesAdapter
import com.xcommerce.mc920.xcommerce.model.Category
import kotlinx.android.synthetic.main.fragment_categories.*

class CategoriesFragment : Fragment() {

    private var categories = emptyList<Category>().toMutableList()
    private var task: CategoriesFetchTask? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        recycler_view_categories.layoutManager = LinearLayoutManager(this.context)
        recycler_view_categories.adapter = CategoriesAdapter(categories, this)

        task = CategoriesFetchTask(this)
        task?.execute()

        if (isTaskRunning(task)){
            showProgressBar()
        } else {
            hideProgressBar()
        }

        if (categories.isNotEmpty()) {
            populateResult(categories.sortedBy({selector(it)}))
        }

        super.onViewCreated(view, savedInstanceState)
    }

    // function to sort categories by name
    fun selector(c: Category): String = c.name

    fun showProgressBar() {
        progressbar.visibility = View.VISIBLE
        recycler_view_categories.visibility = View.GONE
    }

    fun hideProgressBar() {
        progressbar.visibility = View.GONE
        recycler_view_categories.visibility = View.VISIBLE
    }

    fun populateResult(categories: List<Category>) {
        this.categories.clear()
        this.categories.addAll(categories)
        recycler_view_categories.adapter.notifyDataSetChanged()

        Log.d("populateResultCategory", categories.toString())
    }

    private fun isTaskRunning(task: CategoriesFetchTask?): Boolean {
        return task?.status != AsyncTask.Status.FINISHED
    }
}