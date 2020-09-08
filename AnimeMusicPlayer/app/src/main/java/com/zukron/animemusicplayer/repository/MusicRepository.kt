package com.zukron.animemusicplayer.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.zukron.animemusicplayer.model.DetailMusicItem
import com.zukron.animemusicplayer.model.MusicItem
import com.zukron.animemusicplayer.networking.InternetConnectionListener
import com.zukron.animemusicplayer.networking.RestApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.CoroutineExceptionHandler

/**
 * Project name is AnimeMusicPlayer
 * Created by Zukron Alviandy R on 9/7/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class MusicRepository(context: Context) {
    private val apiService = RestApi.getApiService(context)
    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.d("Debug", exception.message.toString())
    }

    fun getMusicList(): LiveData<List<MusicItem>> {
        return object : LiveData<List<MusicItem>>() {
            override fun onActive() {
                super.onActive()

                apiService.let {
                    CoroutineScope(IO).launch(handler) {
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

                apiService.let {
                    CoroutineScope(IO).launch(handler) {
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

    fun setInternetConnectionListener(internetConnectionListener: InternetConnectionListener) {
        RestApi.setInternetConnectionListener(internetConnectionListener)
    }
}
