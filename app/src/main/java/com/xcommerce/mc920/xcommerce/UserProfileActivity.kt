package com.xcommerce.mc920.xcommerce

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.xcommerce.mc920.xcommerce.model.ClientAPI
import com.xcommerce.mc920.xcommerce.model.ClientDetails
import com.xcommerce.mc920.xcommerce.utilities.ClientHttpUtil
import kotlinx.android.synthetic.main.activity_user_profile.*

class UserProfileActivity : AppCompatActivity() {

    private var mAuthTask: UserProfileTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        mAuthTask = UserProfileTask()
        mAuthTask!!.execute()
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        profile.visibility = if (show) View.GONE else View.VISIBLE
        profile.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 0 else 1).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        profile.visibility = if (show) View.GONE else View.VISIBLE
                    }
                })

        profile_progress.visibility = if (show) View.VISIBLE else View.GONE
        profile_progress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        profile_progress.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })
    }

    inner class UserProfileTask internal constructor() : AsyncTask<Void, Void, ClientDetails>() {

        override fun onPreExecute() {
            super.onPreExecute()
            showProgress(true)
        }

        override fun doInBackground(vararg params: Void): ClientDetails? {
            return ClientHttpUtil.getRequest(ClientAPI.Details.PATH)
        }

        override fun onPostExecute(det: ClientDetails?) {
            mAuthTask = null

            if (det?.success == true) {
                findViewById<TextView>(R.id.name).apply { text = det.name }
                findViewById<TextView>(R.id.email).apply { text = det.email }
                findViewById<TextView>(R.id.cpf).apply { text = det.cpf }
                findViewById<TextView>(R.id.statecity).apply { text = det.statecity }
                findViewById<TextView>(R.id.cep).apply { text = det.cep }
                findViewById<TextView>(R.id.address).apply { text = det.address }
                showProgress(false)
            } else {
                finish()
            }
        }

        override fun onCancelled() {
            mAuthTask = null
            finish()
        }
    }
}
