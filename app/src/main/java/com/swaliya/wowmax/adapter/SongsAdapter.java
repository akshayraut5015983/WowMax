package com.swaliya.wowmax.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.swaliya.wowmax.R;
import com.swaliya.wowmax.activity.SongsActivity;
import com.swaliya.wowmax.activity.VdoDetailsActivity;
import com.swaliya.wowmax.configg.Config;
import com.swaliya.wowmax.model.MainMovieListModel;

import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ViewHolder> {
    private List<MainMovieListModel> android;
    private Context mContex;

    public SongsAdapter(List<MainMovieListModel> android, Context mContex) {
        this.android = android;
        this.mContex = mContex;
    }

    @Override
    public SongsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_songs, viewGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(SongsAdapter.ViewHolder viewHolder, int i) {
        MainMovieListModel mAinMoviListModel = android.get(i);

        viewHolder.tvTitel.setText(mAinMoviListModel.getMovieTitle());
        viewHolder.tvCat.setText(mAinMoviListModel.getCategoryName());
        String url = Config.URL + mAinMoviListModel.getMovieImage2();
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
                ((SongsActivity) mContex).initiaPlayer(String.valueOf(android.get(i).getMovieTitle()));

               /* Intent intent = new Intent(mContex, VdoDetailsActivity.class);
                intent.putExtra("url", mAinMoviListModel.getMovieAddress());
                intent.putExtra("name", mAinMoviListModel.getMovieTitle());
                intent.putExtra("cat", mAinMoviListModel.getCategoryName());
                intent.putExtra("rel", mAinMoviListModel.getMovieRelYear());
                intent.putExtra("qlt", mAinMoviListModel.getMovieQuality());
                intent.putExtra("dur", mAinMoviListModel.getMovieDuration());
                intent.putExtra("desp", mAinMoviListModel.getMovieDesc());
                intent.putExtra("img", mAinMoviListModel.getMovieImage2());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContex.startActivity(intent);*/

            }
        });
    }

    @Override
    public int getItemCount() {
        return android.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitel, tvCat;
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);

            tvTitel = (TextView) view.findViewById(R.id.tvTitle);
            tvCat = (TextView) view.findViewById(R.id.tvCat);

            imageView = (ImageView) view.findViewById(R.id.img);

        }
    }

}