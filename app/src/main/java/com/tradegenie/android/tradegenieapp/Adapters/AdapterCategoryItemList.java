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
import com.tradegenie.android.tradegenieapp.Fragments.SubCategoryFragment;
import com.tradegenie.android.tradegenieapp.Models.SubCategoryModel;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

;


/**
 *
 */
public class AdapterCategoryItemList extends RecyclerView.Adapter<AdapterCategoryItemList.MyViewHolderCategoryItemList> {



    private Context mContext;
    private ArrayList<SubCategoryModel> mSubcategSubCategoryModelArrayList;
    private String mSelectedCategory;

    public AdapterCategoryItemList(Context mContext, ArrayList<SubCategoryModel> subCategoryModelArrayList,String SelectedCategory) {
        this.mContext = mContext;
        this.mSubcategSubCategoryModelArrayList = subCategoryModelArrayList;
        mSelectedCategory=SelectedCategory;
    }

    @NonNull
    @Override
    public MyViewHolderCategoryItemList onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_category_item, null);
        return new MyViewHolderCategoryItemList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderCategoryItemList myViewHolderCategoryItemList, final int i) {


        myViewHolderCategoryItemList.tvSubCategoryName.setText(mSubcategSubCategoryModelArrayList.get(i).getTitle());

        try {
            Picasso.get().load(mSubcategSubCategoryModelArrayList.get(i).getSubcat_image()).placeholder(R.drawable.default_document).into(myViewHolderCategoryItemList.ivSubCategory);
        } catch (Exception e) {

        }

        myViewHolderCategoryItemList.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SubCategoryFragment subCategoryFragment = new SubCategoryFragment();
                Bundle bundle=new Bundle();
                bundle.putString("mSelectedSubCategory",mSubcategSubCategoryModelArrayList.get(i).getPk_id());
                bundle.putString("mSelectedCategory",mSelectedCategory);
                subCategoryFragment.setArguments(bundle);
                Constants.mMainActivity.ChangeFragments(subCategoryFragment, "SubCategoryFragment");

            }
        });

    }

    @Override
    public int getItemCount() {
        return mSubcategSubCategoryModelArrayList.size();
    }


    public class MyViewHolderCategoryItemList extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_sub_category)
        ImageView ivSubCategory;
        @BindView(R.id.tv_sub_category_name)
        TextView tvSubCategoryName;

        public MyViewHolderCategoryItemList(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}
