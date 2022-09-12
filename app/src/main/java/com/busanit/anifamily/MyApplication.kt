package com.busanit.anifamily

import androidx.multidex.MultiDexApplication
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MyApplication : MultiDexApplication() {
    companion object {
        var auth = Firebase.auth
        var email: String? = null
        var nickname:String? =null
        fun checkAuth(): Boolean {
            var currentUser = auth.currentUser
            return currentUser?.let { // 인증성공
                email = currentUser.email
                currentUser.isEmailVerified
            } ?: let {
                false // 인증실패
            }
        }
    }

//    override fun onCreate() {
//        super.onCreate()
//        auth = Firebase.auth
//    }
}