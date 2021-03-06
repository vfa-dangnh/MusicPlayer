package com.haidangkf.musicplayer.fragment;

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
import com.haidangkf.musicplayer.dto.Song;
import com.haidangkf.musicplayer.utils.DividerItemDecoration;
import com.haidangkf.musicplayer.utils.SongUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FolderFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    ArrayList<String> folderList = new ArrayList<>();
    RecyclerView.Adapter<MyViewHolder> mAdapter;
    SongUtil songUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_display, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle("Folder List");

        songUtil = new SongUtil(context);
        folderList = songUtil.getFolderList();

        if (folderList.size() == 0) {
            Toast.makeText(context, "No folder was found", Toast.LENGTH_LONG).show();
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
                ArrayList<Song> songList = songUtil.getSongInFolder(folderList.get(position));
                long duration = songUtil.getTotalDuration(songList);
                holder.tvDuration.setText(songUtil.milliSecondsToTimer(duration));
                String name = folderList.get(position);
                name = name.substring(name.lastIndexOf('/') + 1);
                holder.tvName.setText(name);
                holder.tvNote.setText(songList.size() + " song(s)");
                holder.btnOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopupMenu(v, position);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return folderList.size();
            }

            public void showPopupMenu(View v, final int position) {
                PopupMenu popup = new PopupMenu(v.getContext(), v);
                popup.getMenuInflater().inflate(R.menu.popup_menu_song_list, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_open:
                                ArrayList<Song> songList = songUtil.getSongInFolder(folderList.get(position));
                                Bundle bundle = new Bundle();
                                bundle.putString("type", "folder");
                                bundle.putParcelableArrayList("songForFolder", songList);
                                startFragment(SongFragment.class.getName(), bundle, true);
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
        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvNote)
        TextView tvNote;
        @BindView(R.id.tvDuration)
        TextView tvDuration;
        @BindView(R.id.btnOption)
        ImageView btnOption;

        public MyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            v.setOnClickListener(this);
            img.setImageResource(R.drawable.ic_folder);
        }

        @Override
        public void onClick(View v) {
            ArrayList<Song> songList = songUtil.getSongInFolder(folderList.get(getAdapterPosition()));
            Bundle bundle = new Bundle();
            bundle.putString("type", "folder");
            bundle.putParcelableArrayList("songForFolder", songList);
            startFragment(SongFragment.class.getName(), bundle, true);
        }
    }

}