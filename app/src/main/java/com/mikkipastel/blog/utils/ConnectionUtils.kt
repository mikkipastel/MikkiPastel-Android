package com.mikkipastel.blog.utils

import android.content.Context
import android.net.ConnectivityManager

class ConnectionUtils {
    fun isConnected(context: Context?): Boolean {

        var isConnected = false

        if (context == null) {
            return isConnected
        }

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        isConnected = (connectivityManager.activeNetworkInfo != null
                && connectivityManager.activeNetworkInfo.isConnected)

        return isConnected
    }
}