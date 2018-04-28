package com.xcommerce.mc920.xcommerce.home.highlights.recycler_view

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView.Adapter
import android.view.ViewGroup
import com.xcommerce.mc920.xcommerce.model.Product
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.xcommerce.mc920.xcommerce.ProductDetailActivity
import com.xcommerce.mc920.xcommerce.R
import kotlinx.android.synthetic.main.adapter_product_view.view.*

class ProductViewHolder(item: View) : RecyclerView.ViewHolder(item) {

    var id = itemView.note_item_id
    var name = itemView.note_item_name
    var brand = itemView.note_item_brand
    var price = itemView.note_item_price
    var image = itemView.note_item_image

    val item = itemView.setOnClickListener(object: View.OnClickListener {
        override fun onClick(p0: View?) {
            val intent = Intent(p0?.context, ProductDetailActivity::class.java)
            intent.putExtra("id", id.text)
            p0?.context?.startActivity(intent)
        }
    })
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
        val (id, name, brand, price, imageUrl) = products[position]
        holder?.let {
            it.id.text = id.toString()
            it.name.text = name
            it.brand.text = brand
            val priceString = "R$" + ("%.2f".format(price/100.0)).toString()
            it.price.text = priceString
            // TODO: aqui vai ser trocado pela imagem da url indicada em imageUrl
            it.image.setImageResource(R.drawable.mock_sofa)
        }
    }
}