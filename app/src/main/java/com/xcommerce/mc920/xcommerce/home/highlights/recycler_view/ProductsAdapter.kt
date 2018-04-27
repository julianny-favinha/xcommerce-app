package com.xcommerce.mc920.xcommerce.home.highlights.recycler_view

import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView.Adapter
import android.view.ViewGroup
import com.xcommerce.mc920.xcommerce.model.Product
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.xcommerce.mc920.xcommerce.R
import kotlinx.android.synthetic.main.adapter_product_view.view.*

class ProductViewHolder(item: View) : RecyclerView.ViewHolder(item) {

    var name = itemView.note_item_name
    var brand = itemView.note_item_brand
    var price = itemView.note_item_price
    var image = itemView.note_item_image

}

class ProductsAdapter(private val products: List<Product>, private val fragment: Fragment) : Adapter<ProductViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(fragment.context).inflate(R.layout.adapter_product_view, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder?, position: Int) {
        val (name, brand, price, imageUrl) = products[position]
        holder?.let {
            it.name.text = name
            it.brand.text = brand
            it.price.text = price.toString()
            // TODO: aqui vai ser trocado pela imagem da url indicada em imageUrl
            it.image.setImageResource(R.drawable.mock_sofa)
        }
    }
}