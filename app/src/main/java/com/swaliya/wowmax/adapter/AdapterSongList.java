package com.swaliya.wowmax.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.swaliya.wowmax.R;
import com.swaliya.wowmax.activity.VdoDetailsActivity;
import com.swaliya.wowmax.configg.Config;
import com.swaliya.wowmax.model.Slider10Model;

import java.util.List;

public class AdapterSongList extends RecyclerView.Adapter<AdapterSongList.ViewHolder> {
    private List<Slider10Model> android;
    private Context mContex;

    public AdapterSongList(List<Slider10Model> android, Context mContex) {
        this.android = android;
        this.mContex = mContex;
    }

    @Override
    public AdapterSongList.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rec_movie_song, viewGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(AdapterSongList.ViewHolder viewHolder, int i) {
        Slider10Model mAinMoviListModel = android.get(i);

        viewHolder.tv_name.setText(mAinMoviListModel.getVideoName());
        viewHolder.tvCat.setText(mAinMoviListModel.getCategory());
        viewHolder.tvDate.setText(mAinMoviListModel.getReleseDate());
        String url = Config.URL + mAinMoviListModel.getThumbnail1();
        try {

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .override(240, 320)
                    .placeholder(R.drawable.black_wowmax)
                    .error(R.drawable.black_wowmax);


            Glide.with(mContex).load(url).apply(options).into(viewHolder.imageView);

           /* Picasso.with(mContex)
                    .load(Config.URL + mAinMoviListModel.getMovieImage())
                    .resize(240, 320)
                    .placeholder(R.drawable.black_wowmax_lan)
                    .error(R.drawable.black_wowmax_lan)
                    .fit()
                    .into(viewHolder.imageView);*/
        } catch (OutOfMemoryError e) {
            Toast.makeText(mContex, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            Log.d("TAG", "OutOfMemoryError: " + e.getMessage());
        } catch (Exception e) {
            Toast.makeText(mContex, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            Log.d("TAG", "Exception: " + e.getMessage());
        }


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContex, VdoDetailsActivity.class);
                intent.putExtra("url", mAinMoviListModel.getVidePath());
                intent.putExtra("name", mAinMoviListModel.getVideoName());
                intent.putExtra("cat", mAinMoviListModel.getCategory());
                intent.putExtra("rel", mAinMoviListModel.getReleseDate());
                intent.putExtra("qlt", mAinMoviListModel.getTypes());
                intent.putExtra("dur", mAinMoviListModel.getDuration());
                intent.putExtra("desp", mAinMoviListModel.getDiscription());
                intent.putExtra("writ", mAinMoviListModel.getWriter());
                intent.putExtra("mdir", mAinMoviListModel.getMusicDirector());
                intent.putExtra("cast", mAinMoviListModel.getCast());
                intent.putExtra("onel", mAinMoviListModel.getOneLine());
                intent.putExtra("img", mAinMoviListModel.getThumbnail2());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContex.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return android.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tvDate, tvCat;
        RoundedImageView imageView;

        public ViewHolder(View view) {
            super(view);

            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvCat = (TextView) view.findViewById(R.id.tvCat);
            tv_name = (TextView) view.findViewById(R.id.tvName);
            imageView = (RoundedImageView) view.findViewById(R.id.imgMov);

        }
    }

}