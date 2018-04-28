package com.xcommerce.mc920.xcommerce

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.xcommerce.mc920.xcommerce.model.Product

import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.content_product_detail.*

class ProductDetailActivity : AppCompatActivity() {

    // TODO: após o request do módulo isso nao vai existir
    val product = Product(id=15, name="Sofá", brand="Marca de um sofá", price=100L, imageUrl="mock_sofa")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Trocar para adicionar item no carrinho", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        // pega intent e coloca o valor no text view
        val id = intent.getStringExtra("id")

        product_detail_id.text = product.id.toString()
        product_detail_name.text = product.name
        product_detail_brand.text = product.brand
        val priceString = "R$" + ("%.2f".format(product.price/100.0)).toString()
        product_detail_price.text = priceString
    }
}
