package com.xcommerce.mc920.xcommerce.home.highlights.recycler_view

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView.Adapter
import android.view.ViewGroup
import com.xcommerce.mc920.xcommerce.model.Product
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import com.xcommerce.mc920.xcommerce.ProductDetailActivity
import com.xcommerce.mc920.xcommerce.R
import com.xcommerce.mc920.xcommerce.utilities.DownloadImageTask
import com.xcommerce.mc920.xcommerce.utilities.formatMoney
import kotlinx.android.synthetic.main.adapter_product_view.view.*

class ProductViewHolder(item: View) : RecyclerView.ViewHolder(item) {
    private val name = itemView.note_item_name
    private val brand = itemView.note_item_brand
    private val price = itemView.note_item_price
    private val category = itemView.note_item_category
    private val image = itemView.note_item_image

    fun bindClick(listener: View.OnClickListener) {
        itemView.setOnClickListener(listener)
    }

    fun bindData(product: Product) {
        name.text = product.name
        brand.text = product.brand
        category.text = product.category
        val priceString = formatMoney(product.price)
        price.text = priceString
        product.imageUrl?.let { DownloadImageTask(image).execute(product.imageUrl) }
                ?: image.setImageResource(R.drawable.image_placeholder)
    }
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
        val product = products[position]

        holder?.bindClick(View.OnClickListener { p0 ->
            val intent = Intent(p0?.context, ProductDetailActivity::class.java)
            intent.putExtra("product", product)
            p0?.context?.startActivity(intent)
        })

        holder?.bindData(product)
    }
}