package com.haidangkf.musicplayer.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.haidangkf.musicplayer.R;
import com.haidangkf.musicplayer.utils.MyApplication;
import com.haidangkf.musicplayer.utils.TimerUtil;

public class FragmentPlay extends BaseFragment {

    public static TextView tvSongInfo;
    static TextView tvCurrentTime;
    static SeekBar seekBar;
    static TextView tvTotalTime;
    ImageButton btnRepeat;
    ImageButton btnBackward;
    ImageButton btnPlay;
    ImageButton btnForward;
    ImageButton btnShuffle;

    // cap nhat thoi gian phat nhac len seekBar
    static Handler mHandler;
    static TimerUtil timerUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragment_play, container, false);

        tvSongInfo = (TextView) view.findViewById(R.id.tvSongInfo);
        tvCurrentTime = (TextView) view.findViewById(R.id.tvCurrentTime);
        seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        tvTotalTime = (TextView) view.findViewById(R.id.tvTotalTime);
        btnRepeat = (ImageButton) view.findViewById(R.id.btnRepeat);
        btnBackward = (ImageButton) view.findViewById(R.id.btnBackward);
        btnPlay = (ImageButton) view.findViewById(R.id.btnPlay);
        btnForward = (ImageButton) view.findViewById(R.id.btnForward);
        btnShuffle = (ImageButton) view.findViewById(R.id.btnShuffle);

        MyApplication.displaySongInfo();

        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.isRepeat == 0) { // currently no repeat
                    MyApplication.isRepeat = 1;
                    btnRepeat.setBackgroundResource(R.drawable.repeat_one_song);
                    Toast.makeText(getActivity(), "Repeating current song", Toast.LENGTH_SHORT).show();
                } else if (MyApplication.isRepeat == 1) { // currently repeat one song
                    MyApplication.isRepeat = 2;
                    btnRepeat.setBackgroundResource(R.drawable.repeat_all_songs);
                    Toast.makeText(getActivity(), "Repeating all songs", Toast.LENGTH_SHORT).show();
                } else { // currently repeat all songs
                    MyApplication.isRepeat = 0;
                    btnRepeat.setBackgroundResource(R.drawable.repeat_off);
                    Toast.makeText(getActivity(), "Repeat is off", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.isShuffle) {
                    MyApplication.isShuffle = false;
                    btnShuffle.setBackgroundResource(R.drawable.shuffle_off);
                    Toast.makeText(getActivity(), "Shuffle is off", Toast.LENGTH_SHORT).show();
                } else {
                    MyApplication.isShuffle = true;
                    btnShuffle.setBackgroundResource(R.drawable.shuffle_on);
                    Toast.makeText(getActivity(), "Shuffle is on", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.pauseMusic();

                if (MyApplication.isPlaying) {
                    btnPlay.setBackgroundResource(R.drawable.pause);
                } else {
                    btnPlay.setBackgroundResource(R.drawable.play);
                }
            }
        });

        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.nextSong();
            }
        });

        btnBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.previousSong();
            }
        });

        mHandler = new Handler();

        // set seek bar values
        seekBar.setProgress(0);
        seekBar.setMax(100);

        timerUtil = new TimerUtil();

        // cập nhật thời gian bản nhạc đang phát
        updateProgressBar();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mHandler.removeCallbacks(mUpdateTimeTask);
                int totalDuration = MyApplication.mediaPlayer.getDuration();
                long currentDuration = timerUtil.progressToTimer(progress, totalDuration);
                // Displaying Current Playing time
                tvCurrentTime.setText("" + timerUtil.milliSecondsToTimer(currentDuration));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTimeTask);
                int totalDuration = MyApplication.mediaPlayer.getDuration();
                int currentPosition = timerUtil.progressToTimer(seekBar.getProgress(), totalDuration);

                // forward or backward to certain seconds
                MyApplication.mediaPlayer.seekTo(currentPosition);

                // update timer progress again
                updateProgressBar();
            }
        });

        return view;
    }

    public static void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    public static Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = MyApplication.mediaPlayer.getDuration();
            long currentDuration = MyApplication.mediaPlayer.getCurrentPosition();

            // Displaying Total Duration time
            tvTotalTime.setText("" + timerUtil.milliSecondsToTimer(totalDuration));
            // Displaying Current Playing time
            tvCurrentTime.setText("" + timerUtil.milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = (int) (timerUtil.getProgressPercentage(currentDuration, totalDuration));
            // Log.d("Progress", "" + progress);
            seekBar.setProgress(progress);

//            // Running this thread after 100 milliseconds
//            mHandler.postDelayed(this, 100); // "this" imply Runnable mUpdateTimeTask
            updateProgressBar();
        }
    };

}