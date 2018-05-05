package com.xcommerce.mc920.xcommerce

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.TextureView
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.xcommerce.mc920.xcommerce.model.ClientAPI
import com.xcommerce.mc920.xcommerce.model.Response
import com.xcommerce.mc920.xcommerce.model.Signup
import com.xcommerce.mc920.xcommerce.utilities.ClientHttpUtil
import kotlinx.android.synthetic.main.activity_signup.*
import org.json.JSONObject

/**
 * A login screen that offers login via email/password.
 */
class SignupActivity : AppCompatActivity() {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private var mAuthTask: UserSignupTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val emailStr = intent.getStringExtra(EMAIL)
        val passwordStr = intent.getStringExtra(PASSWORD)

        findViewById<TextView>(R.id.email).apply { text = emailStr }
        findViewById<TextView>(R.id.password).apply { text = passwordStr }

        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptSignup()
                return@OnEditorActionListener true
            }
            false
        })

        email_sign_up_button.setOnClickListener { attemptSignup() }
        cep_validate_button.setOnClickListener { completeWithCep() }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptSignup() {
        if (mAuthTask != null) {
            return
        }

        // Reset errors.
        name.error = null
        cpf.error = null
        cep.error = null
        address.error = null
        email.error = null
        password.error = null
        password_check.error = null

        var cancel = false
        var focusView: View? = null

        // Store values at the time of the login attempt.
        val nameStr = name.text.toString()
        val cpfStr = cpf.text.toString()
        val cepStr = cep.text.toString()
        val addressStr = address.text.toString()
        val emailStr = email.text.toString()
        val passwordStr = password.text.toString()
        val checkPasswordStr = password_check.text.toString()

        // Check for valid check password.
        if(passwordStr != checkPasswordStr){
            password_check.error = getString(R.string.error_badcheck_password)
            focusView = password_check
            cancel = true
        }

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

        // Check for valid address.
        if(TextUtils.isEmpty(addressStr)) {
            address.error = getString(R.string.error_field_required)
            focusView = address
            cancel = true
        } else if (!isAddressValid(addressStr)) {
            address.error = getString(R.string.error_invalid_address)
            focusView = address
            cancel = true
        }

        // Check for valid cep.
        if(TextUtils.isEmpty(cepStr)) {
            cep.error = getString(R.string.error_field_required)
            focusView = cep
            cancel = true
        } else if (!isCepValid(cepStr)) {
            cep.error = getString(R.string.error_invalid_cep)
            focusView = cep
            cancel = true
        }

        // Check for valid cpf.
        if(TextUtils.isEmpty(cpfStr)) {
            cpf.error = getString(R.string.error_field_required)
            focusView = cpf
            cancel = true
        } else if (!isCpfValid(cpfStr)) {
            cpf.error = getString(R.string.error_invalid_cpf)
            focusView = cpf
            cancel = true
        }

        // Check for name.
        if(TextUtils.isEmpty(nameStr)) {
            name.error = getString(R.string.error_field_required)
            focusView = name
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt signup and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user signup attempt.
            showProgress(true)
            mAuthTask = UserSignupTask(nameStr, cpfStr, cepStr, addressStr, emailStr, passwordStr)
            mAuthTask!!.execute(null as Void?)
        }
    }

    /**
     * Find address corresponding to given CEP
     */
    private fun completeWithCep() {
        val mCep = cep.text.toString()
        if (TextUtils.isEmpty(mCep) || !isCepValid(mCep)) {
            cep.error = getString(R.string.error_invalid_cep)
            cep.requestFocus()
        } else {
            //TODO: Connect with module to get address
            try {
                // Simulate network access.
                Thread.sleep(500)
            } catch (e: InterruptedException) {
                return
            }

            findViewById<TextView>(R.id.address).apply { text = "Av. Albert Einstein" }
        }
    }

    private fun isCpfValid(cpf: String): Boolean {
        //TODO: Replace with proper CPF check
        val c = cpf.toIntOrNull()
        if (c != null && c <= 99999999999){
            return true
        }
        return false
    }

    private fun isCepValid(cep: String): Boolean {
        //TODO: Check cep validity
        val c = cep.toIntOrNull()
        if (c != null && c <= 99999999){
            return true
        }
        return false
    }

    private fun isAddressValid(address: String): Boolean {
        //TODO: Check address validity
        return true
    }

    private fun isEmailValid(email: String): Boolean {
        //TODO: Replace this with your own logic
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        //TODO: Replace this with your own logic
        return password.length > 4
    }

    /**
     * Shows the progress UI and hides the signup form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

            signup_form.visibility = if (show) View.GONE else View.VISIBLE
            signup_form.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 0 else 1).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            signup_form.visibility = if (show) View.GONE else View.VISIBLE
                        }
                    })

            signup_progress.visibility = if (show) View.VISIBLE else View.GONE
            signup_progress.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 1 else 0).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            signup_progress.visibility = if (show) View.VISIBLE else View.GONE
                        }
                    })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            signup_progress.visibility = if (show) View.VISIBLE else View.GONE
            signup_form.visibility = if (show) View.GONE else View.VISIBLE
        }
    }

    /**
     * Represents an asynchronous registration task used to authenticate
     * the user.
     */
    inner class UserSignupTask internal constructor(private val mName: String,
                                                    private val mCPF: String,
                                                    private val mCep: String,
                                                    private val mAddress: String,
                                                    private val mEmail: String,
                                                    private val mPassword: String) : AsyncTask<Void, Void, Boolean>() {

        override fun doInBackground(vararg params: Void): Boolean? {
            // TODO: attempt authentication against a network service

            val ret = ClientHttpUtil.postRequest<Response, Signup>(ClientAPI.Signup.PATH, Signup(mName, mCPF, mCep, mAddress, mEmail, mPassword))

            return ret?.success
        }

        override fun onPostExecute(success: Boolean?) {
            mAuthTask = null
            showProgress(false)

            if (success!!) {
                finish()
            } else {
                email.error = getString(R.string.error_email_unavailable)
                email.requestFocus()
            }
        }

        override fun onCancelled() {
            mAuthTask = null
            showProgress(false)
        }
    }

}
