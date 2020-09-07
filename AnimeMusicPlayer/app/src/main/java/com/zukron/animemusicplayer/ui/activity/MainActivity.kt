package com.zukron.animemusicplayer.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.zukron.animemusicplayer.R
import com.zukron.animemusicplayer.adapter.MusicListAdapter
import com.zukron.animemusicplayer.service.Utilities
import com.zukron.animemusicplayer.ui.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Project name is AnimeMusicPlayer
 * Created by Zukron Alviandy R on 9/7/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class MainActivity : AppCompatActivity(), MusicListAdapter.OnSelectedMusic {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var musicListAdapter: MusicListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // adapter
        musicListAdapter = MusicListAdapter()
        musicListAdapter.setOnSelectedMusic(this)

        // view model
        mainViewModel = ViewModelProvider(
                this, ViewModelProvider.AndroidViewModelFactory(application)
        ).get(MainViewModel::class.java)

        mainViewModel.getMusicList.observe(this) {
            if (it != null) {
                musicListAdapter.musicItemList = it
                pb_list_music.visibility = View.GONE
            }
        }

        // recycler view
        rv_list_music.adapter = musicListAdapter

        // snackbar for internet connection
        if (Utilities.getConnectionType(this) == 0) {
            Snackbar.make(linear_layout, R.string.no_internet, Snackbar.LENGTH_INDEFINITE)
                    .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                    .setDuration(20000)
                    .show()
        }
    }

    override fun onSelectedItem(id: Int) {
        val intent = Intent(this, DetailMusicActivity::class.java)
        intent.putExtra(DetailMusicActivity.EXTRA_ID, id)
        startActivity(intent)
    }
}