package com.zukron.animemusicplayer.service;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Project name is AnimeMusicPlayer
 * Created by Zukron Alviandy R on 7/17/2020
 */
public class MusicPlayer {
    private static MediaPlayer mediaPlayer;
    public static MediaPlayer init(String dataSource) {
        try {
            // reset jika ingin memutar lagu lain, lagu sebelumnya akan berhenti
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.reset();
            }

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(dataSource);
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return mediaPlayer;
    }
}
