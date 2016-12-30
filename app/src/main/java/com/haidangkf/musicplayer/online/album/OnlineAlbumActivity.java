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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class OnlineAlbumActivity extends AppCompatActivity {

    ListView onlineAlbumListViet;
    AdapterOnlineAlbum adapter;
    ArrayList<OnlineAlbum> albumList = new ArrayList<>();
    ProgressDialog myProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_album);

        onlineAlbumListViet = (ListView)findViewById(R.id.onlineAlbumListViet);
        adapter = new AdapterOnlineAlbum(OnlineAlbumActivity.this, R.layout.adapter_online_album, albumList);
        onlineAlbumListViet.setAdapter(adapter);
        adapter.setNotifyOnChange(true);

        // khai bao dialog cho qua trình cap nhat co so du lieu
        myProgress = new ProgressDialog(this);
        myProgress.setTitle("Data loading  ...");
        myProgress.setMessage("Please wait...");
        myProgress.setCancelable(false);

        getAlbumList();

        onlineAlbumListViet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    // kiem tra dien thoai co ket noi internet hay khong
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void getAlbumList() {

        // neu dien thoai chua ket noi internet thi hien thi thong bao
        if (!isOnline()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(OnlineAlbumActivity.this);
            dialog.setTitle("Error!!");
            dialog.setMessage("Please check your internet connection");
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
                String result = null; // for UTF-8 encoding
                try {
                    result = new String(responseBody, "UTF-8");
                    Log.i("kq", "result " + result);
                    String finalResult = removeUTF8BOM(result);

                    if(finalResult.startsWith("[")){
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<OnlineAlbum>>() {}.getType();
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
                Log.i("upload", "error --> " + error);
                myProgress.cancel();
            }
        });
    }

    private static String removeUTF8BOM(String s) {
        while( !s.startsWith("[")  && s.length() > 0){
            s = s.substring(1);
        }
        return s;
    }
}
