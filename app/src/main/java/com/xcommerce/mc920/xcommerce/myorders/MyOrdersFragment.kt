package com.xcommerce.mc920.xcommerce.myorders
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xcommerce.mc920.xcommerce.R
import com.xcommerce.mc920.xcommerce.home.categories.OrdersFetchTask
import com.xcommerce.mc920.xcommerce.home.categories.recycler_view.CategoriesAdapter
import com.xcommerce.mc920.xcommerce.model.Order
import kotlinx.android.synthetic.main.fragment_orders.*

class MyOrdersFragment: Fragment() {

    private var myOrders = emptyList<Order>().toMutableList()
    private var task: OrdersFetchTask? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_orders, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        recycler_view_orders.layoutManager = LinearLayoutManager(this.context)
        recycler_view_orders.adapter = OrdersAdapter(orders, this)

        task = OrdersFetchTask(this)
        task?.execute()

        if (isTaskRunning(task)){
            showProgressBar()
        } else {
            hideProgressBar()
        }

        if (orders.isNotEmpty()) {
            populateResult(orders.sortedBy({selector(it)}))
        }

        super.onViewCreated(view, savedInstanceState)
    }

    fun showProgressBar() {
        progressbar.visibility = View.VISIBLE
        recycler_view_orders.visibility = View.GONE
    }

    fun hideProgressBar() {
        progressbar.visibility = View.GONE
        recycler_view_orders.visibility = View.VISIBLE
    }

    fun populateResult(orders: List<>) {
        this.orders.clear()
        this.orders.addAll(orders)
        recycler_view_orders.adapter.notifyDataSetChanged()

        Log.d("populateResultOrder", orders.toString())
    }

    private fun isTaskRunning(task: OrdersFetchTask?): Boolean {
        return task?.status != AsyncTask.Status.FINISHED
    }
}