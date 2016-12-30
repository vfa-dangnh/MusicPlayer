package com.haidangkf.musicplayer.online.album;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.haidangkf.musicplayer.R;
import com.haidangkf.musicplayer.online.OnlineDefine;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterOnlineAlbum extends ArrayAdapter<OnlineAlbum> {
    private Activity context;
    private int layout;
    private ArrayList<OnlineAlbum> list;
    View row;
    public AdapterOnlineAlbum(Context context, int textViewResourceId, ArrayList<OnlineAlbum> objects)
    {
        super(context, textViewResourceId, objects);

        this.context = (Activity) context;
        this.layout = textViewResourceId;
        this.list = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        row = convertView;
        final Holder holder;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layout, parent, false);


            holder = new Holder();
            holder.img = (ImageView) row.findViewById(R.id.adapter_online_album_img);

            row.setTag(holder);
        }
        else
        {
            holder = (Holder)row.getTag();
        }

        OnlineAlbum album = list.get(position);

        String img = album.getAlbum_img_link();
        String fullLinkImage = OnlineDefine.IMG_ALBUM_LINK+img;

        Picasso.with(context).load(fullLinkImage).into(holder.img);

        return row;
    }
    public static class Holder
    {
        ImageView img;

    }
}