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

public class MyApplication extends Application{

    public static Context context;
    public static ArrayList<OnlineSong> songList = new ArrayList<>();
    public static MediaPlayer mediaPlayer = new MediaPlayer();

    public static OnlineSong currentSong = new OnlineSong();
    public static int index;

    @Override
    public void onCreate(){
        super.onCreate();
        context = this;
    }

    public static void playMusic(String url){
        try{
            Uri myUri = Uri.parse(url);

            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();

            mediaPlayer.setDataSource(context, myUri);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare(); //don't use prepareAsync for mp3 playback
            mediaPlayer.start();

            Intent x = new Intent("OnlinePlayerActivity");
            x.putExtra("message", "play");
            context.sendBroadcast(x);

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    nextMusic();
                }
            });

        }catch (Exception e){

        }
    }

    public static void openPlayMusicPlayer(){
        try{
           currentSong = MyApplication.songList.get(MyApplication.index);
            String url = OnlineDefine.MUSIC_LINK+currentSong.getLink();
            MyApplication.playMusic(url);
        }catch (Exception e){

        }
    }

    public static void pauseMusic(){
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();

        Intent x = new Intent("OnlinePlayerActivity");
        x.putExtra("message", "play");
        context.sendBroadcast(x);
    }

    public static void nextMusic(){
        int musicListSize = songList.size();

        if(index >= musicListSize - 1){
            index = 0;
        } else {
            index++;
        }

        openPlayMusicPlayer();
    }

    public static void preMusic(){
        int musicListSize = songList.size();

        if(index <= 0){
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
//                if(mediaPlayer.isPlaying()){
//                    mediaPlayer.release();
//                    mediaPlayer = new MediaPlayer();
//                }
//
//                mediaPlayer.setDataSource(context, myUri);
//                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                mediaPlayer.prepare(); //don't use prepareAsync for mp3 playback
//                mediaPlayer.start();
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
