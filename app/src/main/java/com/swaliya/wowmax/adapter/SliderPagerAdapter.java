package com.swaliya.wowmax.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;
import com.swaliya.wowmax.R;
import com.swaliya.wowmax.activity.VdoDetailsActivity;
import com.swaliya.wowmax.configg.Config;
import com.swaliya.wowmax.model.MainMovieListModel;
import com.swaliya.wowmax.model.SliderModel;

import java.util.List;


public class SliderPagerAdapter extends PagerAdapter {
    Context mContext;
    List<MainMovieListModel> categoryArrayList;
    LayoutInflater mLayoutInflater;
    Button go;

    public SliderPagerAdapter(Context m_Context, List<MainMovieListModel> categoryArrayList) {
        this.mContext = m_Context;
        this.categoryArrayList = categoryArrayList;
        mLayoutInflater = (LayoutInflater) m_Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        if (categoryArrayList != null)
            return categoryArrayList.size();
        else
            return 0;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
//        View itemView = (View) mLayoutInflater.inflate(R.layout.item_service_banner, null);
        View itemView = mLayoutInflater.inflate(R.layout.item_service_banner, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.img);
        String url = Config.URL + categoryArrayList.get(position).getMovieImage2();
        String finUrl = url.replace(" ", "%20");
        Log.e("TAG", "instantiateItem: " + finUrl);
        Picasso.with(mContext)
                .load(finUrl)
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, VdoDetailsActivity.class);
                intent.putExtra("url", categoryArrayList.get(position).getMovieAddress());
                intent.putExtra("name", categoryArrayList.get(position).getMovieTitle());
                intent.putExtra("cat", categoryArrayList.get(position).getCategoryName());
                intent.putExtra("rel", categoryArrayList.get(position).getMovieRelYear());
                intent.putExtra("qlt", categoryArrayList.get(position).getMovieQuality());
                intent.putExtra("dur", categoryArrayList.get(position).getMovieDuration());
                intent.putExtra("desp", categoryArrayList.get(position).getMovieDesc());
                intent.putExtra("img", categoryArrayList.get(position).getMovieImage2());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);

            }
        });
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

}

