package com.haidangkf.musicplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.haidangkf.musicplayer.R;
import com.haidangkf.musicplayer.dto.Song;
import com.haidangkf.musicplayer.utils.DividerItemDecoration;
import com.haidangkf.musicplayer.utils.SongUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SongFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    ArrayList<Song> songList = new ArrayList<>();
    RecyclerView.Adapter<MyViewHolder> mAdapter;
    SongUtil songUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_song, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle("Song List");

        songUtil = new SongUtil(context);
        this.songList = songUtil.getAllSongs();

        if (songList.size() == 0) {
            Toast.makeText(context, "No song was found", Toast.LENGTH_LONG).show();
            return;
        }

        mAdapter = new RecyclerView.Adapter<MyViewHolder>() {
            @Override
            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_song_list, parent, false);
                return new MyViewHolder(v);
            }

            @Override
            public void onBindViewHolder(MyViewHolder holder, int position) {
                holder.tvName.setText(songList.get(position).getName());
                holder.tvArtist.setText(songList.get(position).getArtist());
                long duration = Long.parseLong(songList.get(position).getDuration());
                holder.tvDuration.setText(songUtil.milliSecondsToTimer(duration));
            }

            @Override
            public int getItemCount() {
                return songList.size();
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // display the divider between rows
        recyclerView.addItemDecoration(new DividerItemDecoration(context, R.drawable.divider_line));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvArtist)
        TextView tvArtist;
        @BindView(R.id.tvDuration)
        TextView tvDuration;

        public MyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            startFragment(PlayerFragment.class.getName(), null, true);
        }
    }

}