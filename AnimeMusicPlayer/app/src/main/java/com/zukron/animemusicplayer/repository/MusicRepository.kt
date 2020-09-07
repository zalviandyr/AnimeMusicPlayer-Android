package com.zukron.animemusicplayer.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.zukron.animemusicplayer.model.DetailMusicItem
import com.zukron.animemusicplayer.model.MusicItem
import com.zukron.animemusicplayer.networking.ApiService
import com.zukron.animemusicplayer.networking.RestApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.zukron.animemusicplayer.service.Utilities

/**
 * Project name is AnimeMusicPlayer
 * Created by Zukron Alviandy R on 9/7/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class MusicRepository(context: Context) {
    // if no connection and check cache is exist
    private val apiService: ApiService? = if (Utilities.getConnectionType(context) != 0) {
        RestApi.getApiService(context)
    } else {
        // if cache dir not empty
        if (context.cacheDir.listFiles()?.size != 0) {
            RestApi.getApiService(context)
        } else {
            null
        }
    }

    fun getMusicList(): LiveData<List<MusicItem>> {
        return object : LiveData<List<MusicItem>>() {
            override fun onActive() {
                super.onActive()

                apiService?.let {
                    CoroutineScope(IO).launch {
                        val music = it.getMusicList()
                        val musicList = music.data

                        withContext(Main) {
                            value = musicList
                        }
                    }
                }
            }
        }
    }

    fun getMusicDetail(id: Int): LiveData<DetailMusicItem> {
        return object : LiveData<DetailMusicItem>() {
            override fun onActive() {
                super.onActive()

                apiService?.let {
                    CoroutineScope(IO).launch {
                        val music = it.getMusic(id)
                        val musicDetail = music.data

                        withContext(Main) {
                            value = musicDetail[0]
                        }
                    }
                }
            }
        }
    }
}
