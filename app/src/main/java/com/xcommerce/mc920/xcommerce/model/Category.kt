package com.xcommerce.mc920.xcommerce.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Category(val name: String)