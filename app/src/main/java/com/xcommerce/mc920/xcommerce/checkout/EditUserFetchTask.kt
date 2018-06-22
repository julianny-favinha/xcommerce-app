package com.xcommerce.mc920.xcommerce.checkout

import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import com.xcommerce.mc920.xcommerce.model.Update
import com.xcommerce.mc920.xcommerce.model.User
import com.xcommerce.mc920.xcommerce.model.UserAPI
import com.xcommerce.mc920.xcommerce.user.AddressActivity
import com.xcommerce.mc920.xcommerce.utilities.ClientHttpUtil

class EditUserFetchTask(private var container: AddressActivity?): AsyncTask<User, Void, User>() {
    override fun doInBackground(vararg params: User?): User? {
        val user = params.first()!!
        Log.d("user", user.toString())
        try {
            return ClientHttpUtil.putRequest(UserAPI.Edit.PATH, Update(user.name, user.password, user.birthDate, user.gender, user.telephone, user.address), true)
        } catch (e: Exception) {
            Log.d("catch", e.toString())
            return null
        }
    }

    override fun onPostExecute(result: User?) {
        if (result != null) {
            container?.finishAddress(result.address)
        } else {
            Toast.makeText(container, "Não foi possível atualizar o endereço.", Toast.LENGTH_SHORT).show()
        }
    }
}