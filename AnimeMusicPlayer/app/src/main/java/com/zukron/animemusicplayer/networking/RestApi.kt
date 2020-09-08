package com.zukron.animemusicplayer.networking

import android.content.Context
import android.util.Log
import com.zukron.animemusicplayer.service.Utilities
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

/**
 * Project name is AnimeMusicPlayer
 * Created by Zukron Alviandy R on 9/7/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
object RestApi {
    private const val BASE_URL = "https://animemusic.us/"
    private const val cacheSize = (5 * 1024 * 1024).toLong()

    private var internetConnectionListener: InternetConnectionListener? = null
    fun setInternetConnectionListener(internetConnectionListener: InternetConnectionListener) {
        this.internetConnectionListener = internetConnectionListener
    }


    fun getApiService(context: Context): ApiService {
        val myCache = Cache(File(context.cacheDir, "retrofit-cache"), cacheSize)


        val okHttpClient: OkHttpClient by lazy {
            val temp = OkHttpClient.Builder()
            temp.cache(myCache)
            temp.addInterceptor(httpLoggingInterceptor())
            temp.addInterceptor(object : NetworkConnectionInterceptor() {
                override fun isInternetAvailable(): Int {
                    return Utilities.getConnectionType(context)
                }

                override fun onInternetUnavailable() {
                    internetConnectionListener?.onInternetUnavailable()
                }
            })

            temp.build()
        }

        val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

        val apiService: ApiService by lazy {
            retrofit.create(ApiService::class.java)
        }

        return apiService
    }

    private fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.d("Debug", message)
            }
        })
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }
}