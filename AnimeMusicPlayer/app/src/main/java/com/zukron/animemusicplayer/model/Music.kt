package com.zukron.animemusicplayer.model

import com.google.gson.annotations.SerializedName

/**
 * Project name is AnimeMusicPlayer
 * Created by Zukron Alviandy R on 7/16/2020
 */
data class Music(
        @SerializedName("post")
        var data: List<MusicItem>
)

data class MusicItem(
        @SerializedName("id")
        var id: Int,
        @SerializedName("coverartikel")
        var cover: String,
        @SerializedName("judulmusic")
        var title: String,
        @SerializedName("namaband")
        var bandName: String
)