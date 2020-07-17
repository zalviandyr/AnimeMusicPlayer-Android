package com.zukron.animemusicplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.card.MaterialCardView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.zukron.animemusicplayer.R;
import com.zukron.animemusicplayer.model.Music;

import java.util.ArrayList;

/**
 * Project name is AnimeMusicPlayer
 * Created by Zukron Alviandy R on 7/16/2020
 */
public class ListMusicAdapter extends RecyclerView.Adapter<ListMusicAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Music> listMusic;
    private OnSelectedItem onSelectedItem;

    public ListMusicAdapter(Context context, ArrayList<Music> listMusic, OnSelectedItem onSelectedItem) {
        this.context = context;
        this.listMusic = listMusic;
        this.onSelectedItem = onSelectedItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_music, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Music music = listMusic.get(position);

        holder.tvBandNameItemMusic.setText(music.getBandName());
        holder.tvTitleItemMusic.setText(music.getTitle());
        Glide.with(context)
                .load(music.getCover())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.drawable.icons8_no_image_64)
                .into(holder.civ_item_music);

        holder.mcvItemMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectedItem.onSelected(music);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listMusic.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private MaterialCardView mcvItemMusic;
        private CircularImageView civ_item_music;
        private TextView tvBandNameItemMusic;
        private TextView tvTitleItemMusic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mcvItemMusic = itemView.findViewById(R.id.mcv_item_music);
            civ_item_music = itemView.findViewById(R.id.civ_item_music);
            tvBandNameItemMusic = itemView.findViewById(R.id.tv_band_name_item_music);
            tvTitleItemMusic = itemView.findViewById(R.id.tv_title_item_music);
        }
    }

    public interface OnSelectedItem {
        void onSelected(Music music);
    }
}
