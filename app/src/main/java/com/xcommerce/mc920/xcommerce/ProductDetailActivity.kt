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
    val product = Product(id=15, name="Sofá", brand="Marca de um sofá", price=100L, category = "Eletrodomésticos", description = "Sofá marrom de 2 lugares que abre para deitar. Ou seja, trata-se de um sofá-cama de 2 (dois) lugares.", imageUrl="mock_sofa")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        setSupportActionBar(toolbar)

        // back button
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Trocar para adicionar item no carrinho", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        // get intent
        val id = intent.getStringExtra("id")

        // TODO: get product by id from back-end
        val codeString = "Código " + product.id.toString()
        this.product_detail_id.text = codeString
        this.product_detail_name.text = product.name
        this.product_detail_brand.text = product.brand
        val priceString = "R$" + ("%.2f".format(product.price/100.0)).toString()
        this.product_detail_price.text = priceString
        this.product_detail_category.text = product.category
        this.product_detail_description.text = product.description
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
