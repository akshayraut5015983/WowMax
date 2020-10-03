package com.swaliya.vidmax.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.swaliya.vidmax.R;
import com.swaliya.vidmax.model.Datum;
import com.swaliya.vidmax.model.HindiModel;
import com.swaliya.vidmax.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class HindiAdapter extends RecyclerView.Adapter<HindiAdapter.ViewHolder> {
    private List<Movie> android;
    private Context mContex;

    public HindiAdapter(List<Movie> android, Context mContex) {
        this.android = android;
        this.mContex = mContex;
    }

    @Override
    public HindiAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rec_hindi_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    public void setMovieList(List<Movie> movieList) {
        this.android = movieList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(HindiAdapter.ViewHolder viewHolder, int i) {
        viewHolder.tv_name.setText(android.get(i).getTitle().toString());

        Glide.with(mContex).load(android.get(i).getImage()).apply(RequestOptions.centerCropTransform()).into(viewHolder.imageView);

    }

    @Override
    public int getItemCount() {
        return android.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        RoundedImageView imageView;

        public ViewHolder(View view) {
            super(view);

            tv_name = (TextView) view.findViewById(R.id.tvName);
            imageView = (RoundedImageView) view.findViewById(R.id.img);


        }
    }

}