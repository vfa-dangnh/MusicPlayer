package com.haidangkf.musicplayer.online.album;

public class OnlineAlbum {
    private String id_album;
    private String album_name;
    private String album_img_link;

    public OnlineAlbum() {
    }

    public OnlineAlbum(String id_album, String album_name, String album_img_link) {
        this.setId_album(id_album);
        this.setAlbum_name(album_name);
        this.setAlbum_img_link(album_img_link);
    }

    public String getId_album() {
        return id_album;
    }

    public void setId_album(String id_album) {
        this.id_album = id_album;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public String getAlbum_img_link() {
        return album_img_link;
    }

    public void setAlbum_img_link(String album_img_link) {
        this.album_img_link = album_img_link;
    }
}
