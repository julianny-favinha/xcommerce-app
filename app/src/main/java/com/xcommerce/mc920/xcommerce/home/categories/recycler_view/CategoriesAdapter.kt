package com.xcommerce.mc920.xcommerce.home.categories.recycler_view

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView.Adapter
import android.view.ViewGroup
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import com.xcommerce.mc920.xcommerce.R
import com.xcommerce.mc920.xcommerce.SearchActivity
import com.xcommerce.mc920.xcommerce.model.Category
import kotlinx.android.synthetic.main.adapter_category_view.view.*

class CategoryViewHolder(item: View) : RecyclerView.ViewHolder(item) {
    var name = itemView.category_name

    fun bindClick(listener: View.OnClickListener) {
        itemView.setOnClickListener(listener)
    }

    fun bindData(categoryName: String) {
        name.text = categoryName
    }
}

class CategoriesAdapter(private val categories: List<Category>, private val fragment: Fragment) : RecyclerView.Adapter<CategoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(fragment.context).inflate(R.layout.adapter_category_view, parent, false)

        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder?, position: Int) {
        val (name) = categories[position]

        holder?.bindClick(View.OnClickListener { p0 ->
            val intent = Intent(p0?.context, SearchActivity::class.java)
            intent.putExtra("categoryName", name)
            p0?.context?.startActivity(intent)
        })

        holder?.bindData(name.toLowerCase().capitalize())
    }
}
