package com.haidangkf.musicplayer.controls;

import android.os.Handler;

import com.haidangkf.musicplayer.dto.Song;

import java.util.ArrayList;

public class PlayerConstants {
    // list of Songs
    public static ArrayList<Song> SONG_LIST = new ArrayList<>();
    // song number which is playing right now from SONG_LIST
    public static int SONG_INDEX = 0;
    // song is playing or paused
    public static boolean SONG_PAUSED = true;
    // repeat mode
    public static boolean IS_REPEAT = false;
    // shuffle mode
    public static boolean IS_SHUFFLE = false;
    // handler for song changed (next, previous) defined in service
    public static Handler SONG_CHANGE_HANDLER;
    // handler for song play/pause defined in service
    public static Handler PLAY_PAUSE_HANDLER;
    // handler for showing song progress
    public static Handler PROGRESSBAR_HANDLER;
}
