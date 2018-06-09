package com.xcommerce.mc920.xcommerce.user

import com.xcommerce.mc920.xcommerce.model.User

class UserHelper {
    companion object {
        var token: String? = null
        private var user: User? = null

        fun isLoggedIn(): Boolean {
            return user != null
        }

        fun retrieveToken(): String?{
            return token
        }

        fun retrieveNullableUser(): User? {
            return user
        }

        fun retrieveUser(): User {
            return user ?: throw IllegalStateException("User must be logged in!")
        }

        fun updateUser(nUser: User?) {
            user = nUser
        }

        fun logOut(): Boolean {
            if (user == null){
                return false
            }

            user = null
            return true
        }
    }
}