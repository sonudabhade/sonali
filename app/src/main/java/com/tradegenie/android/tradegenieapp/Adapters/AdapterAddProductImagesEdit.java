package com.tradegenie.android.tradegenieapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tradegenie.android.tradegenieapp.Fragments.AddNewProduct;
import com.tradegenie.android.tradegenieapp.Fragments.AddNewProductEdit;
import com.tradegenie.android.tradegenieapp.Models.EditProductImageModel;
import com.tradegenie.android.tradegenieapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 *
 */
public class AdapterAddProductImagesEdit extends RecyclerView.Adapter<AdapterAddProductImagesEdit.MyViewHolderAddProductImage> {



    private Context mContext;
    private ArrayList<EditProductImageModel> mAddProductImagesModelArrayList;
    private AddNewProductEdit mAddAddNewProductEdit;

    public AdapterAddProductImagesEdit(Context mContext, ArrayList<EditProductImageModel> addProductImagesModelArrayList, AddNewProductEdit addNewProductEdit) {
        this.mContext = mContext;
        this.mAddProductImagesModelArrayList = addProductImagesModelArrayList;
        mAddAddNewProductEdit = addNewProductEdit;
    }

    @NonNull
    @Override
    public MyViewHolderAddProductImage onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_add_product_image, null);
        return new MyViewHolderAddProductImage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolderAddProductImage myViewHolderUploadCertificate, final int i) {


        if (mAddProductImagesModelArrayList.get(i).getDocumentBitmap()==null&&!mAddProductImagesModelArrayList.get(i).getImage_name().equals("")){

            try {
                Picasso.get().load(mAddProductImagesModelArrayList.get(i).getImage_name()).placeholder(R.drawable.default_add_image).into(myViewHolderUploadCertificate.imgSpecialImageView);
            } catch (Exception e) {

            }

        }else {

            myViewHolderUploadCertificate.imgSpecialImageView.setImageBitmap(mAddProductImagesModelArrayList.get(i).getDocumentBitmap());

        }



        myViewHolderUploadCertificate.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAddAddNewProductEdit.ShowCameraGallryOption(i);

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
