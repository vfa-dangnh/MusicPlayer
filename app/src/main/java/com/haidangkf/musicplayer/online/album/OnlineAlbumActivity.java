package com.haidangkf.musicplayer.online.album;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haidangkf.musicplayer.R;
import com.haidangkf.musicplayer.online.OnlineDefine;
import com.haidangkf.musicplayer.online.music.OnlineListMusicActivity;
import com.haidangkf.musicplayer.utils.Common;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class OnlineAlbumActivity extends AppCompatActivity {

    ListView onlineAlbumListView;
    AdapterOnlineAlbum adapter;
    ArrayList<OnlineAlbum> albumList = new ArrayList<>();
    ProgressDialog myProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_album);

        onlineAlbumListView = (ListView) findViewById(R.id.onlineAlbumListView);
        adapter = new AdapterOnlineAlbum(OnlineAlbumActivity.this, R.layout.adapter_online_album, albumList);
        onlineAlbumListView.setAdapter(adapter);
        adapter.setNotifyOnChange(true);

        // dialog for updating database
        myProgress = new ProgressDialog(this);
        myProgress.setTitle("Data loading...");
        myProgress.setMessage("Please wait...");
        myProgress.setCancelable(false);

        getAlbumList();

        onlineAlbumListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OnlineAlbum album = albumList.get(position);
                String albumId = album.getId_album();

                Bundle bundle = new Bundle();
                bundle.putString("key", OnlineDefine.GET_LIST_SONG_BY_ALBUM);
                bundle.putString("id", albumId);
                Intent intent = new Intent(OnlineAlbumActivity.this, OnlineListMusicActivity.class);
                intent.putExtra("DATA", bundle);
                startActivity(intent);
            }
        });
    }

    // check Internet connection
    public boolean isOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void getAlbumList() {
        // show error message if not connected to Internet
        if (!isOnline()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(OnlineAlbumActivity.this);
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
        params.put("tag", OnlineDefine.GET_LIST_ALBUM);
        postToHost(params);
        myProgress.show();
    }

    public void postToHost(RequestParams params) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(OnlineAlbumActivity.this, OnlineDefine.DOMAIN_INDEX, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = null;
                try {
                    result = new String(responseBody, "UTF-8"); // for UTF-8 encoding
                    Log.d(Common.TAG, "post result: " + result);
                    String finalResult = removeUTF8BOM(result);

                    if (finalResult.startsWith("[")) { // first character of Json string
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<OnlineAlbum>>() {
                        }.getType();
                        List<OnlineAlbum> list1 = (List<OnlineAlbum>) gson.fromJson(finalResult, listType);

                        albumList.clear();
                        albumList.addAll(list1);
                        adapter.notifyDataSetChanged();
                    }
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
