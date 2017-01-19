package com.haidangkf.musicplayer.online.artist;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haidangkf.musicplayer.R;
import com.haidangkf.musicplayer.online.OnlineDefine;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterArtist extends ArrayAdapter<OnlineArtist> {
    private Activity context;
    private int layout;
    private ArrayList<OnlineArtist> list;
    View row;

    public AdapterArtist(Context context, int textViewResourceId, ArrayList<OnlineArtist> objects) {
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
            holder.img = (ImageView) row.findViewById(R.id.adapter_artist_img);
            holder.name = (TextView) row.findViewById(R.id.adapter_artist_name);
            row.setTag(holder);
        } else {
            holder = (Holder) row.getTag();
        }

        OnlineArtist artist = list.get(position);
        String name = artist.getArtist_name();
        String img = artist.getArtist_img_link();
        String fullLinkImage = OnlineDefine.IMG_ARTIST_LINK + img;
        holder.name.setText(name);
        Picasso.with(context).load(fullLinkImage).into(holder.img);

        return row;
    }

    public static class Holder {
        ImageView img;
        TextView name;
    }
}