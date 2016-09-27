package com.haidangkf.musicplayer.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haidangkf.musicplayer.R;
import com.haidangkf.musicplayer.dto.Song;

import java.util.ArrayList;

public class AdapterList extends ArrayAdapter<Song> {

    private Activity context;
    private int layout;
    private ArrayList<Song> list;
    View row;

    public AdapterList(Context context, int textViewResourceId, ArrayList<Song> objects) {
        super(context, textViewResourceId, objects);

        this.context = (Activity) context;
        this.layout = textViewResourceId;
        this.list = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TypedArray ta = getContext().getResources().obtainTypedArray(R.array.colors);
        int[] myColors = new int[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            myColors[i] = ta.getColor(i,0);
        }

        row = convertView;
        final Holder holder;

        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(layout, parent, false);

            holder = new Holder();
            holder.tvSongName = (TextView) row.findViewById(R.id.tvSongName);
            holder.imgSong = (ImageView) row.findViewById(R.id.imgSong);

            row.setTag(holder);
        } else {
            holder = (Holder) row.getTag();
        }

        Song song = list.get(position);

        holder.tvSongName.setText(song.getSongName());
        if (song.isPlaying()) {
            holder.imgSong.setBackgroundResource(R.drawable.treble_clef);
            holder.tvSongName.setTextColor(myColors[2]);
            holder.tvSongName.setBackgroundColor(myColors[6]);
        } else {
            holder.imgSong.setBackgroundResource(R.drawable.list);
            holder.tvSongName.setTextColor(myColors[0]);
            holder.tvSongName.setBackgroundColor(myColors[1]);
        }

        return row;
    }

    public static class Holder {
        TextView tvSongName;
        ImageView imgSong;
    }
}