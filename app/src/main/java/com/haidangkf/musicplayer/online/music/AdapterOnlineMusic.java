package com.haidangkf.musicplayer.online.music;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.haidangkf.musicplayer.R;

import java.util.ArrayList;

public class AdapterOnlineMusic extends ArrayAdapter<OnlineSong> {
    private Activity context;
    private int layout;
    private ArrayList<OnlineSong> list;
    View row;

    public AdapterOnlineMusic(Context context, int textViewResourceId, ArrayList<OnlineSong> objects) {
        super(context, textViewResourceId, objects);
        this.context = (Activity) context;
        this.layout = textViewResourceId;
        this.list = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        row = convertView;
        final Holder holder;

        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(layout, parent, false);
            holder = new Holder();
            holder.artist = (TextView) row.findViewById(R.id.adapter_music_online_artist);
            holder.name = (TextView) row.findViewById(R.id.adapter_music_online_name);
            row.setTag(holder);
        } else {
            holder = (Holder) row.getTag();
        }

        OnlineSong song = list.get(position);
        String name = song.getName();
        String artist = song.getArtist_name();
        holder.name.setText("" + name);
        holder.artist.setText("" + artist);

        return row;
    }

    public static class Holder {
        TextView name;
        TextView artist;
    }
}