package com.haidangkf.musicplayer.online.music;



public class OnlineSong {
    private String id_music;
    private String name;
    private String link;
    private String album_name;
    private String artist_name;

    public OnlineSong() {
    }

    public OnlineSong(String id_music, String name, String link, String album_name, String artist_name) {
        this.setId_music(id_music);
        this.setName(name);
        this.setLink(link);
        this.setAlbum_name(album_name);
        this.setArtist_name(artist_name);
    }


    public String getId_music() {
        return id_music;
    }

    public void setId_music(String id_music) {
        this.id_music = id_music;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }
}
