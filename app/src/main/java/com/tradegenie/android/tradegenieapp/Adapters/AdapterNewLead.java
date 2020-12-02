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

import com.tradegenie.android.tradegenieapp.Fragments.NewLeadFragment;
import com.tradegenie.android.tradegenieapp.Models.NewLeadModel;
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
public class AdapterNewLead extends RecyclerView.Adapter<AdapterNewLead.MyViewHolderNewLead> {


    private Context mContext;
    private ArrayList<NewLeadModel> mNewLeadModelArrayList;

    public AdapterNewLead(Context mContext, ArrayList<NewLeadModel> NewLeadModelArrayList) {
        this.mContext = mContext;
        this.mNewLeadModelArrayList = NewLeadModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolderNewLead onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_new_lead, null);
        return new MyViewHolderNewLead(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderNewLead myViewHolderNewLead, final int i) {


        if (mNewLeadModelArrayList.get(i).getPost_type().equals("1")) {


            myViewHolderNewLead.tvProductName.setText(mNewLeadModelArrayList.get(i).getProduct_name());


        } else {


            myViewHolderNewLead.tvProductName.setText(mNewLeadModelArrayList.get(i).getEnquiry_type());


        }


        String date = mNewLeadModelArrayList.get(i).getCreated_date();
        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date newDate = null;
        try {
            newDate = spf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        date = spf.format(newDate);
        myViewHolderNewLead.tvDate.setText(date);

        myViewHolderNewLead.tvQty.setText("Required Quantity : " + mNewLeadModelArrayList.get(i).getQty());
        myViewHolderNewLead.tvPriceRange.setText("Price Range : " + mNewLeadModelArrayList.get(i).getEn_currency() + " " + mNewLeadModelArrayList.get(i).getPrice_from() + " to " + mNewLeadModelArrayList.get(i).getPrice_to());
        myViewHolderNewLead.tvPrice.setText(mNewLeadModelArrayList.get(i).getCurrency() + " " + mNewLeadModelArrayList.get(i).getLeads());

        myViewHolderNewLead.linearLayoutBuyLead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NewLeadFragment newLeadFragment = new NewLeadFragment();
                Bundle bundle = new Bundle();
                bundle.putString("pk_id", mNewLeadModelArrayList.get(i).getPk_id());
                newLeadFragment.setArguments(bundle);
                Constants.mMainActivity.ChangeFragments(newLeadFragment, "NewLeadFragment");

            }
        });

        if (mNewLeadModelArrayList.get(i).getLeads().equals("")) {
            myViewHolderNewLead.linearLayoutBuyLead.setVisibility(View.GONE);
        }else{
            myViewHolderNewLead.linearLayoutBuyLead.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return mNewLeadModelArrayList.size();
    }


    public class MyViewHolderNewLead extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_product_name)
        TextView tvProductName;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_qty)
        TextView tvQty;
        @BindView(R.id.tv_price_range)
        TextView tvPriceRange;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.linear_layout_buy_lead)
        LinearLayout linearLayoutBuyLead;

        public MyViewHolderNewLead(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}
