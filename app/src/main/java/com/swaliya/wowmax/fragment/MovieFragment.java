package com.swaliya.wowmax.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.swaliya.wowmax.R;
import com.swaliya.wowmax.activity.VdoDetailsActivity;
import com.swaliya.wowmax.adapter.MyListAdapter;
import com.swaliya.wowmax.model.MyListData;

import java.util.HashMap;

public class MovieFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private AdView mAdView, adView;
    private SliderLayout sliderLayout;
    private HashMap<String, Integer> sliderImages;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_movie, viewGroup, false);

        forAdverties(v);
    //    forSlider(v);

        v.findViewById(R.id.layCircle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Coming Soon...!", Toast.LENGTH_SHORT).show();
            }
        });

        MyListData[] myListData = new MyListData[]{
                new MyListData("http://wowmaxmovies.com/video/andhadhun.mp4", "Andhadund", "Comedy And Crime", "2018", "Full hd", "2.30 hr", "Akash, a piano player pretending to be visually-impaired, unwittingly becomes entangled in a number of problems as he witnesses the murder of a former film actor.", R.drawable.adnl, R.drawable.andp),
                new MyListData("http://wowmaxmovies.com/video/Pyaar_Ka_Punchnama_2.mp4", "Pyar ka panchnama 2", "Comedy And Romance", "2015", "Full hd", "2.30 hr", "Anshul, Siddharth and Tarun each fall in love at the same time with different women. However, their relationships soon turn toxic and make them realise the dark side of love.", R.drawable.pyarkapal, R.drawable.pyarkapap),
                new MyListData("http://wowmaxmovies.com/video/bahubali.mp4", "Bahubali", "Action and War", "2015", "Full hd", "2.40 hr", "In the kingdom of Mahishmati, Shivudu falls in love with a young warrior woman. While trying to woo her, he learns about the conflict-ridden past of his family and his true legacy.", R.drawable.babubalil, R.drawable.babubalip),
                new MyListData("http://wowmaxmovies.com/video/baby.mp4", "Baby", "Thriller and action", "2015", "Full hd", "2.34 hr", "An elite team of the Indian intelligence system perpetually strives to detect and eliminate terrorists and their plots. Officer Ajay leads a team to destroy one such potentially lethal operation.", R.drawable.babyl, R.drawable.babyp),
                new MyListData("http://wowmaxmovies.com/video/bharat.mp4", "Bharat", "Action and Drama ", "2019", "Full hd", "2.30 hr", "During the India-Pakistan partition, a family gets torn apart leaving Bharat, one of the children, in charge of the remaining members. All his life he tries to keep the promise he made to his father..", R.drawable.bharatl, R.drawable.bharatp),
                new MyListData("http://wowmaxmovies.com/video/DeDePyaarDe.mp4", "De de pyar de", "Rommance ", "2019", "Full hd", "2.30 hr", "When Ashish falls in love with Ayesha, a woman almost half his age, he introduces her to his ex-wife and children. However, their unacceptance threatens to ruin their relationship..", R.drawable.dedepyardel, R.drawable.dedepyardep),
                new MyListData("http://wowmaxmovies.com/video/fryday.mp4", "Fryday", "Comedy ", "2018", "Full hd", "2.30 hr", "Rajiv, a bad salesman, must find a way to save his job even though he has personal problems after encountering a devious man who breaks into his house and has other plans for him..", R.drawable.frydayl, R.drawable.frydayp),
                new MyListData("http://wowmaxmovies.com/video/golmaalagain.mp4", "Golmaal Again", "Comedy ", "2017", "Full hd", "2.30 hr", "Five orphan men return to the orphanage they grew up in to attend their mentor's funeral. However, they encounter the ghost of their childhood friend, Khushi, and help her attain salvation.", R.drawable.golmaall, R.drawable.golmalp),
                new MyListData("http://wowmaxmovies.com/video/gullyboy.mp4", "Gully boy", "Musical and Drama ", "2019", "Full hd", "2.30 hr", "Murad, an underdog, struggles to convey his views on social issues and life in Dharavi through rapping. His life changes drastically when he meets a local rapper, Shrikant alias MC Sher..", R.drawable.gullyboyl, R.drawable.gullyboyp),
                new MyListData("http://wowmaxmovies.com/video/manmarziyaan.mp4", "Manmarziyaan", "Rommance ", "2018", "Full hd", "2.30 hr", "Rumi and Vicky, who are in love, are caught by Rumi's family and pressurised to get married. However, when Vicky refuses to commit, a marriage broker brings in Robbie as a prospective suitor..", R.drawable.manmarziyaanl, R.drawable.manmarziyaanp),
                new MyListData("http://wowmaxmovies.com/video/nawabzaade.mp4", "Nawabzade", "Rommance and Comedy ", "2018", "Full hd", "2.30 hr", "When three best friends fall in love with the same girl, their friendship is challenged. However, they soon realize that there is more to her than her looks..", R.drawable.nawabzadel, R.drawable.nawabazadep),
                new MyListData("http://wowmaxmovies.com/video/bajiraomastani.mp4", "Bajirao Mastani", "Rommance and action ", "2018", "Full hd", "2.30 hr", "The heroic Peshwa Bajirao, married to Kashibai, falls in love with Mastani, a warrior princess in distress. They struggle to make their love triumph amid opposition from his conservative family..", R.drawable.bajiraomastanil, R.drawable.bajiraomastanip),
        };

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        MyListAdapter adapter = new MyListAdapter(myListData, getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(adapter);

        return v;

    }

    private void forAdverties(View v) {


        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        PublisherAdView mPublisherAdView = v.findViewById(R.id.adView);
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
        mPublisherAdView.loadAd(adRequest);

        PublisherAdView   mPublisherAdVieww = v.findViewById(R.id.adVieww);
        PublisherAdRequest adRequestt = new PublisherAdRequest.Builder().build();
        mPublisherAdVieww.loadAd(adRequestt);
    }

    private void forSlider(View v) {
        sliderLayout = v.findViewById(R.id.sliderLayout);
        sliderImages = new HashMap<>();
        sliderImages.put("Bahubali", R.drawable.babubalip);
        sliderImages.put("Pyar ka panchanama", R.drawable.pyarkapap);
        sliderImages.put("Bharat", R.drawable.bharatp);

        for (String name : sliderImages.keySet()) {

            TextSliderView textSliderView = new TextSliderView(getContext());
            textSliderView
                    .description(name)
                    .image(sliderImages.get(name))
                    .setScaleType(BaseSliderView.ScaleType.FitCenterCrop)
                    .setOnSliderClickListener(this);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);
            sliderLayout.addSlider(textSliderView);
        }

        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
        // sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        //  sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(2000);
        sliderLayout.addOnPageChangeListener(this);
        sliderLayout.getPagerIndicator().setVisibility(View.GONE);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        //  Toast.makeText(getContext(), slider.getBundle().get("extra") +  " ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}