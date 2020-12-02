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
import com.tradegenie.android.tradegenieapp.Fragments.ProductListBuyFragment;
import com.tradegenie.android.tradegenieapp.Models.SubSubCategoryModel;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 *
 */
public class AdapterSubCategoryItemList extends RecyclerView.Adapter<AdapterSubCategoryItemList.MyViewHolderSubCategoryItemList> {



    private Context mContext;
    private ArrayList<SubSubCategoryModel> mSubSubCategoryModelArrayList;
    private String mSelectedCategory,mSelectedSubCategory;

    public AdapterSubCategoryItemList(Context mContext, ArrayList<SubSubCategoryModel> subSubCategoryModelArrayList,String selectedCategory,String selectedSubCategory) {
        this.mContext = mContext;
        this.mSubSubCategoryModelArrayList = subSubCategoryModelArrayList;
        mSelectedCategory=selectedCategory;
        mSelectedSubCategory=selectedSubCategory;
    }

    @NonNull
    @Override
    public MyViewHolderSubCategoryItemList onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_sub_category_item, null);
        return new MyViewHolderSubCategoryItemList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderSubCategoryItemList myViewHolderSubCategoryItemList, final int i) {



        myViewHolderSubCategoryItemList.tvSubSubCategory.setText(mSubSubCategoryModelArrayList.get(i).getTitle());

        try {
            Picasso.get().load(mSubSubCategoryModelArrayList.get(i).getSubcat_image()).placeholder(R.drawable.default_document).into(myViewHolderSubCategoryItemList.ivSubCategory);
        } catch (Exception e) {

        }

        myViewHolderSubCategoryItemList.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ProductListBuyFragment productListBuyFragment = new ProductListBuyFragment();
                Bundle bundle=new Bundle();
                bundle.putString("mSelectedCategory",mSelectedCategory);
                bundle.putString("mSelectedSubCategory",mSelectedSubCategory);
                bundle.putString("mSelectedSubSubCategory",mSubSubCategoryModelArrayList.get(i).getPk_id());
                bundle.putString("FromHomeScreen","FromHomeScreen");
                productListBuyFragment.setArguments(bundle);
                Constants.mMainActivity.ChangeFragments(productListBuyFragment, "ProductListBuyFragment");

            }
        });

    }

    @Override
    public int getItemCount() {
        return mSubSubCategoryModelArrayList.size();
    }


    public class MyViewHolderSubCategoryItemList extends RecyclerView.ViewHolder {


        @BindView(R.id.iv_sub_category)
        ImageView ivSubCategory;
        @BindView(R.id.tv_sub_sub_category)
        TextView tvSubSubCategory;

        public MyViewHolderSubCategoryItemList(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }
    }

}
