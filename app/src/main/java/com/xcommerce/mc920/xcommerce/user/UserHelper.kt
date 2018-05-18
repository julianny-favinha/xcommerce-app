package com.xcommerce.mc920.xcommerce.user

import com.xcommerce.mc920.xcommerce.model.User

class UserHelper {
    companion object {
        private var user: User? = null

        fun retrieveUser(): User? {
            return user
        }

        fun updateUser(nUser: User?) {
            user = nUser
        }

        fun logOut(): Boolean {
            if (user == null){
                return false
            }
            else {
                user = null
                return true
            }
        }
    }
}