package com.tradegenie.android.tradegenieapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;
import com.tradegenie.android.tradegenieapp.Models.AdsItem;
import com.tradegenie.android.tradegenieapp.R;

import java.util.ArrayList;

public class MyViewPagerAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;
    ArrayList<AdsItem> Ads;
    Context mContext;
    public MyViewPagerAdapter(Context mContext, ArrayList<AdsItem> Ads) {
        this.Ads=Ads;
        this.mContext=mContext;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.row_ads, container, false);
        final ImageView imgAd = view.findViewById(R.id.imgAd);
        try {
            Picasso.get().load(Ads.get(position).getImg()).into(imgAd);
        } catch (Exception e) {
            e.printStackTrace();
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!String.valueOf(Ads.get(position).getUrl()).equalsIgnoreCase("")) {
                    Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                    httpIntent.setData(Uri.parse(String.valueOf(Ads.get(position).getUrl())));
                    mContext.startActivity(httpIntent);
                }else {


                }
            }
        });

        container.addView(view);

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