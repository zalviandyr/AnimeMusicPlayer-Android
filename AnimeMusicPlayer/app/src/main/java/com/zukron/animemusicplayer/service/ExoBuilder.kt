package com.zukron.animemusicplayer.service

import android.content.Context
import com.google.android.exoplayer2.SimpleExoPlayer

/**
 * Project name is AnimeMusicPlayer
 * Created by Zukron Alviandy R on 9/7/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
object ExoBuilder {
    private var simpleExoPlayer: SimpleExoPlayer? = null

    fun getInstance(context: Context): SimpleExoPlayer {
        simpleExoPlayer ?: SimpleExoPlayer.Builder(context).also { simpleExoPlayer = it.build() }

        return simpleExoPlayer!!
    }
}