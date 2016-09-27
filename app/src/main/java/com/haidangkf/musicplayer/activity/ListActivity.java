package com.haidangkf.musicplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.haidangkf.musicplayer.R;
import com.haidangkf.musicplayer.adapter.AdapterList;
import com.haidangkf.musicplayer.dto.Song;
import com.haidangkf.musicplayer.utils.MyApplication;

import java.util.ArrayList;

public class ListActivity extends BaseActivity {

    ArrayList<Song> songList = new ArrayList<>();
    AdapterList adapterList;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_fragment_list);

        listView = (ListView) findViewById(R.id.listView);

        Intent i = getIntent();
        Bundle b = i.getBundleExtra("bundle");
        String name = b.getString("name");
        String type = b.getString("type");

        if (type.equals("album")) {
            songList = MyApplication.getSongListForAlbum(this, name);
        } else {
            songList = MyApplication.getSongListForArtist(this, name);
        }

        adapterList = new AdapterList(context, R.layout.layout_adapter_list, songList);
        listView.setAdapter(adapterList);
        MyApplication.songList.clear();
        MyApplication.songList.addAll(songList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyApplication.currentSong = songList.get(position);
                MyApplication.playBackMusic(); // play the currentSong
                MyApplication.currentPosition = position;

                Log.i("my_log", "position = "+position);
            }
        });
    }

}
