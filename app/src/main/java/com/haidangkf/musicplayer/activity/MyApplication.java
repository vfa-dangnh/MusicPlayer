package com.haidangkf.musicplayer.activity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import com.haidangkf.musicplayer.online.OnlineDefine;
import com.haidangkf.musicplayer.online.music.OnlineSong;

import java.util.ArrayList;

public class MyApplication extends Application {

    public static Context context;
    public static ArrayList<OnlineSong> songList = new ArrayList<>();
    public static MediaPlayer mp = new MediaPlayer();

    public static OnlineSong currentSong = new OnlineSong();
    public static int index;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static void playMusic(String url) {
        try {
            Uri myUri = Uri.parse(url);

            mp.release();
            mp = new MediaPlayer();

            mp.setDataSource(context, myUri);
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.prepare(); //don't use prepareAsync for mp3 playback
            mp.start();

            Intent x = new Intent("OnlinePlayerActivity");
            x.putExtra("message", "play");
            context.sendBroadcast(x);

            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    nextMusic();
                }
            });

        } catch (Exception e) {
        }
    }

    public static void openPlayMusicPlayer() {
        try {
            currentSong = MyApplication.songList.get(MyApplication.index);
            String url = OnlineDefine.MUSIC_LINK + currentSong.getLink();
            MyApplication.playMusic(url);
        } catch (Exception e) {

        }
    }

    public static void pauseMusic() {
        if (mp.isPlaying())
            mp.pause();
        else
            mp.start();

        Intent x = new Intent("OnlinePlayerActivity");
        x.putExtra("message", "play");
        context.sendBroadcast(x);
    }

    public static void nextMusic() {
        int musicListSize = songList.size();

        if (index >= musicListSize - 1) {
            index = 0;
        } else {
            index++;
        }

        openPlayMusicPlayer();
    }

    public static void preMusic() {
        int musicListSize = songList.size();

        if (index <= 0) {
            index = musicListSize - 1;
        } else {
            index--;
        }

        openPlayMusicPlayer();
    }

//    public static void playMusic(String url){
//        new PlayMusicOnline().execute(url);
//    }
//
//    private static class PlayMusicOnline extends AsyncTask<String, Integer, Void> {
//        protected Void doInBackground(String... urls) {
//            try{
//                Uri myUri = Uri.parse(urls[0]);
//
//                if(mp.isPlaying()){
//                    mp.release();
//                    mp = new mp();
//                }
//
//                mp.setDataSource(context, myUri);
//                mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                mp.prepare(); //don't use prepareAsync for mp3 playback
//                mp.start();
//            }catch (Exception e){
//
//            }
//        }
//
//        protected void onProgressUpdate(Integer... progress) {
//
//        }
//
//        protected void onPostExecute(Long result) {
//
//        }
//    }
}
