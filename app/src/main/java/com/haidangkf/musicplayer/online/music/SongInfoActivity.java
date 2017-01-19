package com.haidangkf.musicplayer.online.music;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.haidangkf.musicplayer.R;
import com.haidangkf.musicplayer.activity.MyApplication;

public class SongInfoActivity extends AppCompatActivity {

    TextView txtSongName;
    TextView txtAlbum;
    TextView txtArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_info);

        txtSongName = (TextView) findViewById(R.id.songInfoName);
        txtAlbum = (TextView) findViewById(R.id.songInfoAlbum);
        txtArtist = (TextView) findViewById(R.id.songInfoArtist);

        txtSongName.setText("" + MyApplication.currentSong.getName());
        txtAlbum.setText("" + MyApplication.currentSong.getAlbum_name());
        txtArtist.setText("" + MyApplication.currentSong.getArtist_name());
    }
}
