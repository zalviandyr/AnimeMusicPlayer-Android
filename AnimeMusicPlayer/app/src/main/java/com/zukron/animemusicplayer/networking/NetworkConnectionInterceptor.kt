package com.zukron.animemusicplayer.networking

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

/**
 * Project name is AnimeMusicPlayer
 * Created by Zukron Alviandy R on 9/8/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
abstract class NetworkConnectionInterceptor : Interceptor {
    private val HEADER_PRAGMA = "Pragma"
    private val HEADER_CACHE_CONTROL = "Cache-Control"
    abstract fun isInternetAvailable(): Int
    abstract fun onInternetUnavailable()

    override fun intercept(chain: Interceptor.Chain): Response {
        return if (isInternetAvailable() != 0) {
            val response = chain.proceed(chain.request())

            val cacheControl = CacheControl.Builder()
                    .maxAge(5, TimeUnit.MINUTES)
                    .build()

            response.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                    .build()
        } else {
            onInternetUnavailable()
            val request = chain.request()

            val cacheControl = CacheControl.Builder()
                    .maxStale(7, TimeUnit.DAYS)
                    .onlyIfCached()
                    .build()

            request.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .cacheControl(cacheControl)
                    .build()

            val response = chain.proceed(request)
            response
        }
    }
}