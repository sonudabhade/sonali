package com.tradegenie.android.tradegenieapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tradegenie.android.tradegenieapp.Fragments.AddNewProduct;
import com.tradegenie.android.tradegenieapp.Models.AddProductImagesModel;
import com.tradegenie.android.tradegenieapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 *
 */
public class AdapterAddProductImages extends RecyclerView.Adapter<AdapterAddProductImages.MyViewHolderAddProductImage> {



    private Context mContext;
    private ArrayList<AddProductImagesModel> mAddProductImagesModelArrayList;
    private AddNewProduct mAddNewProduct;

    public AdapterAddProductImages(Context mContext, ArrayList<AddProductImagesModel> addProductImagesModelArrayList, AddNewProduct addNewProduct) {
        this.mContext = mContext;
        this.mAddProductImagesModelArrayList = addProductImagesModelArrayList;
        mAddNewProduct = addNewProduct;
    }

    @NonNull
    @Override
    public MyViewHolderAddProductImage onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_add_product_image, null);
        return new MyViewHolderAddProductImage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolderAddProductImage myViewHolderUploadCertificate, final int i) {

        myViewHolderUploadCertificate.imgSpecialImageView.setImageBitmap(mAddProductImagesModelArrayList.get(i).getDocumentBitmap());

        myViewHolderUploadCertificate.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAddNewProduct.ShowCameraGallryOption(i);

            }
        });


    }

    @Override
    public int getItemCount() {
        return mAddProductImagesModelArrayList.size();
    }


    public class MyViewHolderAddProductImage extends RecyclerView.ViewHolder {

        @BindView(R.id.img_special_image_view)
        ImageView imgSpecialImageView;

        public MyViewHolderAddProductImage(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
