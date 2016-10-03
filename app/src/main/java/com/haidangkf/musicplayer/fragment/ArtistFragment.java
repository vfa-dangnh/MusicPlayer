package com.haidangkf.musicplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.haidangkf.musicplayer.R;
import com.haidangkf.musicplayer.dto.Song;
import com.haidangkf.musicplayer.utils.DividerItemDecoration;
import com.haidangkf.musicplayer.utils.SongUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArtistFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    ArrayList<String> artistList = new ArrayList<>();
    RecyclerView.Adapter<MyViewHolder> mAdapter;
    SongUtil songUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_display, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle("Artist List");

        songUtil = new SongUtil(context);
        this.artistList = songUtil.getArtistList();

        if (artistList.size() == 0) {
            Toast.makeText(context, "No artist was found", Toast.LENGTH_LONG).show();
            return;
        }

        mAdapter = new RecyclerView.Adapter<MyViewHolder>() {
            @Override
            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_list, parent, false);
                return new MyViewHolder(v);
            }

            @Override
            public void onBindViewHolder(MyViewHolder holder, int position) {
                long duration = 0;
                ArrayList<Song> songList = songUtil.getSongForArtist(artistList.get(position));
                for (Song song : songList) {
                    duration += Long.parseLong(song.getDuration());
                }
                holder.tvDuration.setText(songUtil.milliSecondsToTimer(duration));
                holder.tvName.setText(artistList.get(position));
            }

            @Override
            public int getItemCount() {
                return artistList.size();
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
        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvNote)
        TextView tvNote;
        @BindView(R.id.tvDuration)
        TextView tvDuration;

        public MyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            v.setOnClickListener(this);
            img.setImageResource(R.drawable.ic_artist);
            tvNote.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onClick(View v) {
            ArrayList<Song> songList = songUtil.getSongForArtist(artistList.get(getAdapterPosition()));
            Bundle bundle = new Bundle();
            bundle.putString("type", "artist");
            bundle.putParcelableArrayList("songForArtist", songList);
            startFragment(SongFragment.class.getName(), bundle, true);
        }
    }

}