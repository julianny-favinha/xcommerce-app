package com.xcommerce.mc920.xcommerce.utilities

fun formatMoney(price: Int) = "R$" + ("%.2f".format(price / 100.0)).toString()