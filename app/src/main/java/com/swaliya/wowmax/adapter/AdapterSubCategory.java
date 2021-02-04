package com.swaliya.wowmax.adapter;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.swaliya.wowmax.R;
import com.swaliya.wowmax.activity.MainActivity;
import com.swaliya.wowmax.activity.SongsActivity;
import com.swaliya.wowmax.activity.VdoDetailsActivity;
import com.swaliya.wowmax.configg.Config;
import com.swaliya.wowmax.fragment.EighteenPlusFragment;
import com.swaliya.wowmax.model.MainMovieListModel;
import com.swaliya.wowmax.model.SubCategryModel;

import java.util.List;

public class AdapterSubCategory extends RecyclerView.Adapter<AdapterSubCategory.ViewHolder> {
    private List<SubCategryModel> android;
    private Context mContex;
    private OnItemClicked listener;

    public interface OnItemClicked {
        void onItemClick(String position);
    }


    public AdapterSubCategory(List<SubCategryModel> android, Context mContex, OnItemClicked onItemClickListener) {
        this.android = android;
        this.mContex = mContex;
        this.listener = onItemClickListener;
    }

    @Override
    public AdapterSubCategory.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sub_cat, viewGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(AdapterSubCategory.ViewHolder viewHolder, int i) {
        SubCategryModel mAinMoviListModel = android.get(i);

        viewHolder.tv_name.setText(mAinMoviListModel.getSubCategoryName());


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(android.get(i).getSubCategoryName());
                //  ((eighteenPlusFragment)mContex).initiaPlayer(String.valueOf(android.get(i).getSubCategoryName()));

               /* EighteenPlusFragment plusFragment=new EighteenPlusFragment();
                plusFragment.getName(String.valueOf(android.get(i).getSubCategoryName()));*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return android.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;

        public ViewHolder(View view) {
            super(view);

            tv_name = (TextView) view.findViewById(R.id.tvName);

        }
    }

}