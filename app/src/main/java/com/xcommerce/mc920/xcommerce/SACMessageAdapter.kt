package com.xcommerce.mc920.xcommerce

import android.content.Context
import android.support.design.R.id.*
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xcommerce.mc920.xcommerce.model.MessageReceive
import kotlinx.android.synthetic.main.adapter_sac_view.view.*
import android.R.attr.button
import android.view.Gravity
import android.R.attr.gravity
import android.widget.LinearLayout



class MessageViewHolder(item: View, context: Context, name: String) : RecyclerView.ViewHolder(item) {
    val sender = item.sender
    val timestamp = item.timestamp
    val content = item.messagecontent
    val layout = item.message_layout
    val outer = item.outer_layout
    val currentContext = context
    val senderName = name

    fun bindData(message: MessageReceive) {
        val year = message.timestamp.take(4)
        val month = message.timestamp.drop(5).take(2)
        val day = message.timestamp.drop(8).take(2)
        val hour = message.timestamp.drop(11)

        val newTimestamp = "$day/$month/$year, $hour"

        sender.text = message.sender
        timestamp.text = newTimestamp
        content.text = message.message

        if (message.sender != senderName) {
            val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.gravity = Gravity.START
            outer.layoutParams = params

            layout.setBackgroundResource(R.drawable.my_message_received)

            layout.gravity = Gravity.START
            outer.gravity = Gravity.START

        } else {
            val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.gravity = Gravity.END
            outer.layoutParams = params

            layout.setBackgroundResource(R.drawable.my_message)

            layout.gravity = Gravity.END
            outer.gravity = Gravity.END
        }

    }
}

class SACMessageAdapter(private val messages: List<MessageReceive>, sender: String, private val context: Context) : RecyclerView.Adapter<MessageViewHolder>() {
    val senderName = sender

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_sac_view, parent, false)

        return MessageViewHolder(view, context, senderName)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder?, position: Int) {
        val message = messages[position]

        holder?.bindData(message)
    }

}