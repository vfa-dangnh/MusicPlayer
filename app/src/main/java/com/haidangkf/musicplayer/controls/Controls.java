package com.haidangkf.musicplayer.controls;

import android.content.Context;

import com.haidangkf.musicplayer.R;
import com.haidangkf.musicplayer.service.MyService;
import com.haidangkf.musicplayer.utils.Common;

import java.util.Random;

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
        if (PlayerConstants.SONG_LIST.size() > 0) {
            if (PlayerConstants.SONG_INDEX < (PlayerConstants.SONG_LIST.size() - 1)) {
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
        if (PlayerConstants.SONG_LIST.size() > 0) {
            if (PlayerConstants.SONG_INDEX > 0) {
                PlayerConstants.SONG_INDEX--;
                PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            } else {
                // play last song
                PlayerConstants.SONG_INDEX = PlayerConstants.SONG_LIST.size() - 1;
                PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            }
        }
        PlayerConstants.SONG_PAUSED = false;
    }

    public static void completionSongControl(Context context) {
        // check for repeat is ON or OFF
        if (PlayerConstants.IS_REPEAT) {
            // repeat is on - play same song again
            PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
        } else if (PlayerConstants.IS_SHUFFLE) {
            // shuffle is on - play a random song
            Random rand = new Random();
            int randNum = rand.nextInt(PlayerConstants.SONG_LIST.size());
            while (randNum == PlayerConstants.SONG_INDEX && PlayerConstants.SONG_LIST.size() > 1) {
                randNum = rand.nextInt(PlayerConstants.SONG_LIST.size());
            }
            PlayerConstants.SONG_INDEX = randNum;
            PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
        } else {
            // no repeat or shuffle ON - play next song
            nextControl(context);
        }
    }

    private static void sendMessage(String message) {
        try {
            PlayerConstants.PLAY_PAUSE_HANDLER.sendMessage(PlayerConstants.PLAY_PAUSE_HANDLER.obtainMessage(0, message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
