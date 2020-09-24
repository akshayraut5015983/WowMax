package com.swaliya.vidmax.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.swaliya.vidmax.R;
import com.swaliya.vidmax.model.Datum;
import com.swaliya.vidmax.model.HindiModel;

import java.util.ArrayList;

public class HindiAdapter extends RecyclerView.Adapter<HindiAdapter.ViewHolder> {
    private ArrayList<Datum> android;
    private Context mContex;

    public HindiAdapter(ArrayList<Datum> android, Context mContex) {
        this.android = android;
        this.mContex = mContex;
    }

    @Override
    public HindiAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rec_hindi_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HindiAdapter.ViewHolder viewHolder, int i) {
        final Datum m = android.get(i);
        viewHolder.tv_name.setText(String.valueOf(m.getName()));

      /*  Glide.with(mContex)
                .load(m.getThumb())
                .into(viewHolder.imageView);*/

    }

    @Override
    public int getItemCount() {
        return android.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);

            tv_name = (TextView) view.findViewById(R.id.tvName);
            imageView = (ImageView) view.findViewById(R.id.img);


        }
    }

}