package com.swaliya.wowmax.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.swaliya.wowmax.R;
import com.swaliya.wowmax.activity.VdoDetailsActivity;

import java.util.HashMap;

public class MovieFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private AdView mAdView, adView;
    private SliderLayout sliderLayout;
    private HashMap<String, Integer> sliderImages;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_movie, viewGroup, false);

        forAdverties(v);
        forSlider(v);

        v.findViewById(R.id.layCircle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Coming Soon...!", Toast.LENGTH_SHORT).show();
            }
        });
        /*v.findViewById(R.id.layAllLetest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ViewAllVdoActivity.class));
                getActivity().overridePendingTransition(R.anim.enter, R.anim.hold);
            }
        });
        v.findViewById(R.id.layAllHindi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ViewAllVdoActivity.class));
                getActivity().overridePendingTransition(R.anim.enter, R.anim.hold);
            }
        });
        v.findViewById(R.id.layMarathi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ViewAllVdoActivity.class));
                getActivity().overridePendingTransition(R.anim.enter, R.anim.hold);
            }
        });
        v.findViewById(R.id.layAllWebSeries).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ViewAllVdoActivity.class));
                getActivity().overridePendingTransition(R.anim.enter, R.anim.hold);
            }
        });
        v.findViewById(R.id.layAllShortMovi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ViewAllVdoActivity.class));
                getActivity().overridePendingTransition(R.anim.enter, R.anim.hold);
            }
        });
        v.findViewById(R.id.layAllRomantic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ViewAllVdoActivity.class));
                getActivity().overridePendingTransition(R.anim.enter, R.anim.hold);
            }
        });
        v.findViewById(R.id.layAllActionDrama).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ViewAllVdoActivity.class));
                getActivity().overridePendingTransition(R.anim.enter, R.anim.hold);
            }
        });
        v.findViewById(R.id.layAllComedy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ViewAllVdoActivity.class));
                getActivity().overridePendingTransition(R.anim.enter, R.anim.hold);
            }
        });
        v.findViewById(R.id.layAllHorror).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ViewAllVdoActivity.class));
                getActivity().overridePendingTransition(R.anim.enter, R.anim.hold);
            }
        });
        v.findViewById(R.id.layAllCrimeSuspence).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ViewAllVdoActivity.class));
                getActivity().overridePendingTransition(R.anim.enter, R.anim.hold);
            }
        });
        v.findViewById(R.id.layAllAdult).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ViewAllVdoActivity.class));
                getActivity().overridePendingTransition(R.anim.enter, R.anim.hold);
            }
        });*/

        v.findViewById(R.id.andhadundh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), VdoDetailsActivity.class);
                intent.putExtra("url", "http://wowmaxmovies.com/video/andhadhun.mp4");
                intent.putExtra("name", "Andhadund");
                intent.putExtra("cat", "Comedy And Crime");
                intent.putExtra("rel", "2018");
                intent.putExtra("qlt", "Full hd");
                intent.putExtra("dur", "2.30 hr");
                intent.putExtra("desp", "Akash, a piano player pretending to be visually-impaired, unwittingly becomes entangled in a number of problems as he witnesses the murder of a former film actor.");
                intent.putExtra("img", R.drawable.andp);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.hold);
            }
        });
        v.findViewById(R.id.pyarkapal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), VdoDetailsActivity.class);
                intent.putExtra("url", "http://wowmaxmovies.com/video/Pyaar_Ka_Punchnama_2.mp4");
                intent.putExtra("name", "pyar_ka_panchnama_2");
                intent.putExtra("cat", "Comedy And Romance");
                intent.putExtra("rel", "2015");
                intent.putExtra("qlt", "Full hd");
                intent.putExtra("dur", "2.40 hr");
                intent.putExtra("desp", "Anshul, Siddharth and Tarun each fall in love at the same time with different women. However, their relationships soon turn toxic and make them realise the dark side of love.");
                intent.putExtra("img", R.drawable.pyarkapap);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.hold);
            }
        });
        v.findViewById(R.id.babubalil).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), VdoDetailsActivity.class);
                intent.putExtra("url", "http://wowmaxmovies.com/video/bahubali.mkv");
                intent.putExtra("name", "Bahubali");
                intent.putExtra("cat", "Action And War");
                intent.putExtra("rel", "2015");
                intent.putExtra("qlt", "Full hd");
                intent.putExtra("dur", "2.40 hr");
                intent.putExtra("desp", "In the kingdom of Mahishmati, Shivudu falls in love with a young warrior woman. While trying to woo her, he learns about the conflict-ridden past of his family and his true legacy.");
                intent.putExtra("img", R.drawable.babubalip);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.hold);
            }
        });
        v.findViewById(R.id.babyl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), VdoDetailsActivity.class);
                intent.putExtra("url", "http://wowmaxmovies.com/video/baby.mkv");
                intent.putExtra("name", "Baby");
                intent.putExtra("cat", "Thriller and action");
                intent.putExtra("rel", "2015");
                intent.putExtra("qlt", "Full hd");
                intent.putExtra("dur", "2.40 hr");
                intent.putExtra("desp", "An elite team of the Indian intelligence system perpetually strives to detect and eliminate terrorists and their plots. Officer Ajay leads a team to destroy one such potentially lethal operation.");
                intent.putExtra("img", R.drawable.babyp);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.hold);
            }
        });
        v.findViewById(R.id.bharatl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), VdoDetailsActivity.class);
                intent.putExtra("url", "http://wowmaxmovies.com/video/bharat.mp4");
                intent.putExtra("name", "Bharat");
                intent.putExtra("cat", "Action and Drama");
                intent.putExtra("rel", "2019");
                intent.putExtra("qlt", "Full hd");
                intent.putExtra("dur", "2.35 hr");
                intent.putExtra("desp", "During the India-Pakistan partition, a family gets torn apart leaving Bharat, one of the children, in charge of the remaining members. All his life he tries to keep the promise he made to his father.");
                intent.putExtra("img", R.drawable.bharatp);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.hold);
            }
        });
        v.findViewById(R.id.dedepyardel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), VdoDetailsActivity.class);
                intent.putExtra("url", "http://wowmaxmovies.com/video/DeDePyaarDe.mp4");
                intent.putExtra("name", "De de pyar de");
                intent.putExtra("cat", "Rommance");
                intent.putExtra("rel", "2019");
                intent.putExtra("qlt", "Full hd");
                intent.putExtra("dur", "2.12 hr");
                intent.putExtra("desp", "When Ashish falls in love with Ayesha, a woman almost half his age, he introduces her to his ex-wife and children. However, their unacceptance threatens to ruin their relationship.");
                intent.putExtra("img", R.drawable.dedepyardep);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.hold);
            }
        });
        v.findViewById(R.id.frydayl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), VdoDetailsActivity.class);
                intent.putExtra("url", "http://wowmaxmovies.com/video/fryday.mp4");
                intent.putExtra("name", "Fryday");
                intent.putExtra("cat", "Comedy");
                intent.putExtra("rel", "2018");
                intent.putExtra("qlt", "Full hd");
                intent.putExtra("dur", "1.54 hr");
                intent.putExtra("desp", "Rajiv, a bad salesman, must find a way to save his job even though he has personal problems after encountering a devious man who breaks into his house and has other plans for him.");
                intent.putExtra("img", R.drawable.frydayp);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.hold);
            }
        });
        v.findViewById(R.id.golmaall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), VdoDetailsActivity.class);
                intent.putExtra("url", "http://wowmaxmovies.com/video/golmaalagain.mp4");
                intent.putExtra("name", "Golmaal Again");
                intent.putExtra("cat", "Comedy");
                intent.putExtra("rel", "2017");
                intent.putExtra("qlt", "Full hd");
                intent.putExtra("dur", "3.00 hr");
                intent.putExtra("desp", "Five orphan men return to the orphanage they grew up in to attend their mentor's funeral. However, they encounter the ghost of their childhood friend, Khushi, and help her attain salvation");
                intent.putExtra("img", R.drawable.golmalp);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.hold);
            }
        });
        v.findViewById(R.id.gullyboyl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), VdoDetailsActivity.class);
                intent.putExtra("url", "http://wowmaxmovies.com/video/gullyboy.mp4");
                intent.putExtra("name", "Gully boy");
                intent.putExtra("cat", "Musical and Drama");
                intent.putExtra("rel", "2019");
                intent.putExtra("qlt", "Full hd");
                intent.putExtra("dur", "2.35 hr");
                intent.putExtra("desp", "Murad, an underdog, struggles to convey his views on social issues and life in Dharavi through rapping. His life changes drastically when he meets a local rapper, Shrikant alias MC Sher.");
                intent.putExtra("img", R.drawable.gullyboyp);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.hold);
            }
        });
        v.findViewById(R.id.manmarziyaanl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), VdoDetailsActivity.class);
                intent.putExtra("url", "http://wowmaxmovies.com/video/manmarziyaan.mp4");
                intent.putExtra("name", "Manmarziyaan");
                intent.putExtra("cat", "Rommance");
                intent.putExtra("rel", "2018");
                intent.putExtra("qlt", "Full hd");
                intent.putExtra("dur", "2.37 hr");
                intent.putExtra("desp", "Rumi and Vicky, who are in love, are caught by Rumi's family and pressurised to get married. However, when Vicky refuses to commit, a marriage broker brings in Robbie as a prospective suitor.");
                intent.putExtra("img", R.drawable.manmarziyaanp);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.hold);
            }
        });
        v.findViewById(R.id.nawabzadel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), VdoDetailsActivity.class);
                intent.putExtra("url", "http://wowmaxmovies.com/video/nawabzaade.mp4");
                intent.putExtra("name", "Nawabzaade");
                intent.putExtra("cat", "Rommance and Comedy");
                intent.putExtra("rel", "2018");
                intent.putExtra("qlt", "Full hd");
                intent.putExtra("dur", "1.52 hr");
                intent.putExtra("desp", "When three best friends fall in love with the same girl, their friendship is challenged. However, they soon realize that there is more to her than her looks.");
                intent.putExtra("img", R.drawable.nawabazadep);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.hold);
            }
        });
        v.findViewById(R.id.bajiraomastanil).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), VdoDetailsActivity.class);
                intent.putExtra("url", "http://wowmaxmovies.com/video/bajiraomastani.mkv");
                intent.putExtra("name", "bajiraomastani");
                intent.putExtra("cat", "Rommance and action");
                intent.putExtra("rel", "2015");
                intent.putExtra("qlt", "Full hd");
                intent.putExtra("dur", "2.15 hr");
                intent.putExtra("desp", "The heroic Peshwa Bajirao, married to Kashibai, falls in love with Mastani, a warrior princess in distress. They struggle to make their love triumph amid opposition from his conservative family.");
                intent.putExtra("img", R.drawable.bajiraomastanip);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.hold);
            }
        });

        return v;

    }

    private void forAdverties(View v) {


        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        mAdView = v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        adView = v.findViewById(R.id.adVieww);
        AdRequest adRequestt = new AdRequest.Builder().build();
        adView.loadAd(adRequestt);
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