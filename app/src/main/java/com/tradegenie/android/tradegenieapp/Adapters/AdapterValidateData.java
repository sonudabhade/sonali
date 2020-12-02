package com.tradegenie.android.tradegenieapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tradegenie.android.tradegenieapp.Models.ValidateDataModel;
import com.tradegenie.android.tradegenieapp.R;

import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 *
 */
public class AdapterValidateData extends RecyclerView.Adapter<AdapterValidateData.MyViewHolderValidateData> {



    private Context mContext;
    private ArrayList<ValidateDataModel> mValidateDataModelArrayList;

    public AdapterValidateData(Context mContext, ArrayList<ValidateDataModel> validateDataModelArrayList) {
        this.mContext = mContext;
        this.mValidateDataModelArrayList = validateDataModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolderValidateData onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_validate_data, null);
        return new MyViewHolderValidateData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderValidateData myViewHolderValidateData, int i) {

        Picasso.get().load(mValidateDataModelArrayList.get(i).getImage()).placeholder(R.drawable.ic_verification).into(myViewHolderValidateData.ivTags);


        myViewHolderValidateData.tvTitle.setText(mValidateDataModelArrayList.get(i).getTitle());

    }

    @Override
    public int getItemCount() {
        return mValidateDataModelArrayList.size();
    }


    public class MyViewHolderValidateData extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_tags)
        ImageView ivTags;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        public MyViewHolderValidateData(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}
