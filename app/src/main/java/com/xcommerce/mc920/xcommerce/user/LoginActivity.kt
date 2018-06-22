package com.xcommerce.mc920.xcommerce.user

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.xcommerce.mc920.xcommerce.R
import com.xcommerce.mc920.xcommerce.model.UserAPI
import com.xcommerce.mc920.xcommerce.model.Login
import com.xcommerce.mc920.xcommerce.model.SignIn
import com.xcommerce.mc920.xcommerce.model.UserResponse
import com.xcommerce.mc920.xcommerce.utilities.ClientHttpUtil
import kotlinx.android.synthetic.main.activity_login.*

const val EMAIL = "com.xcommerce.mc851.EMAIL"
const val PASSWORD = "com.xcommerce.mc851.PASSWORD"

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private var mAuthTask: UserLoginTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sign_in_button.setOnClickListener { attemptLogin() }
        sign_up_button.setOnClickListener { redirectSignUp() }
    }

    override fun onResume() {
        super.onResume()

        if (UserHelper.isLoggedIn()) {
            finish()
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin() {
        // Store values at the time of the login attempt.
        val emailStr = email.text.toString()
        val passwordStr = password.text.toString()

        if (isEmailValid(emailStr) || isPasswordValid(passwordStr)) {
            showProgress(true)
            mAuthTask = UserLoginTask(this, emailStr, passwordStr)
            mAuthTask!!.execute(null as Void?)
        }
    }

    private fun redirectSignUp() {
        val emailStr = email.text.toString()
        val passwordStr = password.text.toString()
        val signup = Intent(this, SignupActivity::class.java).apply {
            putExtra(EMAIL, emailStr)
            putExtra(PASSWORD, passwordStr)
        }
        email.error = null
        password.error = null
        startActivity(signup)
    }

    private fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 6
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        login_form.visibility = if (show) View.GONE else View.VISIBLE
        login_form.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 0 else 1).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        login_form.visibility = if (show) View.GONE else View.VISIBLE
                    }
                })

        login_progress.visibility = if (show) View.VISIBLE else View.GONE
        login_progress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        login_progress.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    inner class UserLoginTask internal constructor(private val container: LoginActivity,
                                                   private val mEmail: String,
                                                   private val mPassword: String): AsyncTask<Void, Void, UserResponse>() {

        override fun doInBackground(vararg params: Void): UserResponse? {
            return try {
                ClientHttpUtil.postRequest(UserAPI.Login.PATH, SignIn(mEmail, mPassword))
            } catch (e: Exception) {
                null
            }
        }

        override fun onPostExecute(res: UserResponse?) {
            mAuthTask = null
            showProgress(false)

            if(res != null) {
                UserHelper.updateUser(res.user)

                val settings = applicationContext.getSharedPreferences("com.xcommerce.mc920.xcommerce", 0)
                val editor = settings.edit()
                editor.putString("tokenUser", res.token)
                Log.d("login token", res.token)


                UserHelper.token = res.token

                // Apply the edits!
                editor.apply()

                finish()
            } else {
                Toast.makeText(container, "Email e/ou senha incorretos.", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onCancelled() {
            mAuthTask = null
            showProgress(false)
        }
    }

}
