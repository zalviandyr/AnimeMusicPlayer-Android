package com.zukron.animemusicplayer.networking

import com.zukron.animemusicplayer.model.DetailMusic
import com.zukron.animemusicplayer.model.Music
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Project name is AnimeMusicPlayer
 * Created by Zukron Alviandy R on 9/7/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
interface ApiService {
    @GET("listmusic")
    suspend fun getMusicList(): Music

    @GET("jsongetid/{id}")
    suspend fun getMusic(
            @Path("id") id: Int
    ): DetailMusic
}