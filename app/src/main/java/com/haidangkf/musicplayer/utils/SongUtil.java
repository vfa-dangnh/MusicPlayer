package com.haidangkf.musicplayer.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.haidangkf.musicplayer.dto.Song;

import java.util.ArrayList;

/**
 * Created by Dang Nguyen on 30-Sep-16.
 */

public class SongUtil {

    String isMusic = MediaStore.Audio.Media.IS_MUSIC;
    String id = MediaStore.Audio.Media._ID;
    String title = MediaStore.Audio.Media.TITLE;
    String track = MediaStore.Audio.Media.TRACK;
    String artist = MediaStore.Audio.Media.ARTIST;
    String duration = MediaStore.Audio.Media.DURATION;
    String album = MediaStore.Audio.Media.ALBUM;
    String composer = MediaStore.Audio.Media.COMPOSER;
    String year = MediaStore.Audio.Media.YEAR;
    String path = MediaStore.Audio.Media.DATA;
    Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

    private static final String TAG = "my_log";
    Context context;

    // constructor
    public SongUtil(Context context) {
        this.context = context;
    }

    public ArrayList<Song> getAllSongs() {
        ArrayList<Song> songArrayList = new ArrayList<>();

        ContentResolver cr = context.getContentResolver();
        final String[] columns = {id, track, artist, title, album, duration, path, year, composer};
//        Cursor cursor = cr.query(uri, columns, null, null, title + " ASC");
        Cursor cursor = cr.query(uri, null, isMusic + " != 0", null, title + " ASC");

        while (cursor.moveToNext()) {
            String sPath = cursor.getString(cursor.getColumnIndex(path));
//            Log.d(TAG, "path: " + sPath);

            // filter file
//            if (path.endsWith(".mp3") || path.endsWith(".MP3")) {
//            }

            String sTitle = cursor.getString(cursor.getColumnIndex(title));
            String sArtist = cursor.getString(cursor.getColumnIndex(artist));
            String sAlbum = cursor.getString(cursor.getColumnIndex(album));
            String sComposer = cursor.getString(cursor.getColumnIndex(composer));
            String sDuration = cursor.getString(cursor.getColumnIndex(duration));
//            Log.d(TAG, "info: " + sTitle + " - " + sArtist + " - " + sAlbum + " - " + sComposer + " - " + sDuration);

            Song song = new Song(sTitle, sArtist, sAlbum, sComposer, sDuration, sPath);
            songArrayList.add(song);
        }

        return songArrayList;
    }

    public ArrayList<String> getAlbumList() {
        ArrayList<String> albumList = new ArrayList<>();
        ArrayList<Song> songList = getAllSongs();

        for (Song song : songList){
            if(!albumList.contains(song.getAlbum())){
                albumList.add(song.getAlbum());
            }
        }

        return albumList;
    }

    public ArrayList<Song> getSongForAlbum(String albumName) {
        ArrayList<Song> allSongs = getAllSongs();
        ArrayList<Song> songList = new ArrayList<>();

        for (Song song : allSongs){
            if(song.getAlbum().equals(albumName)){
                songList.add(song);
            }
        }

        return songList;
    }

    public ArrayList<String> getArtistList() {
        ArrayList<String> artistList = new ArrayList<>();
        ArrayList<Song> songList = getAllSongs();

        for (Song song : songList){
            if(!artistList.contains(song.getArtist())){
                artistList.add(song.getArtist());
            }
        }

        return artistList;
    }

    public ArrayList<Song> getSongForArtist(String artistName) {
        ArrayList<Song> allSongs = getAllSongs();
        ArrayList<Song> songList = new ArrayList<>();

        for (Song song : allSongs){
            if(song.getArtist().equals(artistName)){
                songList.add(song);
            }
        }

        return songList;
    }

    //////////////////////////////// Timer Util ////////////////////////////////

    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    public int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage = (((double) currentSeconds) / totalSeconds) * 100;

        // return percentage
        return percentage.intValue();
    }

    public int progressToTimer(int progress, int totalDuration) {
        int currentDuration;
        totalDuration /= 1000; // convert to seconds
        currentDuration = (int) ((((double) progress) / 100) * totalDuration); // in seconds

        // return current duration in milliseconds
        return currentDuration * 1000;
    }

}
