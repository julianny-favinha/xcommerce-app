package com.xcommerce.mc920.xcommerce.home.highlights

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xcommerce.mc920.xcommerce.R
import com.xcommerce.mc920.xcommerce.home.highlights.recycler_view.ProductsAdapter
import com.xcommerce.mc920.xcommerce.model.Product
import kotlinx.android.synthetic.main.fragment_highlights.*

class HighlightsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_highlights, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: Get data from backend
        val products = listOf(Product(name="Teste1", brand="Teste", price=1L),
                Product("Teste2", "Teste", 2L),
                Product("Teste2", "Teste", 2L),
                Product("Teste2", "Teste", 2L)
        ,Product("Teste2", "Teste", 2L)
        ,Product("Teste2", "Teste", 2L),
                Product("Teste2", "Teste", 2L)
        ,
                Product("Teste2", "Teste", 2L),Product("Teste2", "Teste", 2L),Product("Teste2", "Teste", 2L),Product("Teste2", "Teste", 2L),Product("Teste2", "Teste", 2L),Product("Teste2", "Teste", 2L))

        recycler_view.layoutManager = LinearLayoutManager(this.context)
        recycler_view.adapter = ProductsAdapter(products, this)

    }

}
