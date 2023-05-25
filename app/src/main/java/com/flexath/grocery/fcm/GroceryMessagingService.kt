package com.flexath.grocery.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class GroceryMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("token", token)
    }
}