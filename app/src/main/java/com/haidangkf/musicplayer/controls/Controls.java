package com.haidangkf.musicplayer.controls;

import android.content.Context;

import com.haidangkf.musicplayer.R;
import com.haidangkf.musicplayer.service.MyService;
import com.haidangkf.musicplayer.utils.Common;

public class Controls {
    public static void playControl(Context context) {
        sendMessage(context.getResources().getString(R.string.play));
        PlayerConstants.SONG_PAUSED = false;
    }

    public static void pauseControl(Context context) {
        sendMessage(context.getResources().getString(R.string.pause));
        PlayerConstants.SONG_PAUSED = true;
    }

    public static void nextControl(Context context) {
        boolean isServiceRunning = Common.isServiceRunning(context, MyService.class);
        if (!isServiceRunning)
            return;
        if (PlayerConstants.SONGS_LIST.size() > 0) {
            if (PlayerConstants.SONG_INDEX < (PlayerConstants.SONGS_LIST.size() - 1)) {
                PlayerConstants.SONG_INDEX++;
                PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            } else {
                // play first song
                PlayerConstants.SONG_INDEX = 0;
                PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            }
        }
        PlayerConstants.SONG_PAUSED = false;
    }

    public static void previousControl(Context context) {
        boolean isServiceRunning = Common.isServiceRunning(context, MyService.class);
        if (!isServiceRunning)
            return;
        if (PlayerConstants.SONGS_LIST.size() > 0) {
            if (PlayerConstants.SONG_INDEX > 0) {
                PlayerConstants.SONG_INDEX--;
                PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            } else {
                // play last song
                PlayerConstants.SONG_INDEX = PlayerConstants.SONGS_LIST.size() - 1;
                PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            }
        }
        PlayerConstants.SONG_PAUSED = false;
    }

    private static void sendMessage(String message) {
        try {
            PlayerConstants.PLAY_PAUSE_HANDLER.sendMessage(PlayerConstants.PLAY_PAUSE_HANDLER.obtainMessage(0, message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
