package com.haidangkf.musicplayer.online.music;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.haidangkf.musicplayer.R;
import com.haidangkf.musicplayer.activity.MyApplication;
import com.haidangkf.musicplayer.online.OnlineDefine;
import com.haidangkf.musicplayer.utils.Common;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

public class ActivityLyrics extends AppCompatActivity {

    TextView txtLyrics;
    ProgressDialog myProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics);

        txtLyrics = (TextView) findViewById(R.id.txtLyrics);
        // dialog for updating database
        myProgress = new ProgressDialog(this);
        myProgress.setTitle("Data loading...");
        myProgress.setMessage("Please wait...");
        myProgress.setCancelable(false);

        getSongLyricsById();
    }

    // check Internet connection
    public boolean isOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void getSongLyricsById() {
        // show error message if not connected to Internet
        if (!isOnline()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityLyrics.this);
            dialog.setTitle("Error!");
            dialog.setMessage("Please check your Internet connection");
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            dialog.show();

            return;
        }

        RequestParams params = new RequestParams();
        params.put("tag", OnlineDefine.GET_LYRICS_SONG);
        params.put("id", MyApplication.currentSong.getId_music());
        postToHost(params);
        myProgress.show();
    }

    public void postToHost(RequestParams params) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(ActivityLyrics.this, OnlineDefine.DOMAIN_INDEX, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = null;
                try {
                    result = new String(responseBody, "UTF-8"); // for UTF-8 encoding
                    txtLyrics.setText("" + result);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    myProgress.cancel();
                }

                myProgress.cancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d(Common.TAG, "error --> " + error);
                myProgress.cancel();
            }
        });
    }

    private static String removeUTF8BOM(String s) {
        while (!s.startsWith("[") && s.length() > 0) {
            s = s.substring(1);
        }
        return s;
    }
}