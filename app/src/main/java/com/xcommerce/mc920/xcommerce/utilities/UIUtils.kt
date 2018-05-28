package com.xcommerce.mc920.xcommerce.utilities

import android.widget.ListView

class UIUtils {
    companion object {
        fun setListViewHeightBasedOnItems(listView: ListView): Boolean {

            val listAdapter = listView.getAdapter()
            if (listAdapter != null) {

                val numberOfItems = listAdapter!!.getCount()

                // Get total height of all items.
                var totalItemsHeight = 0
                for (itemPos in 0 until numberOfItems) {
                    val item = listAdapter!!.getView(itemPos, null, listView)
                    item.measure(0, 0)
                    totalItemsHeight += item.getMeasuredHeight()
                }

                // Get total height of all item dividers.
                val totalDividersHeight = listView.getDividerHeight() * (numberOfItems - 1)

                // Set list height.
                val params = listView.getLayoutParams()
                params.height = totalItemsHeight + totalDividersHeight
                listView.setLayoutParams(params)
                listView.requestLayout()

                return true

            } else {
                return false
            }

        }
    }
}