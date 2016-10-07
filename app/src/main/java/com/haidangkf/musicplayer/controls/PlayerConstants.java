package com.haidangkf.musicplayer.controls;

import android.os.Handler;

import com.haidangkf.musicplayer.dto.Song;

import java.util.ArrayList;

public class PlayerConstants {
    //list of Songs
    public static ArrayList<Song> SONGS_LIST = new ArrayList<>();
    //song number which is playing right now from SONGS_LIST
    public static int SONG_INDEX = 0;
    //song is playing or paused
    public static boolean SONG_PAUSED = true;
    //handler for song changed(next, previous) defined in service(SongService)
    public static Handler SONG_CHANGE_HANDLER;
    //handler for song play/pause defined in service(SongService)
    public static Handler PLAY_PAUSE_HANDLER;
    //handler for showing song progress defined in Activities(MainActivity, AudioPlayerActivity)
    public static Handler PROGRESSBAR_HANDLER;
}
