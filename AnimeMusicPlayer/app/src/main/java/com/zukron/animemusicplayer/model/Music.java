package com.zukron.animemusicplayer.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Project name is AnimeMusicPlayer
 * Created by Zukron Alviandy R on 7/16/2020
 */
public class Music implements Parcelable {
    private int id;
    private String cover;
    private String title;
    private String bandName;

    public Music() {
    }

    protected Music(Parcel in) {
        id = in.readInt();
        cover = in.readString();
        title = in.readString();
        bandName = in.readString();
    }

    public static final Creator<Music> CREATOR = new Creator<Music>() {
        @Override
        public Music createFromParcel(Parcel in) {
            return new Music(in);
        }

        @Override
        public Music[] newArray(int size) {
            return new Music[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBandName() {
        return bandName;
    }

    public void setBandName(String bandName) {
        this.bandName = bandName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(cover);
        parcel.writeString(title);
        parcel.writeString(bandName);
    }
}
