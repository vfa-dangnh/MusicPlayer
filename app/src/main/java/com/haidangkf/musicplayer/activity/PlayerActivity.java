package com.haidangkf.musicplayer.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.haidangkf.musicplayer.R;
import com.haidangkf.musicplayer.controls.Controls;
import com.haidangkf.musicplayer.controls.PlayerConstants;
import com.haidangkf.musicplayer.dto.Song;
import com.haidangkf.musicplayer.service.MyService;
import com.haidangkf.musicplayer.utils.Common;
import com.haidangkf.musicplayer.utils.SongUtil;

import butterknife.BindView;

public class PlayerActivity extends BaseActivity {

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
        Log.d(Common.TAG, "onCreate PlayerActivity");

        init();

        Intent intent = getIntent();
        if (intent.hasExtra("from")) { // from click on menu Play
            if (!Common.isServiceRunning(context, MyService.class)) {
                Common.startService(context, MyService.class);
            } else {
                // service is running, handle action change song
                PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            }
        } else {
            if (!Common.isServiceRunning(context, MyService.class)) {
                disablePlayerScreen();
                return;
            }
        }

        MainActivity.updateUIBottomBar(context);
        this.updateUIPlayer(context);

        // SeekBar change listener
        songProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mHandler.removeCallbacks(mUpdateTimeTask);
                int totalDuration = MyService.mp.getDuration();
                int currentDuration = songUtil.progressToTimer(progress, totalDuration);
                // Displaying Current Playing time
                songCurrentPosition.setText("" + songUtil.milliSecondsToTimer(currentDuration));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // remove message Handler from updating progress bar
                mHandler.removeCallbacks(mUpdateTimeTask);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTimeTask);
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
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnInfo = (ImageButton) findViewById(R.id.btnInfo);
        imgDisc = (ImageView) findViewById(R.id.imgDisc);
        songTitle = (TextView) findViewById(R.id.songTitle);

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
        registerReceiver(finishPlayerActivity, new IntentFilter("finishPlayerActivity"));
    }

    private void disablePlayerScreen() {
        btnInfo.setEnabled(false);
        btnPlay.setEnabled(false);
        btnForward.setEnabled(false);
        btnBackward.setEnabled(false);
        btnNext.setEnabled(false);
        btnPrevious.setEnabled(false);
        songProgressBar.setEnabled(false);
        imgDisc.clearAnimation();
        songTitle.setText("");
        songCurrentPosition.setText("");
        songTotalDuration.setText("");
    }

    private final BroadcastReceiver finishPlayerActivity = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    private void setClickEventsButtons() {
        /**
         * play previous song
         */
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Controls.previousControl(context);
            }
        });

        /**
         * play and pause current song
         */
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PlayerConstants.SONG_PAUSED) {
                    Controls.pauseControl(context);
                } else {
                    Controls.playControl(context);
                }
            }
        });

        /**
         * play next song
         */
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Controls.nextControl(context);
            }
        });

        /**
         * Forward button click event
         * Forwards song specified seconds
         */
        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // get current song position
                int currentPosition = MyService.mp.getCurrentPosition();
                // check if seekForward time is lesser than song duration
                if (currentPosition + seekForwardTime < MyService.mp.getDuration()) {
                    // forward song
                    MyService.mp.seekTo(currentPosition + seekForwardTime);
                } else {
                    // forward to end position
                    MyService.mp.seekTo(MyService.mp.getDuration());
                }
            }
        });

        /**
         * Backward button click event
         * Backward song to specified seconds
         */
        btnBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // get current song position
                int currentPosition = MyService.mp.getCurrentPosition();
                // check if seekBackward time is greater than 0 sec
                if (currentPosition - seekBackwardTime > 0) {
                    // forward song
                    MyService.mp.seekTo(currentPosition - seekBackwardTime);
                } else {
                    // backward to starting position
                    MyService.mp.seekTo(0);
                }

            }
        });

        /**
         * repeat mode
         */
        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (PlayerConstants.IS_REPEAT) {
                    PlayerConstants.IS_REPEAT = false;
                    Toast.makeText(context, "Repeat is OFF", Toast.LENGTH_SHORT).show();
                    btnRepeat.setImageResource(R.drawable.btn_repeat);
                } else {
                    PlayerConstants.IS_REPEAT = true;
                    Toast.makeText(context, "Repeat is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    PlayerConstants.IS_SHUFFLE = false;
                    btnRepeat.setImageResource(R.drawable.btn_repeat_focused);
                    btnShuffle.setImageResource(R.drawable.btn_shuffle);
                }
            }
        });

        /**
         * shuffle mode
         */
        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (PlayerConstants.IS_SHUFFLE) {
                    PlayerConstants.IS_SHUFFLE = false;
                    Toast.makeText(context, "Shuffle is OFF", Toast.LENGTH_SHORT).show();
                    btnShuffle.setImageResource(R.drawable.btn_shuffle);
                } else {
                    PlayerConstants.IS_SHUFFLE = true;
                    Toast.makeText(context, "Shuffle is ON", Toast.LENGTH_SHORT).show();
                    // make repeat to false
                    PlayerConstants.IS_REPEAT = false;
                    btnShuffle.setImageResource(R.drawable.btn_shuffle_focused);
                    btnRepeat.setImageResource(R.drawable.btn_repeat);
                }
            }
        });

        /**
         * show info of current song
         */
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String title = PlayerConstants.SONG_LIST.get(PlayerConstants.SONG_INDEX).getName();
                String message = PlayerConstants.SONG_LIST.get(PlayerConstants.SONG_INDEX).toString();
                Common.dialogConfirmOk(context, title, message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
            }
        });
    }

    /**
     * update UI for this player screen
     *
     * @param context
     */
    public static void updateUIPlayer(Context context) {
        if (Common.isServiceRunning(context, MyService.class)) {
            Song song = PlayerConstants.SONG_LIST.get(PlayerConstants.SONG_INDEX);
            songTitle.setText(song.getName());
            if (PlayerConstants.SONG_PAUSED) {
                imgDisc.clearAnimation();
                btnPlay.setImageResource(R.drawable.btn_play);
            } else {
                imgDisc.startAnimation(rotateAnim);
                btnPlay.setImageResource(R.drawable.btn_pause);
            }
        } else {

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
            long totalDuration = MyService.mp.getDuration();
            long currentDuration = MyService.mp.getCurrentPosition();

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
        Log.d(Common.TAG, "onResume PlayerActivity");
        super.onResume();
        if (Common.isServiceRunning(context, MyService.class)) {
            MainActivity.updateUIBottomBar(context);
            updateUIPlayer(context);
        } else {
            disablePlayerScreen();
        }
    }

    @Override
    public void onDestroy() {
        Log.d(Common.TAG, "onDestroy PlayerActivity");
        super.onDestroy();
        mHandler.removeCallbacks(mUpdateTimeTask);
        unregisterReceiver(finishPlayerActivity);
    }

}