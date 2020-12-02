package com.tradegenie.android.tradegenieapp.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tradegenie.android.tradegenieapp.Fragments.MyLeadFragment;
import com.tradegenie.android.tradegenieapp.Fragments.NewLeadFragment;
import com.tradegenie.android.tradegenieapp.Models.MyLeadModel;
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
public class    AdapterMyLead extends RecyclerView.Adapter<AdapterMyLead.myViewHolderMyLead> {



    private Context mContext;
    private ArrayList<MyLeadModel> mMyLeadModelArrayList;

    public AdapterMyLead(Context mContext, ArrayList<MyLeadModel> myLeadModelArrayList) {
        this.mContext = mContext;
        this.mMyLeadModelArrayList = myLeadModelArrayList;
    }

    @NonNull
    @Override
    public myViewHolderMyLead onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_my_lead, null);
        return new myViewHolderMyLead(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolderMyLead myViewHolderMyLead, final int i) {


        myViewHolderMyLead.tvProductName.setText(mMyLeadModelArrayList.get(i).getProduct_name());

        String date=mMyLeadModelArrayList.get(i).getCreated_date();
        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date newDate = null;
        try {
            newDate = spf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        date = spf.format(newDate);
        myViewHolderMyLead.tvDate.setText(date);

        myViewHolderMyLead.tvQnt.setText("Required Quantity : "+mMyLeadModelArrayList.get(i).getQuantity());
        myViewHolderMyLead.tvPriceRange.setText("Price Range : "+mMyLeadModelArrayList.get(i).getEn_currency()+" "+mMyLeadModelArrayList.get(i).getPrice_from()+" to "+mMyLeadModelArrayList.get(i).getPrice_to());
       // myViewHolderMyLead.tvPrice.setText(mNewLeadModelArrayList.get(i).getCurrency()+" "+mNewLeadModelArrayList.get(i).getLeads());

        String pincode="";

        if (mMyLeadModelArrayList.get(i).getPincode().equals("0")){

            pincode="";

        }else {

            pincode=mMyLeadModelArrayList.get(i).getPincode();

        }




        myViewHolderMyLead.linearLayoutViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyLeadFragment myLeadFragment=new MyLeadFragment();
                Bundle bundle=new Bundle();
                bundle.putString("pk_id",mMyLeadModelArrayList.get(i).getPk_id());
                myLeadFragment.setArguments(bundle);
                Constants.mMainActivity.ChangeFragments(myLeadFragment,"MyLeadFragment");

            }
        });


    }

    @Override
    public int getItemCount() {
        return mMyLeadModelArrayList.size();
    }


    public class myViewHolderMyLead extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_product_name)
        TextView tvProductName;
        @BindView(R.id.tv_qnt)
        TextView tvQnt;
        @BindView(R.id.tv_price_range)
        TextView tvPriceRange;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.linear_layout_view_details)
        LinearLayout linearLayoutViewDetails;

        public myViewHolderMyLead(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}
