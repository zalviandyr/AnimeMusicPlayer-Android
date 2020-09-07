package com.zukron.animemusicplayer.ui

import android.app.Application
import androidx.lifecycle.*
import com.zukron.animemusicplayer.model.DetailMusicItem
import com.zukron.animemusicplayer.model.MusicItem
import com.zukron.animemusicplayer.repository.MusicRepository
/**
 * Project name is AnimeMusicPlayer
 * Created by Zukron Alviandy R on 9/7/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MusicRepository(application)
    private val _musicId: MutableLiveData<Int> = MutableLiveData()

    val getMusicList: LiveData<List<MusicItem>> = repository.getMusicList()
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