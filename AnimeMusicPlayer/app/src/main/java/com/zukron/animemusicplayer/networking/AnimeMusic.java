package com.zukron.animemusicplayer.networking;

/**
 * Project name is MusicPlayer
 * Created by Zukron Alviandy R on 7/16/2020
 */
public interface AnimeMusic {
    String baseUrl = "https://animemusic.us";
    String listMusic = baseUrl + "/listmusic";
    String getMusic = baseUrl + "/jsongetid/{0}";
}
