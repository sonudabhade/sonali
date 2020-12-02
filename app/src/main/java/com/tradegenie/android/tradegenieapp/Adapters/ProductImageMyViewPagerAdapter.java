package com.tradegenie.android.tradegenieapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tradegenie.android.tradegenieapp.Models.ProductImageViewModel;
import com.tradegenie.android.tradegenieapp.Models.ViewPagerProductImageModel;
import com.tradegenie.android.tradegenieapp.R;

import java.util.ArrayList;

public class ProductImageMyViewPagerAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;
    ArrayList<ViewPagerProductImageModel> Ads;
    Context mContext;
    public ProductImageMyViewPagerAdapter(Context mContext, ArrayList<ViewPagerProductImageModel> Ads) {
        this.Ads=Ads;
        this.mContext=mContext;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.adapter_product_image_slider_view, container, false);
        final ImageView imgAd = view.findViewById(R.id.imgAd);

        try {
            Picasso.get().load(Ads.get(position).getImage()).into(imgAd);
        } catch (Exception e) {
            e.printStackTrace();
        }


        container.addView(view);

        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!String.valueOf(Ads.get(position).getImage()).equalsIgnoreCase("")) {
                    Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                    httpIntent.setData(Uri.parse(String.valueOf(Ads.get(position).getImage())));
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