package com.swaliya.wowmax.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.swaliya.wowmax.R;
import com.swaliya.wowmax.adapter.MyListAdapter;
import com.swaliya.wowmax.adapter.ShortMoviAdapter;
import com.swaliya.wowmax.model.MyListData;

public class ShortMoviFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_short_movi, viewGroup, false);

        MyListData[] myListData = new MyListData[]{
                new MyListData("De De pyar De ", R.drawable.dedepyardep),
                new MyListData("Ishq Junoon", R.drawable.ishqjunoonp),
                new MyListData("Bahubali", R.drawable.babubalip),
                new MyListData("Fukrey", R.drawable.fukreyp),
        };

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        ShortMoviAdapter adapter = new ShortMoviAdapter(myListData, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return v;
    }
}