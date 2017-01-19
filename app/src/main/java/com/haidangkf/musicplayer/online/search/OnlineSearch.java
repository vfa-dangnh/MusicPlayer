package com.haidangkf.musicplayer.online.search;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haidangkf.musicplayer.R;
import com.haidangkf.musicplayer.activity.MyApplication;
import com.haidangkf.musicplayer.online.OnlineDefine;
import com.haidangkf.musicplayer.online.OnlinePlayerActivity;
import com.haidangkf.musicplayer.online.music.AdapterOnlineMusic;
import com.haidangkf.musicplayer.online.music.OnlineSong;
import com.haidangkf.musicplayer.utils.Common;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class OnlineSearch extends AppCompatActivity {

    EditText editName;
    ImageView imgSearch;
    ListView listView;
    ProgressDialog myProgress;
    AdapterOnlineMusic adapter;
    ArrayList<OnlineSong> mySongList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_search);

        editName = (EditText) findViewById(R.id.editTextOnlineSearch);
        imgSearch = (ImageView) findViewById(R.id.imgOnlineSearch);
        listView = (ListView) findViewById(R.id.lvOnlineSearch);

        adapter = new AdapterOnlineMusic(OnlineSearch.this, R.layout.adapter_online_music_list, mySongList);
        listView.setAdapter(adapter);
        adapter.setNotifyOnChange(true);

        // dialog for updating database
        myProgress = new ProgressDialog(this);
        myProgress.setTitle("Data loading...");
        myProgress.setMessage("Please wait...");
        myProgress.setCancelable(false);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyApplication.songList.clear();
                MyApplication.songList.addAll(mySongList);
                MyApplication.index = position;
                startActivity(new Intent(OnlineSearch.this, OnlinePlayerActivity.class));
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString();
                if (!name.isEmpty()) {
                    MyApplication.songList.clear();
                    adapter.notifyDataSetChanged();
                    getMusicList(name);
                }
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

    public void getMusicList(String name) {
        // show error message if not connected to Internet
        if (!isOnline()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(OnlineSearch.this);
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
        params.put("tag", OnlineDefine.SEARCH_MUSIC);
        params.put("musicName", name);
        postToHost(params);
        myProgress.show();
    }

    public void postToHost(RequestParams params) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(OnlineSearch.this, OnlineDefine.DOMAIN_INDEX, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = null;
                try {
                    result = new String(responseBody, "UTF-8"); // for UTF-8 encoding
                    Log.d(Common.TAG, "post result: " + result);
                    String finalResult = removeUTF8BOM(result);

                    if (finalResult.startsWith("[")) { // first character of Json string
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<OnlineSong>>() {
                        }.getType();
                        List<OnlineSong> list1 = (List<OnlineSong>) gson.fromJson(finalResult, listType);

                        mySongList.clear();
                        mySongList.addAll(list1);
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
