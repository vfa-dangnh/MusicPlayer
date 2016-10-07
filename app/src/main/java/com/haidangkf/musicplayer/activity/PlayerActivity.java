package com.haidangkf.musicplayer.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.haidangkf.musicplayer.R;
import com.haidangkf.musicplayer.controls.Controls;
import com.haidangkf.musicplayer.controls.PlayerConstants;
import com.haidangkf.musicplayer.dto.Song;
import com.haidangkf.musicplayer.service.MyService;
import com.haidangkf.musicplayer.utils.Common;
import com.haidangkf.musicplayer.utils.SongUtil;

import java.util.ArrayList;

import butterknife.BindView;

public class PlayerActivity extends BaseActivity {

    @BindView(R.id.btnPlay)
    ImageButton btnPlay;
    @BindView(R.id.btnForward)
    ImageButton btnForward;
    @BindView(R.id.btnBackward)
    ImageButton btnBackward;
    @BindView(R.id.btnNext)
    ImageButton btnNext;
    @BindView(R.id.btnPrevious)
    ImageButton btnPrevious;
    @BindView(R.id.btnInfo)
    ImageButton btnInfo;
    @BindView(R.id.btnRepeat)
    ImageButton btnRepeat;
    @BindView(R.id.btnShuffle)
    ImageButton btnShuffle;
    @BindView(R.id.songProgressBar)
    SeekBar songProgressBar;
    @BindView(R.id.songTitle)
    TextView songTitleLabel;
    @BindView(R.id.songCurrentDurationLabel)
    TextView songCurrentDurationLabel;
    @BindView(R.id.songTotalDurationLabel)
    TextView songTotalDurationLabel;
    @BindView(R.id.imgDisc)
    ImageView imgDisc;

    private Handler mHandler = new Handler(); // to update UI timer, progress bar...
    private SongUtil songUtil;
    private int seekForwardTime = 5000; // milliseconds
    private int seekBackwardTime = 5000; // milliseconds
    private int currentSongIndex = -1;
    private boolean isShuffle = false;
    private boolean isRepeat = false;
    private Animation rotateAnim;
    private ArrayList<Song> songsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_player);


        init();

        if (!Common.isServiceRunning(context, MyService.class)) {
            Common.startService(context, MyService.class);
        } else {
            // service is running, handle action change song
            PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
        }
        MainActivity.updateUIBottomBar(context);


        // SeekBar change listener
        songProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mHandler.removeCallbacks(mUpdateTimeTask);
                int totalDuration = MyService.mp.getDuration();
                int currentDuration = songUtil.progressToTimer(progress, totalDuration);
                // Displaying Current Playing time
                songCurrentDurationLabel.setText("" + songUtil.milliSecondsToTimer(currentDuration));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // remove message Handler from updating progress bar
                mHandler.removeCallbacks(mUpdateTimeTask);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int totalDuration = MyService.mp.getDuration();
                int currentDuration = songUtil.progressToTimer(seekBar.getProgress(), totalDuration);

                // forward or backward to certain seconds
                MyService.mp.seekTo(currentDuration);

                // update timer progress again
                updateProgressBar();
            }
        });
    }

    private void init() {
        setClickEventsButtons();
        songUtil = new SongUtil(context);
        rotateAnim = AnimationUtils.loadAnimation(context, R.anim.rotate);
        PlayerConstants.PROGRESSBAR_HANDLER = new Handler(){
            @Override
            public void handleMessage(Message msg){
                Integer i[] = (Integer[])msg.obj;
                songCurrentDurationLabel.setText(songUtil.milliSecondsToTimer(i[0]));
                songTotalDurationLabel.setText(songUtil.milliSecondsToTimer(i[1]));
            }
        };

        songsList = PlayerConstants.SONGS_LIST;
        currentSongIndex = PlayerConstants.SONG_INDEX;
    }

    private void setClickEventsButtons() {
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Controls.previousControl(context);
                MainActivity.updateUIBottomBar(context);
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PlayerConstants.SONG_PAUSED) {
                    Controls.pauseControl(context);
                } else {
                    Controls.playControl(context);
                }
                MainActivity.updateUIBottomBar(context);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Controls.nextControl(context);
                MainActivity.updateUIBottomBar(context);
            }
        });
    }

    /**
     * Update timer on seekbar
     */
    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = MyService.mp.getDuration();
            long currentDuration = MyService.mp.getCurrentPosition();

            // Displaying Total Duration time
            songTotalDurationLabel.setText("" + songUtil.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            songCurrentDurationLabel.setText("" + songUtil.milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = songUtil.getProgressPercentage(currentDuration, totalDuration);
            songProgressBar.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

}