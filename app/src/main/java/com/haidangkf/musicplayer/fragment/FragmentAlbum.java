package com.haidangkf.musicplayer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.haidangkf.musicplayer.R;
import com.haidangkf.musicplayer.activity.ListActivity;
import com.haidangkf.musicplayer.utils.MyApplication;

import java.util.ArrayList;

public class FragmentAlbum extends BaseFragment {

    TextView tvTitle;
    ListView listView;
    ArrayList<String> albumList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragment_album, container, false);

        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        listView = (ListView) view.findViewById(R.id.listView);

        albumList = MyApplication.getAlbumList(getActivity());
        adapter = new ArrayAdapter<>(getActivity(), R.layout.layout_adapter_album, R.id.tvAlbumName, albumList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = albumList.get(position);
                Intent i = new Intent(getActivity(), ListActivity.class);
                Bundle b = new Bundle();
                b.putString("name", name);
                b.putString("type", "album");
                i.putExtra("bundle", b);
                startActivity(i);
            }
        });

        return view;
    }
}
