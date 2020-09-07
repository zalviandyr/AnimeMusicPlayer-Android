package com.zukron.animemusicplayer.model

import com.google.gson.annotations.SerializedName

/**
 * Project name is AnimeMusicPlayer
 * Created by Zukron Alviandy R on 7/16/2020
 */
data class DetailMusic(
        @SerializedName("data")
        var data: List<DetailMusicItem>
)

data class DetailMusicItem(
        @SerializedName("thumbnailUri")
        var image: String,
        @SerializedName("url")
        var url: String,
        @SerializedName("judulmusic")
        var title: String,
        @SerializedName("judulpost")
        var titlePost: String,
        @SerializedName("namaband")
        var bandName: String
)