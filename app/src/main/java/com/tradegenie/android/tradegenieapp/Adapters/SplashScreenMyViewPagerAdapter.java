package com.tradegenie.android.tradegenieapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tradegenie.android.tradegenieapp.Models.SplashScreenItem;
import com.tradegenie.android.tradegenieapp.R;

import java.util.ArrayList;

public class SplashScreenMyViewPagerAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;
    ArrayList<SplashScreenItem> Ads;
    Context mContext;
    int width;

    public SplashScreenMyViewPagerAdapter(Context mContext, ArrayList<SplashScreenItem> Ads, int width) {
        this.Ads = Ads;
        this.mContext = mContext;
        this.width = width;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.row_splash_item, container, false);
        final ImageView imgAd = view.findViewById(R.id.imgAd);
        final TextView tv_sub_text = view.findViewById(R.id.tv_sub_text);


        imgAd.getLayoutParams().height = width;
        imgAd.getLayoutParams().width = width;
        Picasso.get().load(Ads.get(position).getImage()).into(imgAd);
        container.addView(view);

        tv_sub_text.setText(Ads.get(position).getLink());

        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!String.valueOf(Ads.get(position).getLink()).equalsIgnoreCase("")) {
                    Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                    httpIntent.setData(Uri.parse(String.valueOf(Ads.get(position).getLink())));
                    mContext.startActivity(httpIntent);
                }else {


                }
            }
        });*/


        return view;
    }

    @Override
    public int getCount() {
        return Ads.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}