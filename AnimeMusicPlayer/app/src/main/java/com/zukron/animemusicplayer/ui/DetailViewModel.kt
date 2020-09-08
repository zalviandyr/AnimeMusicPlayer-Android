package com.zukron.animemusicplayer.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.zukron.animemusicplayer.model.DetailMusicItem
import com.zukron.animemusicplayer.networking.InternetConnectionListener
import com.zukron.animemusicplayer.repository.MusicRepository

/**
 * Project name is AnimeMusicPlayer
 * Created by Zukron Alviandy R on 9/8/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MusicRepository(application)
    private val _musicId: MutableLiveData<Int> = MutableLiveData()
    fun setInternetConnectionListener(internetConnectionListener: InternetConnectionListener) {
        repository.setInternetConnectionListener(internetConnectionListener)
    }

    val getMusicDetail: LiveData<DetailMusicItem> = Transformations
            .switchMap(_musicId) {
                repository.getMusicDetail(it)
            }

    fun setMusicId(value: Int) {
        if (_musicId.value == value) {
            return
        }
        _musicId.value = value
    }
}