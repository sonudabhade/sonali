package com.tradegenie.android.tradegenieapp.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tradegenie.android.tradegenieapp.Fragments.CategoryFragment;
import com.tradegenie.android.tradegenieapp.Models.CategoryModel;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 *
 */
public class AdapterHomePageItemList extends RecyclerView.Adapter<AdapterHomePageItemList.MyViewHolderHomeItemList> {

    private Context mContext;

    private ArrayList<CategoryModel> mCategoryModelArrayList;

    public AdapterHomePageItemList(Context mContext, ArrayList<CategoryModel> categoryModelArrayList) {
        this.mContext = mContext;
        this.mCategoryModelArrayList = categoryModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolderHomeItemList onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_home_item, null);
        return new MyViewHolderHomeItemList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderHomeItemList myViewHolderHomeItemList, final int i) {


        myViewHolderHomeItemList.tvCategoryName.setText(mCategoryModelArrayList.get(i).getCat_name());

        try {
            Picasso.get().load(mCategoryModelArrayList.get(i).getCat_image()).placeholder(R.drawable.default_document).into(myViewHolderHomeItemList.ivCategoryName);
        } catch (Exception e) {

        }

        myViewHolderHomeItemList.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CategoryFragment categoryFragment = new CategoryFragment();
                Bundle bundle=new Bundle();
                bundle.putString("category_id",mCategoryModelArrayList.get(i).getPk_id());
                categoryFragment.setArguments(bundle);
                Constants.mMainActivity.ChangeFragments(categoryFragment, "CategoryFragment");


            }
        });

    }

    @Override
    public int getItemCount() {
        return mCategoryModelArrayList.size();
    }


    public class MyViewHolderHomeItemList extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_category_name)
        TextView tvCategoryName;
        @BindView(R.id.iv_category_name)
        ImageView ivCategoryName;

        public MyViewHolderHomeItemList(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}
