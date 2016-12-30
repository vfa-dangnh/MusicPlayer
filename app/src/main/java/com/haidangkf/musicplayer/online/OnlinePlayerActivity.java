package com.haidangkf.musicplayer.online;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.haidangkf.musicplayer.R;
import com.haidangkf.musicplayer.activity.BaseActivity;
import com.haidangkf.musicplayer.activity.MyApplication;
import com.haidangkf.musicplayer.controls.PlayerConstants;
import com.haidangkf.musicplayer.online.artist.ArtistInfo;
import com.haidangkf.musicplayer.online.music.ActivityLyrics;
import com.haidangkf.musicplayer.online.music.SongInfoActivity;
import com.haidangkf.musicplayer.utils.Common;
import com.haidangkf.musicplayer.utils.SongUtil;

import butterknife.BindView;

public class OnlinePlayerActivity extends BaseActivity {

    @BindView(R.id.btnForward)
    ImageButton btnForward;
    @BindView(R.id.btnBackward)
    ImageButton btnBackward;
    @BindView(R.id.btnNext)
    ImageButton btnNext;
    @BindView(R.id.btnPrevious)
    ImageButton btnPrevious;
    @BindView(R.id.btnRepeat)
    ImageButton btnRepeat;
    @BindView(R.id.btnShuffle)
    ImageButton btnShuffle;
    @BindView(R.id.songProgressBar)
    SeekBar songProgressBar;
    @BindView(R.id.songCurrentPosition)
    TextView songCurrentPosition;
    @BindView(R.id.songTotalDuration)
    TextView songTotalDuration;

    public static ImageButton btnInfo;
    public static ImageButton btnPlay;
    public static ImageView imgDisc;
    public static TextView songTitle;
    public static Animation rotateAnim;

    private Handler mHandler = new Handler(); // to update UI timer, progress bar...
    private SongUtil songUtil;
    private int seekForwardTime = 5000; // milliseconds
    private int seekBackwardTime = 5000; // milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_player);

        setTitle("Playing Media");

        init();

        MyApplication.openPlayMusicPlayer();

//        Intent intent = getIntent();
//        if (intent.hasExtra("from")) { // from click on menu Play
//            if (!Common.isServiceRunning(context, MyService.class)) {
//                Common.startService(context, MyService.class);
//            } else {
//                // service is running, handle action change song
//                PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
//            }
//        } else {
//            if (!Common.isServiceRunning(context, MyService.class)) {
//                disablePlayerScreen();
//                return;
//            }
//        }

//        MainActivity.updateUIBottomBar(context);
        this.updateUIPlayer();
        updateProgressBar();
        // SeekBar change listener
        songProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mHandler.removeCallbacks(mUpdateTimeTask);
                int totalDuration = MyApplication.mediaPlayer.getDuration();
                int currentDuration = songUtil.progressToTimer(progress, totalDuration);
                // Displaying Current Playing time
                songCurrentPosition.setText("" + songUtil.milliSecondsToTimer(currentDuration));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                mHandler.removeCallbacks(mUpdateTimeTask);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTimeTask);
                int totalDuration = MyApplication.mediaPlayer.getDuration();
                int currentDuration = songUtil.progressToTimer(seekBar.getProgress(), totalDuration);

                // forward or backward to certain seconds
                MyApplication.mediaPlayer.seekTo(currentDuration);

                // update timer progress again
                updateProgressBar();
            }
        });
    }

    private void init() {
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnInfo = (ImageButton) findViewById(R.id.btnInfo);
        imgDisc = (ImageView) findViewById(R.id.imgDisc);
        songTitle = (TextView) findViewById(R.id.songTitle);

        btnShuffle.setBackgroundResource(R.drawable.lyrics);
        btnRepeat.setBackgroundResource(R.drawable.singer_icon);

        songUtil = new SongUtil(context);
        rotateAnim = AnimationUtils.loadAnimation(context, R.anim.rotate);
        setClickEventsButtons();

        PlayerConstants.PROGRESSBAR_HANDLER = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Integer i[] = (Integer[]) msg.obj;
                songCurrentPosition.setText(songUtil.milliSecondsToTimer(i[0]));
                songTotalDuration.setText(songUtil.milliSecondsToTimer(i[1]));
            }
        };

        // set Progress bar values
        songProgressBar.setProgress(0);
        songProgressBar.setMax(100);

        // register receiver
        registerReceiver(finishPlayerActivity, new IntentFilter("OnlinePlayerActivity"));
    }


    private final BroadcastReceiver finishPlayerActivity = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try{
                updateUIPlayer();

            }catch (Exception e){

            }

        }
    };

    private void setClickEventsButtons() {
        /**
         * play previous song
         */
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.preMusic();

            }
        });

        /**
         * play and pause current song
         */
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.pauseMusic();

            }
        });

        /**
         * play next song
         */
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.nextMusic();

            }
        });

        /**
         * Forward button click event
         * Forwards song specified seconds
         */
        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

            }
        });

        /**
         * Backward button click event
         * Backward song to specified seconds
         */
        btnBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

            }
        });

        /**
         * repeat mode
         */
        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // hien thi thong tin ca si
                Intent intent = new Intent(OnlinePlayerActivity.this, ArtistInfo.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", MyApplication.currentSong.getArtist_name());
                intent.putExtra("DATA", bundle);
                startActivity(intent);
            }
        });

        /**
         * shuffle mode
         */
        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // hien thi loi bai hat
                startActivity(new Intent(OnlinePlayerActivity.this, ActivityLyrics.class));
            }
        });

        /**
         * show info of current song
         */
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                startActivity(new Intent(OnlinePlayerActivity.this, SongInfoActivity.class));
            }
        });
    }


    public static void updateUIPlayer() {
        songTitle.setText(MyApplication.currentSong.getName());
        if (MyApplication.mediaPlayer.isPlaying()) {
            imgDisc.startAnimation(rotateAnim);
            btnPlay.setImageResource(R.drawable.btn_pause);
        } else {
            imgDisc.clearAnimation();
            btnPlay.setImageResource(R.drawable.btn_play);
        }
    }

    /**
     * Update timer on SeekBar
     */
    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = MyApplication.mediaPlayer.getDuration();
            long currentDuration = MyApplication.mediaPlayer.getCurrentPosition();

            // Displaying Total Duration time
            songTotalDuration.setText("" + songUtil.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            songCurrentPosition.setText("" + songUtil.milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = songUtil.getProgressPercentage(currentDuration, totalDuration);
            songProgressBar.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
//        if (Common.isServiceRunning(context, MyService.class)) {
//            MainActivity.updateUIBottomBar(context);
//            updateUIPlayer(context);
//        } else {
//            disablePlayerScreen();
//        }
    }

    @Override
    public void onDestroy() {
        Log.d(Common.TAG, "onDestroy PlayerActivity");
        super.onDestroy();
        mHandler.removeCallbacks(mUpdateTimeTask);
        unregisterReceiver(finishPlayerActivity);
    }

}
