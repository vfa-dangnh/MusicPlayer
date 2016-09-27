package com.haidangkf.musicplayer.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.haidangkf.musicplayer.R;
import com.haidangkf.musicplayer.adapter.AdapterList;
import com.haidangkf.musicplayer.dto.Song;
import com.haidangkf.musicplayer.utils.MyApplication;

import java.util.ArrayList;

public class FragmentList extends BaseFragment {

    TextView tvTitle;
    ListView listView;

    ArrayList<Song> songList = new ArrayList<>();
    AdapterList adapterList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragment_list, container, false);

        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        listView = (ListView) view.findViewById(R.id.listView);

        songList = MyApplication.getAllMusicList(getActivity());
        adapterList = new AdapterList(getActivity(), R.layout.layout_adapter_list, songList);
        listView.setAdapter(adapterList);
        adapterList.setNotifyOnChange(true);

        MyApplication.songList.clear();
        MyApplication.songList.addAll(songList);
        setIsPlayingOrNot();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyApplication.currentSong = songList.get(position);
                MyApplication.playBackMusic();  // play the currentSong
                MyApplication.currentPosition = position;
                Log.i("my_log", "position = "+position);
                setIsPlayingOrNot();
            }
        });
        return view;
    }

    public void setIsPlayingOrNot() {
        for (int i = 0; i<songList.size(); i++) {
            Song song = songList.get(i);
            if (song.getPath().equals(MyApplication.currentSong.getPath())) {
                song.setPlaying(true);
            } else {
                song.setPlaying(false);
            }
        }

        adapterList.notifyDataSetChanged();
    }
}
