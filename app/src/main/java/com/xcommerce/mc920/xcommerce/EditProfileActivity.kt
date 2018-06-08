package com.xcommerce.mc920.xcommerce

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.xcommerce.mc920.xcommerce.model.*
import com.xcommerce.mc920.xcommerce.user.UserHelper
import com.xcommerce.mc920.xcommerce.utilities.ClientHttpUtil
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity() {

    private var mAuthTask: UserEditTask? = null
    private var mCepTask: CepTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        edit_profile_button.setOnClickListener { attemptEdit() }
        cep_validate_button.setOnClickListener { completeWithCep() }
    }

    override fun onResume() {
        super.onResume()

        if (UserHelper.isLoggedIn()){
            val usr = UserHelper.retrieveUser()
            name.setText(usr.name, TextView.BufferType.EDITABLE)
            email.setText(usr.email, TextView.BufferType.EDITABLE)
            cep.setText(usr.cep, TextView.BufferType.EDITABLE)
            val complemento = (if (usr.address.complement != "") "Complemento " + usr.address.complement else "")
            val logradouro = usr.address.address.logradouro + ", " + usr.address.number + " " + complemento
            edit_profile_logradouro.setText(logradouro, TextView.BufferType.EDITABLE)
            edit_profile_neighborhood.setText(usr.address.address.neighborhood, TextView.BufferType.EDITABLE)
            val cityStateString = usr.address.address.city + " " + usr.address.address.state
            edit_profile_city_state.setText(cityStateString, TextView.BufferType.EDITABLE)
        } else {
            finish()
        }
    }

    /**
     * Find address corresponding to given CEP
     * Returns true if CEP is valid, false otherwise
     */
    private fun completeWithCep(): Boolean {
        cep.error = null
        val mCep = cep.text.toString()
        if (TextUtils.isEmpty(mCep) || mCep.length != 8) {
            cep.error = getString(R.string.error_invalid_cep)
            cep.requestFocus()
        } else if (mCepTask == null) {
            mCepTask = CepTask(mCep)
            mCepTask!!.execute(null as Void?)
        }
        return cep.error == null
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
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptEdit() {
        // Store values at the time of the login attempt.
        val nameStr = name.text.toString()
        val cepStr = cep.text.toString()
        val addressNumber = edit_profile_number.text.toString()
        val emailStr = email.text.toString()
        val passwordStr = password.text.toString()
        val checkPasswordStr = password_check.text.toString()

        if (mAuthTask != null) {
            return
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user signup attempt.
            showProgress(true)
            mAuthTask = UserEditTask(nameStr, cepStr, addressNumber, emailStr, passwordStr, checkPasswordStr)
            mAuthTask!!.execute(null as Void?)
        }
    }

    /**
     * Shows the progress UI and hides the signup form.
     */
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        edit_form.visibility = if (show) View.GONE else View.VISIBLE
        edit_form.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 0 else 1).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        edit_form.visibility = if (show) View.GONE else View.VISIBLE
                    }
                })

        edit_progress.visibility = if (show) View.VISIBLE else View.GONE
        edit_progress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        edit_progress.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })
    }

    /**
     * Represents an asynchronous registration task used to authenticate
     * the cep.
     */
    inner class CepTask internal constructor(private val mCep: String): AsyncTask<Void, Void, Address>() {

        override fun doInBackground(vararg params: Void): Address? {
            return ClientHttpUtil.getRequest(AddressAPI.CheckCep.of(mCep))
        }

        override fun onPostExecute(add: Address?) {
            mCepTask = null

            if (add != null) {
                edit_profile_logradouro.setText(add.logradouro, TextView.BufferType.EDITABLE)
                edit_profile_neighborhood.setText(add.neighborhood, TextView.BufferType.EDITABLE)
                val cityStateString = add.city + " " + add.state
                edit_profile_city_state.setText(cityStateString, TextView.BufferType.EDITABLE)
            } else {
                cep.error = getString(R.string.error_invalid_cep)
                cep.requestFocus()
            }
        }

        override fun onCancelled() {
            mCepTask = null
            cep.error = "Error"
            cep.requestFocus()
        }
    }

    /**
     * Represents an asynchronous registration task used to authenticate
     * the user.
     */
    inner class UserEditTask internal constructor(private val mName: String,
                                                  private val mCep: String,
                                                  private val mAddressNumber: String,
                                                  private val mEmail: String,
                                                  private val mPassword: String,
                                                  private val mCheckPassword: String) : AsyncTask<Void, Void, UserResponse>() {

        override fun onPreExecute() {
            // Reset errors.
            name.error = null
            cep.error = null
            edit_profile_number.error = null
            email.error = null
            password.error = null
            password_check.error = null

            var cancel = false
            var focusView: View? = null

            // Check for valid check password.
            if (mPassword != mCheckPassword) {
                password_check.error = getString(R.string.error_badcheck_password)
                focusView = password_check
                cancel = true
            }

            // Check for a valid password, if the user entered one.
            if (TextUtils.isEmpty(mPassword)) {
                password.error = getString(R.string.error_field_required)
                focusView = password
                cancel = true
            } else if (!isPasswordValid(mPassword)) {
                password.error = getString(R.string.error_invalid_password)
                focusView = password
                cancel = true
            }

            // Check for a valid email address.
            if (TextUtils.isEmpty(mEmail)) {
                email.error = getString(R.string.error_field_required)
                focusView = email
                cancel = true
            } else if (!isEmailValid(mEmail)) {
                email.error = getString(R.string.error_invalid_email)
                focusView = email
                cancel = true
            }

            // Check for valid address.
            if (TextUtils.isEmpty(mAddressNumber)) {
                edit_profile_number.error = getString(R.string.error_field_required)
                focusView = edit_profile_number
                cancel = true
            }

            // Check for valid cep.
            if (TextUtils.isEmpty(mCep)) {
                cep.error = getString(R.string.error_field_required)
                focusView = cep
                cancel = true
            } else if (!completeWithCep()) {
                cep.error = getString(R.string.error_invalid_cep)
                focusView = cep
                cancel = true
            }

            // Check for name.
            if (TextUtils.isEmpty(mName)) {
                name.error = getString(R.string.error_field_required)
                focusView = name
                cancel = true
            }

            if (cancel) {
                // There was an error; don't attempt signup and focus the first
                // form field with an error.
                focusView?.requestFocus()
                cancel(true)
            } else {
                // Show a progress spinner, and kick off a background task to
                // perform the user signup attempt.
                showProgress(true)
            }
        }

        override fun doInBackground(vararg params: Void): UserResponse? {
            return ClientHttpUtil.postRequest(UserAPI.Edit.PATH, Edit(mName, mCep, mAddressNumber, mEmail, mPassword))
        }

        override fun onPostExecute(res: UserResponse?) {
            mAuthTask = null
            showProgress(false)

            if (res != null) {
                UserHelper.updateUser(res.user)
                finish()
            }

            email.error = getString(R.string.error_edit)
            email.requestFocus()

        }

        override fun onCancelled() {
            mAuthTask = null
            showProgress(false)
        }
    }
}
