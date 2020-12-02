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
import com.tradegenie.android.tradegenieapp.Models.AdvCategoryModel;
import com.tradegenie.android.tradegenieapp.R;

import java.util.ArrayList;

public class AdapterAdvCategoryViewPager extends PagerAdapter {
    private LayoutInflater layoutInflater;
    ArrayList<AdvCategoryModel> Ads;
    Context mContext;
    public AdapterAdvCategoryViewPager(Context mContext, ArrayList<AdvCategoryModel> Ads) {
        this.Ads=Ads;
        this.mContext=mContext;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.row_ads, container, false);
        final ImageView imgAd = view.findViewById(R.id.imgAd);
        Picasso.get().load(Ads.get(position).getImg()).into(imgAd);
        container.addView(view);

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