package com.haidangkf.musicplayer.utils;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.haidangkf.musicplayer.dto.Song;
import com.haidangkf.musicplayer.fragment.FragmentPlay;

import java.util.ArrayList;
import java.util.Random;

public class MyApplication extends Application {

    static final String _id = MediaStore.Audio.Media._ID;
    static final String track = MediaStore.Audio.Media.TRACK;
    static final String title = MediaStore.Audio.Media.TITLE;
    static final String artist = MediaStore.Audio.Media.ARTIST;
    static final String duration = MediaStore.Audio.Media.DURATION;
    static final String album = MediaStore.Audio.Media.ALBUM;
    static final String composer = MediaStore.Audio.Media.COMPOSER;
    static final String year = MediaStore.Audio.Media.YEAR;
    static final String path = MediaStore.Audio.Media.DATA;
    static final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

    public static MediaPlayer mediaPlayer;
    public static ArrayList<Song> songList = new ArrayList<>();
    public static Song currentSong = new Song();
    public static int currentPosition; // vị trí bài hát trong songList, tính từ 0
    public static boolean isPlaying;
    public static boolean isPause;
    public static int isRepeat;
    public static boolean isShuffle;

    @Override
    public void onCreate() {
        super.onCreate();

        songList = getAllMusicList(getApplicationContext());
        mediaPlayer = new MediaPlayer();
        currentSong = songList.get(0);
        currentPosition = 0;
        isRepeat = 0; // mặc định là không lặp lại
        isShuffle = false;
    }

    public static void playBackMusic() { // play currentSong from beginning
        try {
            mediaPlayer.release();

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(currentSong.getPath());
            mediaPlayer.prepare();

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    endOfTheSong();
                }
            });

            isPlaying = true;
            mediaPlayer.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void endOfTheSong() {
        if (isRepeat == 1) { // currently repeat one song
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
        } else if (isRepeat == 2) { // currently repeat all songs
            nextSong();
        } else { // currently no repeat

            if (currentPosition != songList.size() - 1) nextSong();

        }
    }

    public static void pauseMusic() {
        try {
            if (isPlaying) {
                mediaPlayer.pause();
                isPlaying = false;
                isPause = true;
            } else {
                isPlaying = true;
                if (isPause) {
                    mediaPlayer.start(); // cho hát tiếp
                } else {
                    playBackMusic(); // hát bài hiện tại từ đầu
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void nextSong() {
        int numOfSong = songList.size();

        if (!isShuffle) { // chế độ Shuffle tắt
            if (currentPosition < numOfSong - 1) {
                currentPosition++;
                currentSong = songList.get(currentPosition);
                Log.i("my_log", "position = "+currentPosition);
                playBackMusic();
                displaySongInfo();
            } else {
                currentPosition = 0;
                currentSong = songList.get(currentPosition);
                Log.i("my_log", "position = "+currentPosition);
                playBackMusic();
                displaySongInfo();
            }
        } else { // chế độ Shuffle bật
            Random rand = new Random();
            currentPosition = rand.nextInt(numOfSong);
            currentSong = songList.get(currentPosition);
            Log.i("my_log", "position = "+currentPosition);
            playBackMusic();
            displaySongInfo();
        }
    }

    public static void previousSong() {
        int numOfSong = songList.size();

        if (!isShuffle) { // chế độ Shuffle tắt
            if (currentPosition > 0) {
                currentPosition--;
                currentSong = songList.get(currentPosition);
                Log.i("my_log", "position = "+currentPosition);
                playBackMusic();
                displaySongInfo();
            } else {
                currentPosition = numOfSong - 1;
                currentSong = songList.get(currentPosition);
                Log.i("my_log", "position = "+currentPosition);
                playBackMusic();
                displaySongInfo();
            }
        } else { // chế độ Shuffle bật
            Random rand = new Random();
            currentPosition = rand.nextInt(numOfSong);
            currentSong = songList.get(currentPosition);
            Log.i("my_log", "position = "+currentPosition);
            playBackMusic();
            displaySongInfo();
        }
    }

    public static void displaySongInfo() {
        FragmentPlay.tvSongInfo.setText("Name: " + currentSong.getSongName() + "\nAlbum: " + currentSong.getAlbum() + "\nArtist: " + currentSong.getArtist());
    }

    // ================================================================================

    public static ArrayList<Song> getAllMusicList(Context context) {
        ArrayList<Song> songArrayList = new ArrayList<>();

        ContentResolver cr = context.getContentResolver();
        final String[] columns = {_id, track, artist, title, album, duration, path, year, composer};
        Cursor cursor = cr.query(uri, columns, null, null, title + " ASC");

        int nName = cursor.getColumnIndex(title);
        int nArtist = cursor.getColumnIndex(artist);
        int nAlbum = cursor.getColumnIndex(album);
        int nPath = cursor.getColumnIndex(path);

        while (cursor.moveToNext()) {
            String _path = cursor.getString(nPath);
            if (_path.endsWith(".mp3") || _path.endsWith(".MP3")) {
                String songName = cursor.getString(nName);
                String _artist = cursor.getString(nArtist);
                String _album = cursor.getString(nAlbum);

                Song song = new Song(songName, _album, _artist, _path);
                songArrayList.add(song);
            }
        }

        return songArrayList;
    }

    public static ArrayList<String> getAlbumList(Context context) {
        ArrayList<String> albumList = new ArrayList<>();

        final String[] columns = {album};
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(uri, columns, null, null, album + " ASC");

        int nAlbum = cursor.getColumnIndex(album);

        while (cursor.moveToNext()) {
            String _album = cursor.getString(nAlbum);

            // nếu chưa có thì thêm vô
            if (!albumList.contains(_album))
                albumList.add(_album);
        }

        return albumList;
    }

    public static ArrayList<String> getArtistList(Context context) {
        ArrayList<String> artistList = new ArrayList<>();

        final String[] columns = {artist};
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(uri, columns, null, null, artist + " ASC");

        int nArtist = cursor.getColumnIndex(artist);

        while (cursor.moveToNext()) {
            String _artist = cursor.getString(nArtist);

            if (!artistList.contains(_artist))
                artistList.add(_artist);
        }

        return artistList;
    }

    public static ArrayList<Song> getSongListForArtist(Context context, String artistName) {
        ArrayList<Song> songArrayList = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        final String[] columns = {_id, track, artist, title, album, duration, path, year, composer};
        Cursor cursor = cr.query(uri, columns, artist + " = ?", new String[]{artistName}, title + " ASC");

        int nName = cursor.getColumnIndex(title);
        int nArtist = cursor.getColumnIndex(artist);
        int nAlbum = cursor.getColumnIndex(album);
        int nPath = cursor.getColumnIndex(path);

        while (cursor.moveToNext()) {
            String _path = cursor.getString(nPath);
            if (_path.endsWith(".mp3") || _path.endsWith(".MP3")) {
                String songName = cursor.getString(nName);
                String _artist = cursor.getString(nArtist);
                String _album = cursor.getString(nAlbum);

                Song song = new Song(songName, _album, _artist, _path);
                songArrayList.add(song);
            }
        }

        return songArrayList;
    }

    public static ArrayList<Song> getSongListForAlbum(Context context, String albumName) {
        ArrayList<Song> songArrayList = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        final String[] columns = {_id, track, artist, title, album, duration, path, year, composer};
        Cursor cursor = cr.query(uri, columns, album + " = ?", new String[]{albumName}, title + " ASC");

        int nName = cursor.getColumnIndex(title);
        int nArtist = cursor.getColumnIndex(artist);
        int nAlbum = cursor.getColumnIndex(album);
        int nPath = cursor.getColumnIndex(path);

        while (cursor.moveToNext()) {
            String _path = cursor.getString(nPath);
            if (_path.endsWith(".mp3") || _path.endsWith(".MP3")) {
                String songName = cursor.getString(nName);
                String _artist = cursor.getString(nArtist);
                String _album = cursor.getString(nAlbum);

                Song song = new Song(songName, _album, _artist, _path);
                songArrayList.add(song);
            }
        }

        return songArrayList;
    }

}
