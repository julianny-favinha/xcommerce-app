package com.xcommerce.mc920.xcommerce.sac

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.Toast
import com.xcommerce.mc920.xcommerce.R
import com.xcommerce.mc920.xcommerce.model.MessageReceive
import com.xcommerce.mc920.xcommerce.model.MessageSend
import com.xcommerce.mc920.xcommerce.model.SacAPI
import com.xcommerce.mc920.xcommerce.user.UserHelper
import com.xcommerce.mc920.xcommerce.utilities.ClientHttpUtil
import kotlinx.android.synthetic.main.activity_sac.*
import kotlinx.android.synthetic.main.content_sac.*

val myHandler = Handler()

class SACActivity : AppCompatActivity() {
    val senderName = UserHelper.retrieveUser().name

    override fun onCreate(savedInstanceState: Bundle?) {
        var emptyResults = emptyList<MessageReceive>().toMutableList()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sac)
        setSupportActionBar(toolbar)

        // back button
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        // custom layout manager
        val reverseLayoutManager = LinearLayoutManager(this)
        reverseLayoutManager.reverseLayout = true

        // Set the layout manager to your recyclerview
        sac_recycler_view.layoutManager = reverseLayoutManager
        sac_recycler_view.adapter = SACMessageAdapter(emptyResults, senderName, this)

        // send button
        sac_button.setOnClickListener{
            sendOnClick()
        }

        // get messages after 3s(or 3000ms)
        myHandler.postDelayed(handlerRunnable, 3000)

    }

    // remove handler callbacks when activity is destroyed
    override fun onDestroy() {
        super.onDestroy()
        myHandler.removeCallbacks(handlerRunnable)
    }

    private var handlerRunnable: Runnable = Runnable {
        val receive = ReceiveTask(this)
        receive.execute()
    }

    private fun sendOnClick(){
        val messageContent = sac_message.text.toString()

        if(messageContent.isNotBlank()) {
            val send = SendTask(MessageSend(sender = senderName, message = messageContent), this)
            sac_message.setText("")

            // execute send message
            send.execute()
        }

        // close digital keyboard
        sac_message.onEditorAction(EditorInfo.IME_ACTION_DONE)
    }

    inner class ReceiveTask internal constructor(context: Context): AsyncTask<Void, Void, List<MessageReceive>>(){
        private var resultsShown = emptyList<MessageReceive>().toMutableList()
        private var currentContext = context

        // populate recyclerview
        private fun populateResult(results: List<MessageReceive>) {
            if(sac_recycler_view.adapter != null) {
                resultsShown.clear()
                resultsShown.addAll(results)
                sac_recycler_view.adapter.notifyDataSetChanged()
            }
        }

        // http request
        override fun doInBackground(vararg p0: Void?): List<MessageReceive> {
            return ClientHttpUtil.getRequest(SacAPI.ReceiveMessages.PATH, true) ?: emptyList()
        }

        // populate and call again
        override fun onPostExecute(result: List<MessageReceive>) {
            super.onPostExecute(result)

            // swap to current adapter
            sac_recycler_view.swapAdapter(SACMessageAdapter(resultsShown, senderName, currentContext), true)
            populateResult(result) // show results on screen
            sac_recycler_view.scrollToPosition(0)

            // get messages again after 3s(or 3000ms)
            myHandler.postDelayed(handlerRunnable, 3000)
        }
    }

    inner class SendTask internal constructor(private val message: MessageSend, context: Context) : AsyncTask<Void, Void, Boolean>(){

        // http request
        override fun doInBackground(vararg p0: Void?): Boolean {
            return ClientHttpUtil.postRequest(SacAPI.SendMessage.PATH, message, true)?: false
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)

            if(result == false){
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(applicationContext, "Erro de envio de mensagem. Tente novamente.", duration)
                toast.show()
            }
        }

        // if cancelled, finish
        override fun onCancelled() {
            super.onCancelled()
            finish()
        }

    }

}
