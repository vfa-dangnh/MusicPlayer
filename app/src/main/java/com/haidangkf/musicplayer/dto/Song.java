package com.haidangkf.musicplayer.dto;

public class Song {

    private String name;
    private String artist;
    private String album;
    private String composer;
    private String duration;
    private String path;

    public Song() {

    }

    public Song(String name, String artist, String album, String composer, String duration, String path) {
        this.setName(name);
        this.setArtist(artist);
        this.setAlbum(album);
        this.setComposer(composer);
        this.setDuration(duration);
        this.setPath(path);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
