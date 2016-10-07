package com.haidangkf.musicplayer.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.RemoteControlClient;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.haidangkf.musicplayer.R;
import com.haidangkf.musicplayer.activity.MainActivity;
import com.haidangkf.musicplayer.controls.Controls;
import com.haidangkf.musicplayer.controls.PlayerConstants;
import com.haidangkf.musicplayer.dto.Song;
import com.haidangkf.musicplayer.receiver.NotificationBroadcast;
import com.haidangkf.musicplayer.utils.Common;
import com.haidangkf.musicplayer.utils.SongUtil;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service implements AudioManager.OnAudioFocusChangeListener {

    public static MediaPlayer mp;
    int NOTIFICATION_ID = 1111;
    public static final String NOTIFY_PREVIOUS = "notify.previous";
    public static final String NOTIFY_DELETE = "notify.delete";
    public static final String NOTIFY_PAUSE = "notify.pause";
    public static final String NOTIFY_PLAY = "notify.play";
    public static final String NOTIFY_NEXT = "notify.next";

    private ComponentName remoteComponentName;
    private RemoteControlClient remoteControlClient;
    AudioManager audioManager;
    Bitmap mDummyAlbumArt;
    private static Timer timer;
    private static boolean currentVersionSupportBigNotification = false;
    private static boolean currentVersionSupportLockScreenControls = false;
    public static SongUtil songUtil;
    Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Common.TAG, "onCreate Service");

        context = this;
        mp = new MediaPlayer();
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        songUtil = new SongUtil(context);
        if (MainActivity.bottomBar != null) {
            MainActivity.bottomBar.setVisibility(View.VISIBLE);
        }

        currentVersionSupportBigNotification = Common.currentVersionSupportBigNotification();
        currentVersionSupportLockScreenControls = Common.currentVersionSupportLockScreenControls();
        timer = new Timer();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Controls.nextControl(getApplicationContext());
            }
        });
    }

    @SuppressLint("NewApi")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(Common.TAG, "onStartCommand Service");

        try {
            if (PlayerConstants.SONGS_LIST.size() <= 0) {
                PlayerConstants.SONGS_LIST = songUtil.getAllSongs();
            }
            Song data = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_INDEX);
            if (currentVersionSupportLockScreenControls) {
                RegisterRemoteClient();
            }
            String songPath = data.getPath();
            playSong(songPath, data);
            newNotification();

            PlayerConstants.SONG_CHANGE_HANDLER = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    String message = (String) msg.obj;
                    Song data = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_INDEX);
                    String songPath = data.getPath();
                    newNotification();
                    try {
                        playSong(songPath, data);
                        MainActivity.updateUIBottomBar(context);
//                        AudioPlayerActivity.changeUI();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d(Common.TAG, "TAG Pressed: " + message);
                    return false;
                }
            });

            PlayerConstants.PLAY_PAUSE_HANDLER = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    String message = (String) msg.obj;
                    if (mp == null)
                        return false;
                    if (message.equalsIgnoreCase(getResources().getString(R.string.play))) {
                        PlayerConstants.SONG_PAUSED = false;
                        if (currentVersionSupportLockScreenControls) {
                            remoteControlClient.setPlaybackState(RemoteControlClient.PLAYSTATE_PLAYING);
                        }
                        mp.start();
                    } else if (message.equalsIgnoreCase(getResources().getString(R.string.pause))) {
                        PlayerConstants.SONG_PAUSED = true;
                        if (currentVersionSupportLockScreenControls) {
                            remoteControlClient.setPlaybackState(RemoteControlClient.PLAYSTATE_PAUSED);
                        }
                        mp.pause();
                    }
                    newNotification();
                    // update UI
                    try {
                        MainActivity.updateUIBottomBar(context);
//                        AudioPlayerActivity.changeButton();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d(Common.TAG, "TAG Pressed: " + message);
                    return false;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }

    /**
     * Send message from timer
     *
     * @author jonty.ankit
     */
    private class MainTask extends TimerTask {
        public void run() {
            handler.sendEmptyMessage(0);
        }
    }

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mp != null) {
                int progress = (mp.getCurrentPosition() * 100) / mp.getDuration();
                Integer i[] = new Integer[3];
                i[0] = mp.getCurrentPosition();
                i[1] = mp.getDuration();
                i[2] = progress;
                try {
                    PlayerConstants.PROGRESSBAR_HANDLER.sendMessage(PlayerConstants.PROGRESSBAR_HANDLER.obtainMessage(0, i));
                } catch (Exception e) {
                }
            }
        }
    };

    /**
     * Notification
     * Custom BigNotification is available from API 16
     */
    @SuppressLint("NewApi")
    private void newNotification() {
        String songName = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_INDEX).getName();
        String albumName = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_INDEX).getAlbum();
        RemoteViews simpleContentView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification_custom);
        RemoteViews expandedView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification_big);

        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_music)
                .setContentTitle(songName).build();

        setListeners(simpleContentView);
        setListeners(expandedView);

        notification.contentView = simpleContentView;
        if (currentVersionSupportBigNotification) {
            notification.bigContentView = expandedView;
        }

        try {
            String albumId = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_INDEX).getAlbumId();
            Bitmap albumArt = songUtil.getAlbumart(getApplicationContext(), Long.parseLong(albumId));
            if (albumArt != null) {
                notification.contentView.setImageViewBitmap(R.id.imageViewAlbumArt, albumArt);
                if (currentVersionSupportBigNotification) {
                    notification.bigContentView.setImageViewBitmap(R.id.imageViewAlbumArt, albumArt);
                }
            } else {
                notification.contentView.setImageViewResource(R.id.imageViewAlbumArt, R.drawable.ic_disc);
                if (currentVersionSupportBigNotification) {
                    notification.bigContentView.setImageViewResource(R.id.imageViewAlbumArt, R.drawable.ic_disc);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (PlayerConstants.SONG_PAUSED) {
            notification.contentView.setViewVisibility(R.id.btnPause, View.GONE);
            notification.contentView.setViewVisibility(R.id.btnPlay, View.VISIBLE);

            if (currentVersionSupportBigNotification) {
                notification.bigContentView.setViewVisibility(R.id.btnPause, View.GONE);
                notification.bigContentView.setViewVisibility(R.id.btnPlay, View.VISIBLE);
            }
        } else {
            notification.contentView.setViewVisibility(R.id.btnPause, View.VISIBLE);
            notification.contentView.setViewVisibility(R.id.btnPlay, View.GONE);

            if (currentVersionSupportBigNotification) {
                notification.bigContentView.setViewVisibility(R.id.btnPause, View.VISIBLE);
                notification.bigContentView.setViewVisibility(R.id.btnPlay, View.GONE);
            }
        }

        notification.contentView.setTextViewText(R.id.textSongName, songName);
        notification.contentView.setTextViewText(R.id.textAlbumName, albumName);
        if (currentVersionSupportBigNotification) {
            notification.bigContentView.setTextViewText(R.id.textSongName, songName);
            notification.bigContentView.setTextViewText(R.id.textAlbumName, albumName);
        }
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        startForeground(NOTIFICATION_ID, notification);
    }

    /**
     * Notification click listeners
     *
     * @param view
     */
    public void setListeners(RemoteViews view) {
        Intent previous = new Intent(NOTIFY_PREVIOUS);
        Intent delete = new Intent(NOTIFY_DELETE);
        Intent pause = new Intent(NOTIFY_PAUSE);
        Intent next = new Intent(NOTIFY_NEXT);
        Intent play = new Intent(NOTIFY_PLAY);

        PendingIntent pPrevious = PendingIntent.getBroadcast(getApplicationContext(), 0, previous, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnPrevious, pPrevious);

        PendingIntent pDelete = PendingIntent.getBroadcast(getApplicationContext(), 0, delete, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnDelete, pDelete);

        PendingIntent pPause = PendingIntent.getBroadcast(getApplicationContext(), 0, pause, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnPause, pPause);

        PendingIntent pNext = PendingIntent.getBroadcast(getApplicationContext(), 0, next, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnNext, pNext);

        PendingIntent pPlay = PendingIntent.getBroadcast(getApplicationContext(), 0, play, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnPlay, pPlay);
    }

    /**
     * Play song, Update Lock screen fields
     *
     * @param songPath
     * @param data
     */
    @SuppressLint("NewApi")
    private void playSong(String songPath, Song data) {
        try {
            if (currentVersionSupportLockScreenControls) {
                UpdateMetadata(data);
                remoteControlClient.setPlaybackState(RemoteControlClient.PLAYSTATE_PLAYING);
            }
            mp.reset();
            mp.setDataSource(songPath);
            mp.prepare();
            mp.start();
            timer.scheduleAtFixedRate(new MainTask(), 0, 100);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // For Lock Screen controls
    @SuppressLint("NewApi")
    private void RegisterRemoteClient() {
        remoteComponentName = new ComponentName(getApplicationContext(), new NotificationBroadcast().ComponentName());
        try {
            if (remoteControlClient == null) {
                audioManager.registerMediaButtonEventReceiver(remoteComponentName);
                Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
                mediaButtonIntent.setComponent(remoteComponentName);
                PendingIntent mediaPendingIntent = PendingIntent.getBroadcast(this, 0, mediaButtonIntent, 0);
                remoteControlClient = new RemoteControlClient(mediaPendingIntent);
                audioManager.registerRemoteControlClient(remoteControlClient);
            }
            remoteControlClient.setTransportControlFlags(
                    RemoteControlClient.FLAG_KEY_MEDIA_PLAY |
                            RemoteControlClient.FLAG_KEY_MEDIA_PAUSE |
                            RemoteControlClient.FLAG_KEY_MEDIA_PLAY_PAUSE |
                            RemoteControlClient.FLAG_KEY_MEDIA_STOP |
                            RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS |
                            RemoteControlClient.FLAG_KEY_MEDIA_NEXT);
        } catch (Exception ex) {
        }
    }

    // For Updating Lock Screen UI
    @SuppressLint("NewApi")
    private void UpdateMetadata(Song data) {
        if (remoteControlClient == null)
            return;
        RemoteControlClient.MetadataEditor metadataEditor = remoteControlClient.editMetadata(true);
        metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_ALBUM, data.getAlbum());
        metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_ARTIST, data.getArtist());
        metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_TITLE, data.getName());
        mDummyAlbumArt = songUtil.getAlbumart(getApplicationContext(), Long.parseLong(data.getAlbumId()));
        if (mDummyAlbumArt == null) {
            mDummyAlbumArt = BitmapFactory.decodeResource(getResources(), R.drawable.ic_disc);
        }
        metadataEditor.putBitmap(RemoteControlClient.MetadataEditor.BITMAP_KEY_ARTWORK, mDummyAlbumArt);
        metadataEditor.apply();
        audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(Common.TAG, "onBind Service");
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Common.TAG, "onDestroy Service");
        if (mp != null) {
            mp.release();
            mp = null;
        }
        if (MainActivity.bottomBar != null) {
            MainActivity.bottomBar.setVisibility(View.GONE);
        }
    }

}
