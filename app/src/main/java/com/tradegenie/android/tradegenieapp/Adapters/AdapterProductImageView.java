package com.tradegenie.android.tradegenieapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tradegenie.android.tradegenieapp.Fragments.ProductViewFragment;
import com.tradegenie.android.tradegenieapp.Models.ProductImageViewModel;
import com.tradegenie.android.tradegenieapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 *
 */
public class AdapterProductImageView extends RecyclerView.Adapter<AdapterProductImageView.MyViewHolderProductImageView> {



    private Context mContext;
    private ArrayList<ProductImageViewModel> mProductImageViewModelArrayList;
    private ProductViewFragment mProductViewFragment;

    public AdapterProductImageView(Context mContext, ArrayList<ProductImageViewModel> mProductImageViewModelArrayList,ProductViewFragment productViewFragment) {
        this.mContext = mContext;
        this.mProductImageViewModelArrayList = mProductImageViewModelArrayList;
        mProductViewFragment=productViewFragment;
    }

    @NonNull
    @Override
    public MyViewHolderProductImageView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_product_image_view, null);
        return new MyViewHolderProductImageView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderProductImageView myViewHolderProductImageView, final int i) {


        try {
            Picasso.get().load(mProductImageViewModelArrayList.get(i).getImage()).placeholder(R.drawable.default_add_image).into(myViewHolderProductImageView.ivAdapterImageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        myViewHolderProductImageView.ivAdapterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProductViewFragment.ChangeImage(i);

            }
        });



    }

    @Override
    public int getItemCount() {
        return mProductImageViewModelArrayList.size();
    }


    public class MyViewHolderProductImageView extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_adapter_image_view)
        ImageView ivAdapterImageView;

        public MyViewHolderProductImageView(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}
