package com.zukron.animemusicplayer.networking

import android.content.Context
import com.zukron.animemusicplayer.service.Utilities
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Project name is AnimeMusicPlayer
 * Created by Zukron Alviandy R on 9/7/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
object RestApi {
    private const val BASE_URL = "https://animemusic.us/"
    private const val cacheSize = (5 * 1024 * 1024).toLong()

    fun getApiService(context: Context): ApiService {
        val myCache = Cache(context.cacheDir, cacheSize)

        var okHttpClient: OkHttpClient? = null
        okHttpClient ?: OkHttpClient.Builder()
                .cache(myCache)
                .addInterceptor { chain ->
                    var request = chain.request()
                    request = if (Utilities.getConnectionType(context) != 0) {
                        request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
                    } else {
                        request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build()
                    }
                    chain.proceed(request)
                }
                .build().also { okHttpClient = it }

        var retrofit: Retrofit? = null
        retrofit ?: Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient!!)
                .addConverterFactory(GsonConverterFactory.create())
                .build().also { retrofit = it }

        var apiService: ApiService? = null
        apiService ?: retrofit!!.create(ApiService::class.java).also { apiService = it }

        return apiService!!
    }
}