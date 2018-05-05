package com.xcommerce.mc920.xcommerce.categories

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xcommerce.mc920.xcommerce.R
import com.xcommerce.mc920.xcommerce.home.categories.recycler_view.CategoriesAdapter
import com.xcommerce.mc920.xcommerce.model.Category
import kotlinx.android.synthetic.main.fragment_categories.*
import kotlinx.android.synthetic.main.fragment_highlights.*

class CategoriesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: Get data from backend
        val categories = listOf(Category(id=1, name="Eletrodomésticos"),
                Category(id=2, name="Cama, mesa e banho"),
                Category(id=3, name="Games"),
                Category(id=4, name="Desktops e notebooks"),
                Category(id=5, name="Elétrica"),
                Category(id=6, name="Celulares"),
                Category(id=7, name="Acessórios"),
                Category(id=8, name="Câmeras e filmadoras"),
                Category(id=9, name="Instrumentos musicais"))

        recycler_view_categories.layoutManager = LinearLayoutManager(this.context)
        recycler_view_categories.adapter = CategoriesAdapter(categories, this)

    }

}