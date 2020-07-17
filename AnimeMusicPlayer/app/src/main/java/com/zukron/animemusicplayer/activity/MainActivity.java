package com.zukron.animemusicplayer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;
import com.zukron.animemusicplayer.R;
import com.zukron.animemusicplayer.adapter.ListMusicAdapter;
import com.zukron.animemusicplayer.model.Music;
import com.zukron.animemusicplayer.networking.APIListMusic;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements APIListMusic.OnResponse, ListMusicAdapter.OnSelectedItem {
    private RecyclerView rvListMusic;
    private ProgressBar pbListMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        APIListMusic apiListMusic = new APIListMusic(this, this);
        rvListMusic = findViewById(R.id.rv_list_music);
        pbListMusic = findViewById(R.id.pb_list_music);

        apiListMusic.getListMusic();
    }

    @Override
    public void listMusicResponse(ArrayList<Music> listMusic) {
        pbListMusic.setVisibility(View.INVISIBLE);

        ListMusicAdapter listMusicAdapter = new ListMusicAdapter(this, listMusic, this);
        rvListMusic.setLayoutManager(new LinearLayoutManager(this));
        rvListMusic.setAdapter(listMusicAdapter);
    }

    @Override
    public void errorResponse(VolleyError error) {
        error.printStackTrace();
    }

    @Override
    public void onSelected(Music music) {
        Intent intent = new Intent(this, DetailMusicActivity.class);
        intent.putExtra("music", music);
        startActivity(intent);
    }
}