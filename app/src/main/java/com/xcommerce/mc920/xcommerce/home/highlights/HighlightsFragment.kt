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
        val products = listOf(Product(id=0, name="Sofá", brand="Marca de um sofá", price=100, category = "Eletrodomésticos", description = "Sofá marrom de 2 lugares que abre para deitar.", imageUrl="mock_sofa"),
                Product(id=1, name="Sofá", brand="Marca de um sofá", price=100, category = "Eletrodomésticos", description = "Sofá marrom de 2 lugares que abre para deitar.", imageUrl="mock_sofa"),
                Product(id=2, name="Sofá", brand="Marca de um sofá", price=100, category = "Eletrodomésticos", description = "Sofá marrom de 2 lugares que abre para deitar.", imageUrl="mock_sofa"),
                Product(id=3, name="Sofá", brand="Marca de um sofá", price=100, category = "Eletrodomésticos", description = "Sofá marrom de 2 lugares que abre para deitar.", imageUrl="mock_sofa"),
                Product(id=4, name="Sofá", brand="Marca de um sofá", price=100, category = "Eletrodomésticos", description = "Sofá marrom de 2 lugares que abre para deitar.", imageUrl="mock_sofa"),
                Product(id=5, name="Sofá", brand="Marca de um sofá", price=100, category = "Eletrodomésticos", description = "Sofá marrom de 2 lugares que abre para deitar.", imageUrl="mock_sofa"),
                Product(id=6, name="Sofá", brand="Marca de um sofá", price=100, category = "Eletrodomésticos", description = "Sofá marrom de 2 lugares que abre para deitar.", imageUrl="mock_sofa"),
                Product(id=7, name="Sofá", brand="Marca de um sofá", price=100, category = "Eletrodomésticos", description = "Sofá marrom de 2 lugares que abre para deitar.", imageUrl="mock_sofa"),
                Product(id=8, name="Sofá", brand="Marca de um sofá", price=100, category = "Eletrodomésticos", description = "Sofá marrom de 2 lugares que abre para deitar.", imageUrl="mock_sofa"),
                Product(id=9, name="Sofá", brand="Marca de um sofá", price=100, category = "Eletrodomésticos", description = "Sofá marrom de 2 lugares que abre para deitar.", imageUrl="mock_sofa"),
                Product(id=10, name="Sofá", brand="Marca de um sofá", price=100, category = "Eletrodomésticos", description = "Sofá marrom de 2 lugares que abre para deitar.", imageUrl="mock_sofa"),
                Product(id=11, name="Sofá", brand="Marca de um sofá", price=100, category = "Eletrodomésticos", description = "Sofá marrom de 2 lugares que abre para deitar.", imageUrl="mock_sofa"),
                Product(id=12, name="Sofá", brand="Marca de um sofá", price=100, category = "Eletrodomésticos", description = "Sofá marrom de 2 lugares que abre para deitar.", imageUrl="mock_sofa"))

        recycler_view.layoutManager = LinearLayoutManager(this.context)
        recycler_view.adapter = ProductsAdapter(products, this)

    }

}
