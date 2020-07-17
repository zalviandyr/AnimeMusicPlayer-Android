package com.zukron.animemusicplayer.networking;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zukron.animemusicplayer.model.DetailMusic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;

/**
 * Project name is AnimeMusicPlayer
 * Created by Zukron Alviandy R on 7/16/2020
 */
public class APIDetailMusic {
    private Context context;
    private OnResponse onResponse;

    public APIDetailMusic(Context context, OnResponse onResponse) {
        this.context = context;
        this.onResponse = onResponse;
    }

    public void getDetailMusic(int id) {
        String url = MessageFormat.format(AnimeMusic.getMusic, id);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    JSONArray array = json.getJSONArray("data");

                    JSONObject musicJson = array.getJSONObject(0);

                    DetailMusic detailMusic = new DetailMusic();
                    detailMusic.setId(musicJson.getInt("id"));
                    detailMusic.setImage(musicJson.getString("thumbnailUri"));
                    detailMusic.setLinkMp3(musicJson.getString("url"));
                    detailMusic.setTitlePost(musicJson.getString("judulpost"));

                    onResponse.detailMusicResponse(detailMusic);
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
        void detailMusicResponse(DetailMusic detailMusic);

        void errorResponse(VolleyError error);
    }
}
