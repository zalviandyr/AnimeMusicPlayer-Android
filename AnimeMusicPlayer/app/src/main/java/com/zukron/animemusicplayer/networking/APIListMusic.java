package com.zukron.animemusicplayer.networking;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zukron.animemusicplayer.model.Music;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Project name is AnimeMusicPlayer
 * Created by Zukron Alviandy R on 7/16/2020
 */
public class APIListMusic {
    private Context context;
    private OnResponse onResponse;

    public APIListMusic(Context context, OnResponse onResponse) {
        this.context = context;
        this.onResponse = onResponse;
    }

    public void getListMusic() {
        String url = AnimeMusic.listMusic;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ArrayList<Music> listMusic = new ArrayList<>();
                    JSONObject json = new JSONObject(response);
                    JSONArray array = json.getJSONArray("post");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject musicJson = array.getJSONObject(i);
                        Music music = new Music();
                        music.setId(musicJson.getInt("id"));
                        music.setTitle(musicJson.getString("judulmusic"));
                        music.setBandName(musicJson.getString("namaband"));
                        music.setCover(musicJson.getString("coverartikel"));

                        listMusic.add(music);
                    }

                    onResponse.listMusicResponse(listMusic);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onResponse.errorResponse(error);
            }
        });

        execute(stringRequest);
    }

    private void execute(StringRequest stringRequest) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public interface OnResponse {
        void listMusicResponse(ArrayList<Music> listMusic);

        void errorResponse(VolleyError error);
    }
}
