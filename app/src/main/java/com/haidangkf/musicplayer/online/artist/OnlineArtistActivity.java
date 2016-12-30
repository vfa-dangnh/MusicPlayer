package com.haidangkf.musicplayer.online.artist;

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
import android.widget.GridView;

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

public class OnlineArtistActivity extends AppCompatActivity {

    GridView gridView;
    AdapterArtist adapter;
    ArrayList<OnlineArtist> artistList = new ArrayList<>();
    ProgressDialog myProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_artist);

        gridView = (GridView)findViewById(R.id.listArtist);

        adapter = new AdapterArtist(OnlineArtistActivity.this, R.layout.adapter_artist, artistList);
        gridView.setAdapter(adapter);
        adapter.setNotifyOnChange(true);

        // khai bao dialog cho qua trình cap nhat co so du lieu
        myProgress = new ProgressDialog(this);
        myProgress.setTitle("Data loading  ...");
        myProgress.setMessage("Please wait...");
        myProgress.setCancelable(false);

        getArtistList();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OnlineArtist artist = artistList.get(position);
                String artistId = artist.getId_artist();

                Bundle bundle = new Bundle();
                bundle.putString("key", OnlineDefine.GET_LIST_SONG_BY_ARTIST);
                bundle.putString("id", artistId);
                Intent intent = new Intent(OnlineArtistActivity.this, OnlineListMusicActivity.class);
                intent.putExtra("DATA", bundle);
                startActivity(intent);
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                String name = artistList.get(position).getArtist_name();

                Intent intent = new Intent(OnlineArtistActivity.this, ArtistInfo.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                intent.putExtra("DATA", bundle);
                startActivity(intent);

                return true;
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

    public void getArtistList() {

        // neu dien thoai chua ket noi internet thi hien thi thong bao
        if (!isOnline()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(OnlineArtistActivity.this);
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
        params.put("tag", OnlineDefine.GET_LIST_ARTIST);
        postToHost(params);

        myProgress.show();
    }

    public void postToHost(RequestParams params) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(OnlineArtistActivity.this, OnlineDefine.DOMAIN_INDEX, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = null; // for UTF-8 encoding
                try {
                    result = new String(responseBody, "UTF-8");
                    Log.i("kq", "result " + result);
                    String finalResult = removeUTF8BOM(result);

                    if(finalResult.startsWith("[")){
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<OnlineArtist>>() {}.getType();
                        List<OnlineArtist> list1 = (List<OnlineArtist>) gson.fromJson(finalResult, listType);

                        artistList.clear();
                        artistList.addAll(list1);
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
