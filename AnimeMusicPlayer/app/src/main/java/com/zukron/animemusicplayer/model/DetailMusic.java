package com.zukron.animemusicplayer.model;

/**
 * Project name is AnimeMusicPlayer
 * Created by Zukron Alviandy R on 7/16/2020
 */
public class DetailMusic {
    private int id;
    private String image;
    private String linkMp3;
    private String titlePost;

    public String getTitlePost() {
        return titlePost;
    }

    public void setTitlePost(String titlePost) {
        this.titlePost = titlePost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLinkMp3() {
        return linkMp3;
    }

    public void setLinkMp3(String linkMp3) {
        this.linkMp3 = linkMp3;
    }
}
