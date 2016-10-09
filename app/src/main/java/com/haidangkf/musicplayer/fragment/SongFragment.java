package com.haidangkf.musicplayer.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.haidangkf.musicplayer.R;
import com.haidangkf.musicplayer.activity.MainActivity;
import com.haidangkf.musicplayer.activity.PlayerActivity;
import com.haidangkf.musicplayer.controls.Controls;
import com.haidangkf.musicplayer.controls.PlayerConstants;
import com.haidangkf.musicplayer.dto.Song;
import com.haidangkf.musicplayer.service.MyService;
import com.haidangkf.musicplayer.utils.Common;
import com.haidangkf.musicplayer.utils.DividerItemDecoration;
import com.haidangkf.musicplayer.utils.SongUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SongFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    RecyclerView.Adapter<MyViewHolder> mAdapter;
    public static SongUtil songUtil;
    public static ArrayList<Song> songList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_display, container, false);
    }

    public void init() {
        songUtil = new SongUtil(context);
        songList = songUtil.getAllSongs(); // default get all songs
        setClickEventsBottomBar();
    }

    public void setClickEventsBottomBar() {
        MainActivity.btnPlayBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PlayerConstants.SONG_PAUSED) {
                    Controls.pauseControl(context);
                } else {
                    Controls.playControl(context);
                }
                MainActivity.updateUIBottomBar(context);
            }
        });

        MainActivity.btnNextBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Controls.nextControl(context);
                MainActivity.updateUIBottomBar(context);
            }
        });

        MainActivity.btnPreviousBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Controls.previousControl(context);
                MainActivity.updateUIBottomBar(context);
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle("Song List");

        init(); // very important

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String type = bundle.getString("type");
            if (type.equals("album")) {
                songList = bundle.getParcelableArrayList("songForAlbum");
            } else if (type.equals("artist")) {
                songList = bundle.getParcelableArrayList("songForArtist");
            } else if (type.equals("folder")) {
                songList = bundle.getParcelableArrayList("songForFolder");
            }
        }

        if (songList.size() == 0) {
            Toast.makeText(context, "No song was found", Toast.LENGTH_LONG).show();
            return;
        }

        mAdapter = new RecyclerView.Adapter<MyViewHolder>() {
            @Override
            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_list_rv, parent, false);
                return new MyViewHolder(v);
            }

            @Override
            public void onBindViewHolder(MyViewHolder holder, final int position) {
                holder.tvName.setText(songList.get(position).getName());
                holder.tvArtist.setText(songList.get(position).getArtist());
                long duration = Long.parseLong(songList.get(position).getDuration());
                holder.tvDuration.setText(songUtil.milliSecondsToTimer(duration));
                holder.btnOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopupMenu(v, position);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return songList.size();
            }

            public void showPopupMenu(View v, final int position) {
                PopupMenu popup = new PopupMenu(v.getContext(), v);
                popup.getMenuInflater().inflate(R.menu.popup_menu_song, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_play:
                                PlayerConstants.SONG_LIST = songList;
                                PlayerConstants.SONG_INDEX = position;
                                PlayerConstants.SONG_PAUSED = false;

                                Intent i = new Intent(context, PlayerActivity.class);
                                i.putExtra("from", 2); // when click on menu Play
                                startActivity(i);
                                break;
                            case R.id.action_detail:
                                String title = songList.get(position).getName();
                                String message = songList.get(position).toString();
                                Common.dialogConfirmOk(context, title, message, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
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
        @BindView(R.id.tvNote)
        TextView tvArtist;
        @BindView(R.id.tvDuration)
        TextView tvDuration;
        @BindView(R.id.btnOption)
        ImageView btnOption;

        public MyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            v.setOnClickListener(this);
        }

        // when click on list item recyclerView
        @Override
        public void onClick(View v) {
            PlayerConstants.SONG_LIST = songList;
            PlayerConstants.SONG_INDEX = getAdapterPosition();
            PlayerConstants.SONG_PAUSED = false;
            if (!Common.isServiceRunning(context, MyService.class)) {
                Common.startService(context, MyService.class);
            } else {
                // service is running, handle action change song
                PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            }
            MainActivity.updateUIBottomBar(context);
        }
    }

}