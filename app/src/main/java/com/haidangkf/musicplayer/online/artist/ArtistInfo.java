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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haidangkf.musicplayer.R;
import com.haidangkf.musicplayer.online.OnlineDefine;
import com.haidangkf.musicplayer.utils.Common;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.haidangkf.musicplayer.activity.MyApplication.context;

public class ArtistInfo extends AppCompatActivity {

    ImageView imgArtist;
    TextView txtInfo;
    ProgressDialog myProgress;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_info);

        imgArtist = (ImageView) findViewById(R.id.imgArtistInfo);
        txtInfo = (TextView) findViewById(R.id.txtArtistInfo);

        // dialog for updating database
        myProgress = new ProgressDialog(this);
        myProgress.setTitle("Data loading...");
        myProgress.setMessage("Please wait...");
        myProgress.setCancelable(false);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("DATA");
        name = bundle.getString("name");

        getArtistList();
    }

    // check Internet connection
    public boolean isOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void getArtistList() {
        // show error message if not connected to Internet
        if (!isOnline()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(ArtistInfo.this);
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
        params.put("tag", OnlineDefine.GET_ARTIST_INFO);
        params.put("name", name);
        postToHost(params);

        myProgress.show();
    }

    public void postToHost(RequestParams params) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(ArtistInfo.this, OnlineDefine.DOMAIN_INDEX, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = null;
                try {
                    result = new String(responseBody, "UTF-8"); // for UTF-8 encoding
                    Log.d(Common.TAG, "post result: " + result);
                    String finalResult = removeUTF8BOM(result);

                    if (finalResult.startsWith("[")) { // first character of Json string
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<OnlineArtist>>() {
                        }.getType();
                        List<OnlineArtist> list1 = (List<OnlineArtist>) gson.fromJson(finalResult, listType);

                        if (list1.size() > 0) {
                            String info = list1.get(0).getArtist_info();
                            String img = list1.get(0).getArtist_img_link();
                            txtInfo.setText(info);
                            String fullLinkImage = OnlineDefine.IMG_ARTIST_LINK + img;
                            Picasso.with(context).load(fullLinkImage).into(imgArtist);
                        }
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
