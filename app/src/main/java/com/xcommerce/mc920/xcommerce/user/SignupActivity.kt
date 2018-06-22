package com.xcommerce.mc920.xcommerce.user

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import com.xcommerce.mc920.xcommerce.R
import com.xcommerce.mc920.xcommerce.model.*
import com.xcommerce.mc920.xcommerce.utilities.ClientHttpUtil
import kotlinx.android.synthetic.main.activity_signup.*


/**
 * A login screen that offers login via email/password.
 */
class SignupActivity : AppCompatActivity() {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private var mAuthTask: UserSignupTask? = null
    private var mCepTask: CepTask? = null

    var address: Address? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val emailStr = intent.getStringExtra(EMAIL)
        val passwordStr = intent.getStringExtra(PASSWORD)

        findViewById<TextView>(R.id.email).apply { text = emailStr }
        findViewById<TextView>(R.id.password).apply { text = passwordStr }

        email_sign_up_button.setOnClickListener { attemptSignup() }
        cep_validate_button.setOnClickListener { completeWithCep() }
    }

    override fun onResume() {
        super.onResume()

        if (UserHelper.isLoggedIn()){
            finish()
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptSignup() {
        // Store values at the time of the login attempt.
        val nameStr = name.text.toString()
        val cpfStr = cpf.text.toString()
        val birthStr = birth_date.text.toString()
        val numberStr = sign_up_number.text.toString()
        val complementStr = sign_up_complement.text.toString()
        val emailStr = email.text.toString()
        val genderStr = gender.text.toString()
        val telephoneStr = telephone.text.toString()
        val passwordStr = password.text.toString()
        val checkPasswordStr = password_check.text.toString()

        if (mAuthTask != null) {
            return
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user signup attempt.
            showProgress(true)
            mAuthTask = UserSignupTask(nameStr, cpfStr, address!!.cep, numberStr, complementStr, birthStr, emailStr, genderStr, telephoneStr, passwordStr, checkPasswordStr)
            mAuthTask!!.execute(null as Void?)
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

    private fun isCpfValid(cpf: String): Boolean {
        if (cpf.toFloatOrNull() == null || cpf.length != 11){
            return false
        }

        var firstvalue =  0
        var secondvalue = 0

        for (i in 0..8){
            firstvalue += (cpf[i] - '0')*(10-i)
            secondvalue += (cpf[i] - '0')*(11-i)
        }
        firstvalue = (firstvalue*10)%11
        secondvalue = ((secondvalue+2*(cpf[9] - '0'))*10)%11

        return (firstvalue%10 == cpf[9] - '0' && secondvalue%10 == cpf[10] - '0' )
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
     * Shows the progress UI and hides the signup form.
     */
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
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
    }

    /**
     * Repdresents an asynchronous registration task use to authenticate
     * the cep.
     */
    inner class CepTask internal constructor(private val mCep: String): AsyncTask<Void, Void, Address>() {

        override fun doInBackground(vararg params: Void): Address? {
            return ClientHttpUtil.getRequest(AddressAPI.CheckCep.of(mCep))
        }

        override fun onPostExecute(add: Address?) {
            mCepTask = null

            if (add != null) {
                address = add

                sign_up_logradouro.setText(add.logradouro, TextView.BufferType.EDITABLE)
                sign_up_neighborhood.setText(add.neighborhood, TextView.BufferType.EDITABLE)
                val cityStateString = add.city + " " + add.state
                sign_up_city_state.setText(cityStateString, TextView.BufferType.EDITABLE)
                sign_up_address.visibility = View.VISIBLE
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
    inner class UserSignupTask internal constructor(private val mName: String,
                                                    private val mCPF: String,
                                                    private val mCep: String,
                                                    private val mNumber: String,
                                                    private val mComplement: String,
                                                    private val mBirth: String,
                                                    private val mEmail: String,
                                                    private val mGender: String,
                                                    private val mTelephone: String,
                                                    private val mPassword: String,
                                                    private val mCheckPassword: String) : AsyncTask<Void, Void, UserResponse>() {

        override fun onPreExecute() {
            // Reset errors.
            name.error = null
            cpf.error = null
            cep.error = null
            sign_up_number.error = null
            email.error = null
            password.error = null
            password_check.error = null

            var cancel = false
            var focusView: View? = null

            // Check for valid check password.
            if(mPassword != mCheckPassword){
                password_check.error = getString(R.string.error_badcheck_password)
                focusView = password_check
                cancel = true
            }

            // Check for a valid password, if the user entered one.
            if (TextUtils.isEmpty(mPassword)){
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
            if(TextUtils.isEmpty(mNumber)) {
                sign_up_number.error = getString(R.string.error_field_required)
                focusView = sign_up_number
                cancel = true
            }

            // Check for valid cep.
            if(TextUtils.isEmpty(mCep) || address == null) {
                cep.error = getString(R.string.error_field_required)
                focusView = cep
                cancel = true
            } else if (!completeWithCep()) {
                cep.error = getString(R.string.error_invalid_cep)
                focusView = cep
                cancel = true
            }

            // Check for valid cpf.
            if(TextUtils.isEmpty(mCPF)) {
                cpf.error = getString(R.string.error_field_required)
                focusView = cpf
                cancel = true
            } else if (!isCpfValid(mCPF)) {
                cpf.error = getString(R.string.error_invalid_cpf)
                focusView = cpf
                cancel = true
            }

            // Check for name.
            if(TextUtils.isEmpty(mName)) {
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
            val addressFull = AddressFull(address!!, mNumber.toInt(), mComplement)
            val signUp = Signup(User(mName, mEmail, mPassword, mBirth, mCPF, addressFull, mGender, mTelephone))
            return ClientHttpUtil.postRequest(UserAPI.Signup.PATH, signUp)
        }

        override fun onPostExecute(res: UserResponse?) {
            mAuthTask = null
            showProgress(false)

            if(res != null) {
                UserHelper.updateUser(res.user)

                val settings = applicationContext.getSharedPreferences("com.xcommerce.mc920.xcommerce", 0)
                val editor = settings.edit()
                editor.putString("tokenUser", res.token)

                UserHelper.token = res.token
                Log.d("signUp token", res.token)

                // Apply the edits!
                editor.apply()

                finish()
            } else {
                email.error = getString(R.string.error_existing_user)
                email.requestFocus()
            }
        }

        override fun onCancelled() {
            mAuthTask = null
            showProgress(false)
        }
    }

}
