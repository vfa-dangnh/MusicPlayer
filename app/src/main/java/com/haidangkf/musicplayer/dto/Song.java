package com.haidangkf.musicplayer.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.haidangkf.musicplayer.fragment.SongFragment;

public class Song implements Parcelable {

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

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Name: " + name + "\n");
        s.append("Artist: " + artist + "\n");
        s.append("Album: " + album + "\n");
        s.append("Composer: " + composer + "\n");
        s.append("Duration: " + SongFragment.songUtil.milliSecondsToTimer(Long.parseLong(duration)) + "\n");
        s.append("Path: " + path);
        return s.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.artist);
        dest.writeString(this.album);
        dest.writeString(this.composer);
        dest.writeString(this.duration);
        dest.writeString(this.path);
    }

    protected Song(Parcel in) {
        this.name = in.readString();
        this.artist = in.readString();
        this.album = in.readString();
        this.composer = in.readString();
        this.duration = in.readString();
        this.path = in.readString();
    }

    public static final Parcelable.Creator<Song> CREATOR = new Parcelable.Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel source) {
            return new Song(source);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };
}
