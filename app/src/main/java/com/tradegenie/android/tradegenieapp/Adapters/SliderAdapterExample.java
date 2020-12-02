package com.tradegenie.android.tradegenieapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;
import com.tradegenie.android.tradegenieapp.Models.AdvCategoryModel;
import com.tradegenie.android.tradegenieapp.R;

import java.util.ArrayList;

public class SliderAdapterExample extends SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {

    private Context context;
    private ArrayList<AdvCategoryModel> mAdvCategoryModelArrayList;

    public SliderAdapterExample(Context context, ArrayList<AdvCategoryModel> AdvCategoryModelArrayList) {
        this.context = context;
        mAdvCategoryModelArrayList = AdvCategoryModelArrayList;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {
        // viewHolder.textViewDescription.setText("This is slider item " + position);

        Log.e("check", "mAdvCategoryModelArrayList" + mAdvCategoryModelArrayList.size());
        Log.e("check", "mAdvCategoryModelArrayList" + mAdvCategoryModelArrayList.get(position).getImg());

        try {
            Picasso.get().load(mAdvCategoryModelArrayList.get(position).getImg()).into(viewHolder.imageViewBackground);
        } catch (Exception e) {
            e.printStackTrace();
        }


        viewHolder.imageViewBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mAdvCategoryModelArrayList.get(position).getUrl().equals("")) {
                    Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                    httpIntent.setData(Uri.parse(mAdvCategoryModelArrayList.get(position).getUrl()));
                    context.startActivity(httpIntent);
                }
            }
        });

    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mAdvCategoryModelArrayList.size();
    }


    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        TextView textViewDescription;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }


}


