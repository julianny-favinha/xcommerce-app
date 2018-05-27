package com.xcommerce.mc920.xcommerce

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import com.xcommerce.mc920.xcommerce.model.UserAPI
import com.xcommerce.mc920.xcommerce.model.Login
import com.xcommerce.mc920.xcommerce.model.User
import com.xcommerce.mc920.xcommerce.model.UserResponse
import com.xcommerce.mc920.xcommerce.user.UserHelper
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

        email_sign_in_button.setOnClickListener { attemptLogin() }
        sign_up_button.setOnClickListener { redirectSignUp() }
    }

    override fun onResume() {
        super.onResume()

        if (UserHelper.retrieveNullableUser() != null){
            finish()
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin() {
        if (mAuthTask != null) {
            return
        }

        // Reset errors.
        email.error = null
        password.error = null

        // Store values at the time of the login attempt.
        val emailStr = email.text.toString()
        val passwordStr = password.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(passwordStr)){
            password.error = getString(R.string.error_field_required)
            focusView = password
            cancel = true
        } else if (!isPasswordValid(passwordStr)) {
            password.error = getString(R.string.error_invalid_password)
            focusView = password
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr)) {
            email.error = getString(R.string.error_field_required)
            focusView = email
            cancel = true
        } else if (!isEmailValid(emailStr)) {
            email.error = getString(R.string.error_invalid_email)
            focusView = email
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true)
            mAuthTask = UserLoginTask(emailStr, passwordStr)
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
        //TODO: Replace this with your own email logic
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        //TODO: Replace this with your own password logic
        return password.length > 4
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
    inner class UserLoginTask internal constructor(private val mEmail: String,
                                                   private val mPassword: String) : AsyncTask<Void, Void, UserResponse>() {

        override fun doInBackground(vararg params: Void): UserResponse? {
            return ClientHttpUtil.postRequest(UserAPI.Login.PATH, Login(mEmail, mPassword))
        }

        override fun onPostExecute(res: UserResponse?) {
            mAuthTask = null
            showProgress(false)

            if(res != null){
                UserHelper.updateUser(res.user)
                // Session.addToken(res.token)
                finish()
            }

            password.error = getString(R.string.error_incorrect_password)
            password.requestFocus()
        }

        override fun onCancelled() {
            mAuthTask = null
            showProgress(false)
        }
    }

}
