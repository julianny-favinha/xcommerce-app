package com.xcommerce.mc920.xcommerce.myorders

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.xcommerce.mc920.xcommerce.R
import com.xcommerce.mc920.xcommerce.model.Order
import kotlinx.android.synthetic.main.activity_my_orders.*
import kotlinx.android.synthetic.main.content_my_orders.*

class MyOrdersActivity : AppCompatActivity() {

    private var orders = emptyList<Order>().toMutableList()
    private var task: MyOrdersFetchTask? = null
    var adapter : MyOrdersViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_orders)
        setSupportActionBar(toolbar)

        // back button
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        adapter = MyOrdersViewAdapter(this, orders)
        listview.adapter = adapter

        task = MyOrdersFetchTask(this)
        task?.execute()

        if (isTaskRunning(task)){
            showProgressBar()
        } else {
            hideProgressBar()
        }

        if (orders.isNotEmpty()) {
            populateResult(orders)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun showProgressBar() {
        progressbar.visibility = View.VISIBLE
        listview.visibility = View.GONE
    }

    fun hideProgressBar() {
        progressbar.visibility = View.GONE
        listview.visibility = View.VISIBLE
    }

    fun populateResult(orders: List<Order>) {
        this.orders.clear()
        this.orders.addAll(orders)
        adapter?.notifyDataSetChanged()
        Log.d("populateResultOrder", orders.toString())
    }

    private fun isTaskRunning(task: MyOrdersFetchTask?): Boolean {
        return task?.status != AsyncTask.Status.FINISHED
    }
}
