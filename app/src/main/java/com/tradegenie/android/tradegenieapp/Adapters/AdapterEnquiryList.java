package com.tradegenie.android.tradegenieapp.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tradegenie.android.tradegenieapp.Fragments.EnquiryDetailsFragment;
import com.tradegenie.android.tradegenieapp.Models.EnquiryListModel;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 *
 */
public class AdapterEnquiryList extends RecyclerView.Adapter<AdapterEnquiryList.MyViewHolderEnquiryList> {



    private Context mContext;
    private ArrayList<EnquiryListModel> mEnquiryListModelArrayList;

    public AdapterEnquiryList(Context mContext, ArrayList<EnquiryListModel> EnquiryListModelArrayList) {
        this.mContext = mContext;
        this.mEnquiryListModelArrayList = EnquiryListModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolderEnquiryList onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_enquiry_list, null);
        return new MyViewHolderEnquiryList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderEnquiryList myViewHolderEnquiryList, final int i) {

        myViewHolderEnquiryList.tvProductName.setText(mEnquiryListModelArrayList.get(i).getProduct_name());
        myViewHolderEnquiryList.tvQty.setText("Required Quantity : " + mEnquiryListModelArrayList.get(i).getQty());
        myViewHolderEnquiryList.tvPriceRange.setText("Price Range : " + mEnquiryListModelArrayList.get(i).getCurrency() + " " + mEnquiryListModelArrayList.get(i).getPrice_from() + " to " + mEnquiryListModelArrayList.get(i).getPrice_to());

        String date = mEnquiryListModelArrayList.get(i).getCreated_date();

        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date StartDate = null;
        try {
            StartDate = spf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        spf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        date = spf.format(StartDate);

        myViewHolderEnquiryList.tvDate.setText(date);

        Log.e("check","===>"+mEnquiryListModelArrayList.get(i).getPaid_status());

        if (mEnquiryListModelArrayList.get(i).getPaid_status().equals("2")){

            myViewHolderEnquiryList.tvWaiting.setVisibility(View.GONE);

        }else {

            myViewHolderEnquiryList.tvWaiting.setVisibility(View.VISIBLE);


        }

        myViewHolderEnquiryList.linearLayoutViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EnquiryDetailsFragment enquiryDetailsFragment = new EnquiryDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("pk_id", mEnquiryListModelArrayList.get(i).getPk_id());
                bundle.putString("product_name", mEnquiryListModelArrayList.get(i).getProduct_name());
                bundle.putString("qty", mEnquiryListModelArrayList.get(i).getQty());
                bundle.putString("price_from", mEnquiryListModelArrayList.get(i).getPrice_from());
                bundle.putString("price_to", mEnquiryListModelArrayList.get(i).getPrice_to());
                bundle.putString("created_date", mEnquiryListModelArrayList.get(i).getCreated_date());
                bundle.putString("descripation", mEnquiryListModelArrayList.get(i).getDescripation());
                bundle.putString("currency", mEnquiryListModelArrayList.get(i).getCurrency());
                bundle.putString("lead_id", mEnquiryListModelArrayList.get(i).getLead_id());
                bundle.putString("preferred", mEnquiryListModelArrayList.get(i).getPreferred());
                bundle.putString("type_of_buyer", mEnquiryListModelArrayList.get(i).getType_of_buyer());
                bundle.putString("seller_fkid", mEnquiryListModelArrayList.get(i).getSeller_fkid());
                bundle.putString("paid_status", mEnquiryListModelArrayList.get(i).getPaid_status());
                bundle.putString("userName", mEnquiryListModelArrayList.get(i).getUserName());
                bundle.putString("UA_pkey", mEnquiryListModelArrayList.get(i).getUA_pkey());
                bundle.putString("UA_email", mEnquiryListModelArrayList.get(i).getUA_email());
                bundle.putString("UA_mobile", mEnquiryListModelArrayList.get(i).getUA_mobile());
                bundle.putString("attributes_type_name", mEnquiryListModelArrayList.get(i).getAttributes_type_name());
                enquiryDetailsFragment.setArguments(bundle);
                Constants.mMainActivity.ChangeFragments(enquiryDetailsFragment, "EnquiryDetailsFragment");

            }
        });


    }

    @Override
    public int getItemCount() {
        return mEnquiryListModelArrayList.size();
    }


    public class MyViewHolderEnquiryList extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_product_name)
        TextView tvProductName;
        @BindView(R.id.tv_qty)
        TextView tvQty;
        @BindView(R.id.tv_price_range)
        TextView tvPriceRange;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_waiting)
        TextView tvWaiting;
        @BindView(R.id.linear_layout_view_details)
        LinearLayout linearLayoutViewDetails;

        public MyViewHolderEnquiryList(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}
