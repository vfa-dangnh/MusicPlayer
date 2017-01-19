package com.haidangkf.musicplayer.online;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.haidangkf.musicplayer.R;
import com.haidangkf.musicplayer.online.album.OnlineAlbumActivity;
import com.haidangkf.musicplayer.online.artist.OnlineArtistActivity;
import com.haidangkf.musicplayer.online.search.OnlineSearch;

public class OnlineMainActivity extends AppCompatActivity {

    ImageButton btnAlbum;
    ImageButton btnArtist;
    ImageButton btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_main);

        btnAlbum = (ImageButton) findViewById(R.id.onlineMainAlbum);
        btnArtist = (ImageButton) findViewById(R.id.onlineMainArtist);
        btnSearch = (ImageButton) findViewById(R.id.onlineMainSearch);

        btnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OnlineMainActivity.this, OnlineAlbumActivity.class));
            }
        });

        btnArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OnlineMainActivity.this, OnlineArtistActivity.class));
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OnlineMainActivity.this, OnlineSearch.class));
            }
        });
    }
}
