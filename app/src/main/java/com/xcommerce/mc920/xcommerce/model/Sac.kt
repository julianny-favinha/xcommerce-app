package com.xcommerce.mc920.xcommerce.model

data class MessageReceive(val timestamp: String, val sender: String, val message: String)

data class MessageSend(val sender: String, val message: String)