package com.zukron.animemusicplayer.ui

import android.app.Application
import androidx.lifecycle.*
import com.zukron.animemusicplayer.model.MusicItem
import com.zukron.animemusicplayer.networking.InternetConnectionListener
import com.zukron.animemusicplayer.repository.MusicRepository
/**
 * Project name is AnimeMusicPlayer
 * Created by Zukron Alviandy R on 9/7/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MusicRepository(application)
    fun setInternetConnectionListener(internetConnectionListener: InternetConnectionListener) {
        repository.setInternetConnectionListener(internetConnectionListener)
    }

    val getMusicList: LiveData<List<MusicItem>> = repository.getMusicList()
}