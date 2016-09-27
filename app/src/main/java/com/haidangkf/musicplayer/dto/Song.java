package com.haidangkf.musicplayer.dto;

public class Song {

    private String songName;
    private String album;
    private String artist;
    private String path;
    private boolean isPlaying;

    public Song() {

    }

    public Song(String songName, String album, String artist, String path) {
        this.setSongName(songName);
        this.setAlbum(album);
        this.setArtist(artist);
        this.setPath(path);
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
