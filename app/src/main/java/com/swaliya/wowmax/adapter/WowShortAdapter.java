package com.swaliya.wowmax.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayer;
import com.swaliya.wowmax.BuildConfig;
import com.swaliya.wowmax.R;
import com.swaliya.wowmax.model.MainMovieListModel;

import java.util.List;

public class WowShortAdapter extends RecyclerView.Adapter<WowShortAdapter.VideoViewHolder> {
    private List<MainMovieListModel> mVideoItems;
    int like = 1, disLike = 1;
    private Context mContex;
    ExoPlayer player;

    public WowShortAdapter(List<MainMovieListModel> videoItems, Context context) {
        mVideoItems = videoItems;
        this.mContex = context;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_wowshort_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.setVideoData(mVideoItems.get(position));
        // defineData(mVideoItems.get(position).getMovieAddress().toString());
        holder.mProgressBar.setVisibility(View.VISIBLE);
        holder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Wowmax");
                    String shareMessage = "\nLet me recommend you this application\n\n";

                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    mContex.startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    Log.d("TAg", "onNavigationItemSelected: ");
                }
            }
        });

        holder.imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                like = like + 1;
                if (like % 2 == 0) {
                    holder.tvLike.setText("1");
                    holder.imgLike.setColorFilter(Color.RED);
                } else {
                    holder.imgLike.setColorFilter(Color.WHITE);
                    holder.tvLike.setText("0");
                }
            }
        });
        holder.imgDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disLike = disLike + 1;
                if (disLike % 2 == 0) {
                    holder.tvDislike.setText("1");
                    holder.imgDislike.setColorFilter(Color.GRAY);
                } else {
                    holder.tvDislike.setText("0");
                    holder.imgDislike.setColorFilter(Color.WHITE);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mVideoItems.size();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        VideoView mVideoView;
        TextView txtTitle, txtDesc, tvLike, tvDislike;
        ProgressBar mProgressBar;
        ImageView imgShare, imgLike, imgPlay, imgDislike;


        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            mVideoView = itemView.findViewById(R.id.videoView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDesc = itemView.findViewById(R.id.txtDesc);
            tvLike = itemView.findViewById(R.id.tvLike);
            tvDislike = itemView.findViewById(R.id.tvDislike);
            mProgressBar = itemView.findViewById(R.id.progressBar);
            imgPlay = itemView.findViewById(R.id.omgPlay);
            imgShare = itemView.findViewById(R.id.imgShare);
            imgLike = itemView.findViewById(R.id.imgLike);
            imgDislike = itemView.findViewById(R.id.imgDislike);
        }

        private void setVideoData(MainMovieListModel videoItem) {
            txtTitle.setText(videoItem.getMovieTitle());
            txtDesc.setText(videoItem.getCategoryName());
            mVideoView.setVideoURI(Uri.parse(videoItem.getMovieAddress()));
            //   mVideoView.setVideoPath(videoItem.getMovieAddress());
            //playerview binding with player


            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mProgressBar.setVisibility(View.GONE);
                    imgPlay.setVisibility(View.VISIBLE);
                    //  mp.start();
                    mp.seekTo(2);
                    if (imgPlay.getVisibility() == View.GONE) {
                        mp.start();
                    } else {
                        imgPlay.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                if (mp.isPlaying()) {
                                    mp.stop();
                                } else {
                                    mp.start();

                                    imgPlay.setVisibility(View.GONE);
                                }
                            }
                        });
                    }

                   /* float videoRatio = mp.getVideoWidth() / (float)mp.getVideoHeight();
                    float screenRatio = mVideoView.getWidth() / (float)mVideoView.getHeight();
                    float scale  = videoRatio / screenRatio;
                    if (scale >= 1f){
                        mVideoView.setScaleX(scale);
                    }else {
                        mVideoView.setScaleY(1f / scale);
                    }*/

                }
            });
            mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    imgPlay.setVisibility(View.VISIBLE);
                    mp.seekTo(2);
                    //  mp.start();
                    imgPlay.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (mp.isPlaying()) {
                                mp.stop();
                            } else {
                                mp.start();

                                imgPlay.setVisibility(View.GONE);
                            }
                        }
                    });

                }
            });

        }
    }
}
