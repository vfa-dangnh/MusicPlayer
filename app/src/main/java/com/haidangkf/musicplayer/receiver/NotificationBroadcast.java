package com.haidangkf.musicplayer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

import com.haidangkf.musicplayer.controls.Controls;
import com.haidangkf.musicplayer.controls.PlayerConstants;
import com.haidangkf.musicplayer.service.MyService;
import com.haidangkf.musicplayer.utils.Common;

public class NotificationBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_MEDIA_BUTTON)) {
            KeyEvent keyEvent = (KeyEvent) intent.getExtras().get(Intent.EXTRA_KEY_EVENT);
            if (keyEvent.getAction() != KeyEvent.ACTION_DOWN)
                return;

            switch (keyEvent.getKeyCode()) {
                case KeyEvent.KEYCODE_HEADSETHOOK:
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                    if (!PlayerConstants.SONG_PAUSED) {
                        Controls.pauseControl(context);
                    } else {
                        Controls.playControl(context);
                    }
                    break;
                case KeyEvent.KEYCODE_MEDIA_PLAY:
                    break;
                case KeyEvent.KEYCODE_MEDIA_PAUSE:
                    break;
                case KeyEvent.KEYCODE_MEDIA_STOP:
                    break;
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                    Log.d(Common.TAG, "TAG: KEYCODE_MEDIA_NEXT");
                    Controls.nextControl(context);
                    break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                    Log.d(Common.TAG, "TAG: KEYCODE_MEDIA_PREVIOUS");
                    Controls.previousControl(context);
                    break;
            }
        } else {
            if (intent.getAction().equals(MyService.NOTIFY_PLAY)) {
                Controls.playControl(context);
            } else if (intent.getAction().equals(MyService.NOTIFY_PAUSE)) {
                Controls.pauseControl(context);
            } else if (intent.getAction().equals(MyService.NOTIFY_NEXT)) {
                Controls.nextControl(context);
            } else if (intent.getAction().equals(MyService.NOTIFY_PREVIOUS)) {
                Controls.previousControl(context);
            } else if (intent.getAction().equals(MyService.NOTIFY_DELETE)) {
                Common.stopService(context,MyService.class);
            }
        }
    }

    public String ComponentName() {
        return this.getClass().getName();
    }
}
