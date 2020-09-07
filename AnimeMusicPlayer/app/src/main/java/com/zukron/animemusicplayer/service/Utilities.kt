package com.zukron.animemusicplayer.service

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Project name is AnimeMusicPlayer
 * Created by Zukron Alviandy R on 9/7/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
object Utilities {
    fun getConnectionType(context: Context): Int {
        // Returns connection type. 0: none; 1: mobile data; 2: wifi
        var result = 0
        val connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        networkCapabilities?.run {
            if (hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                result = 2
            } else if (hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                result = 1
            }
        }

        return result
    }

    fun Long.convertTime(): String {
        val minute = TimeUnit.MILLISECONDS.toMinutes(this)
        val second = TimeUnit.MILLISECONDS.toSeconds(this) % 60

        return String.format(Locale.US, "%02d:%02d", minute, second)
    }
}