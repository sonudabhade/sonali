package com.tradegenie.android.tradegenieapp.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tradegenie.android.tradegenieapp.Adapters.MyViewPagerAdapter;
import com.tradegenie.android.tradegenieapp.Adapters.SplashScreenMyViewPagerAdapter;
import com.tradegenie.android.tradegenieapp.Models.AdsItem;
import com.tradegenie.android.tradegenieapp.Models.SplashScreenItem;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;
import com.tradegenie.android.tradegenieapp.Utility.SessionManager;
import com.tradegenie.android.tradegenieapp.Utility.UtilityMethods;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tradegenie.android.tradegenieapp.Utility.Constants.USER_LANG;

public class SplashScreenActivity extends AppCompatActivity {

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.RECORD_AUDIO

    };

    SplashScreenMyViewPagerAdapter splashScreenMyViewPagerAdapter;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.layoutDots)
    LinearLayout layoutDots;
    @BindView(R.id.relative_layout_view_pager)
    RelativeLayout relativeLayoutViewPager;
    @BindView(R.id.btn_buyer_splash_screen)
    TextView btnBuyerSplashScreen;
    @BindView(R.id.btn_seller_splash_screen)
    TextView btnSellerSplashScreen;
    @BindView(R.id.relativeLayoutSplashScreen)
    RelativeLayout relativeLayoutSplashScreen;
    private ArrayList<SplashScreenItem> adsArrayList;
    private TextView dots[];
    int NUM_PAGES, currentPage;
    Timer timer;

    private SessionManager mSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        mSessionManager = new SessionManager(SplashScreenActivity.this);


        //mSessionManager.removeAll();

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        setLocale();
        if (!mSessionManager.getStringData(Constants.KEY_USER_TYPE).equalsIgnoreCase("")) {

            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);

            if (mSessionManager.getStringData(Constants.KEY_SELLER_USER_NAME).equalsIgnoreCase("null") || mSessionManager.getStringData(Constants.KEY_SELLER_USER_NAME).equalsIgnoreCase("")) {

                intent.putExtra("loginUser", "new");

            } else {

                intent.putExtra("loginUser", "old");

            }

            startActivity(intent);

        } else {


        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;



        adsArrayList = new ArrayList();
        adsArrayList.add(new SplashScreenItem("", R.drawable.screen1, "Increase Sale and Reduce Cost"));
        adsArrayList.add(new SplashScreenItem("", R.drawable.screen2, "Increase Sale and Reduce Cost"));
        adsArrayList.add(new SplashScreenItem("", R.drawable.screen3, "Search your requirement through thousands of sellers"));
        adsArrayList.add(new SplashScreenItem("", R.drawable.screen4, "Search your requirement through thousands of sellers"));
        adsArrayList.add(new SplashScreenItem("", R.drawable.screen5, "Search your requirement through thousands of sellers"));
        splashScreenMyViewPagerAdapter = new SplashScreenMyViewPagerAdapter(SplashScreenActivity.this, adsArrayList,width);
        viewPager.setClipToPadding(false);
        //viewPager.setPadding(120, 0, 120, 0);
        viewPager.setAdapter(splashScreenMyViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        // adding bottom dots
        NUM_PAGES = adsArrayList.size();
        addBottomDots(0);
        SetSliderAutoTimer();
    }

    private void setLocale() {
        try {
            String lang = mSessionManager.getStringData(USER_LANG);
            if (lang.equalsIgnoreCase("Sinhala")){
                lang = "si";
            }else {
                lang = "en";
            }
            Locale myLocale = new Locale(lang);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
        }catch (Exception e){e.printStackTrace();}
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {

            currentPage = position;
            addBottomDots(position);

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {


        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    //Method for add dots on bottom
    private void addBottomDots(int currentPage) {
        dots = new TextView[adsArrayList.size()];

        layoutDots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(SplashScreenActivity.this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.colorGray));
            layoutDots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(getResources().getColor(R.color.colorWhite));
    }

    //Method to set timer to slider
    private void SetSliderAutoTimer() {
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                try {
                    viewPager.setCurrentItem(currentPage++, true);
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 100, 2000);
    }

    @OnClick({R.id.btn_buyer_splash_screen, R.id.btn_seller_splash_screen})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_buyer_splash_screen:

                Intent intentSignUpOrSignInActivity = new Intent(SplashScreenActivity.this, SignUpOrSignInActivity.class);
                intentSignUpOrSignInActivity.putExtra("usertype", "buyer");
                startActivity(intentSignUpOrSignInActivity);

//                Intent intentMainActivity = new Intent(SplashScreenActivity.this, MainActivity.class);
//                intentMainActivity.putExtra("usertype", "buyer");
//                startActivity(intentMainActivity);


                break;
            case R.id.btn_seller_splash_screen:

//                Intent intentMainActivity2 = new Intent(SplashScreenActivity.this, MainActivity.class);
//                intentMainActivity2.putExtra("usertype", "seller");
//                startActivity(intentMainActivity2);

                Intent intentSignUpOrSignInActivity2 = new Intent(SplashScreenActivity.this, SignUpOrSignInActivity.class);
                intentSignUpOrSignInActivity2.putExtra("usertype", "seller");
                startActivity(intentSignUpOrSignInActivity2);

                break;
        }
    }

    //for permissions
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}