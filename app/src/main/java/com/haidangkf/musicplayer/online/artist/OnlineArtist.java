package com.haidangkf.musicplayer.online.artist;

public class OnlineArtist {
    private String id_artist;
    private String artist_name;
    private String artist_img_link;
    private String artist_info = "";

    public OnlineArtist() {
    }

    public OnlineArtist(String artist_id, String artist_img_link, String artist_name) {
        this.setId_artist(artist_id);
        this.setArtist_img_link(artist_img_link);
        this.setArtist_name(artist_name);
    }

    public OnlineArtist(String artist_id, String artist_name, String artist_img_link, String artist_info) {
        this.setId_artist(artist_id);
        this.setArtist_name(artist_name);
        this.setArtist_img_link(artist_img_link);
        this.setArtist_info(artist_info);
    }

    public String getId_artist() {
        return id_artist;
    }

    public void setId_artist(String id_artist) {
        this.id_artist = id_artist;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public String getArtist_img_link() {
        return artist_img_link;
    }

    public void setArtist_img_link(String artist_img_link) {
        this.artist_img_link = artist_img_link;
    }

    public String getArtist_info() {
        return artist_info;
    }

    public void setArtist_info(String artist_info) {
        this.artist_info = artist_info;
    }
}
