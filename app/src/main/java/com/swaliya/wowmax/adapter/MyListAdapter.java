package com.swaliya.wowmax.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.swaliya.wowmax.R;
import com.swaliya.wowmax.activity.VdoDetailsActivity;
import com.swaliya.wowmax.activity.VdoDetailsOfflineActivity;
import com.swaliya.wowmax.model.MyListData;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {
    private MyListData[] listdata;
    private Context context;

    // RecyclerView recyclerView;
    public MyListAdapter(MyListData[] listdata, Context context) {
        this.listdata = listdata;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MyListData myListData = listdata[position];
        holder.textView.setText(listdata[position].getName());
        holder.tvCat.setText(listdata[position].getCat());

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .override(240, 320)
                .placeholder(R.drawable.black_wowmax)
                .error(R.drawable.black_wowmax);


        Glide.with(context).load(listdata[position].getImgl()).apply(options).into(holder.imageView);

     //   holder.imageView.setImageResource(listdata[position].getImgl());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Toast.makeText(view.getContext(), "click on item: " + myListData.getName(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(context, VdoDetailsOfflineActivity.class);
                intent.putExtra("url", myListData.getUrl());
                intent.putExtra("name", myListData.getName());
                intent.putExtra("cat", myListData.getCat());
                intent.putExtra("rel", myListData.getRel());
                intent.putExtra("qlt", myListData.getQlt());
                intent.putExtra("dur", myListData.getDur());
                intent.putExtra("desp", myListData.getDesp());
                intent.putExtra("img", myListData.getImgp());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RoundedImageView imageView;
        public TextView textView, tvCat;


        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (RoundedImageView) itemView.findViewById(R.id.imgMov);
            this.textView = (TextView) itemView.findViewById(R.id.tvName);
            this.tvCat = (TextView) itemView.findViewById(R.id.tvCat);
        }
    }
}