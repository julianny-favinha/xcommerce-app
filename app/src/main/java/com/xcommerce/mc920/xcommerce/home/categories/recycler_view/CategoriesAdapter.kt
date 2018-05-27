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
import com.xcommerce.mc920.xcommerce.model.Category
import kotlinx.android.synthetic.main.adapter_category_view.view.*

class CategoryViewHolder(item: View) : RecyclerView.ViewHolder(item) {

    var name = itemView.category_name

    // TODO: clique na categoria vai para tela que lista todos os produtos da categoria
//    val item = itemView.setOnClickListener(object: View.OnClickListener {
//        override fun onClick(p0: View?) {
//            val intent = Intent(p0?.context, ListProductsByCategoryActivity::class.java)
//            intent.putExtra("id", id.text)
//            p0?.context?.startActivity(intent)
//        }
//    })
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
        holder?.let {
            it.name.text = name.toLowerCase().capitalize()
        }
    }
}
