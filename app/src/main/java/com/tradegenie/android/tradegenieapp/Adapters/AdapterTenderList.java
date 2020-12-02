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

import com.tradegenie.android.tradegenieapp.Fragments.MyTenderDetailFragment;
import com.tradegenie.android.tradegenieapp.Models.TenderModel;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 *
 */
public class AdapterTenderList extends RecyclerView.Adapter<AdapterTenderList.MyViewHolderTender> {


    private Context mContext;
    private ArrayList<TenderModel> mTenderModelArrayList;
    String From;


    public AdapterTenderList(Context mContext, ArrayList<TenderModel> TenderModelArrayList,String From) {
        this.mContext = mContext;
        this.From = From;
        this.mTenderModelArrayList = TenderModelArrayList;

    }

    @NonNull
    @Override
    public MyViewHolderTender onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_tender_list, null);
        return new MyViewHolderTender(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderTender myViewHolderTender, final int i) {
        try {
            myViewHolderTender.linearLayoutViewDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    MyTenderDetailFragment myTenderDetailFragment = new MyTenderDetailFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("tender_id", mTenderModelArrayList.get(i).getPk_id());
                    bundle.putString("From",From);
                    myTenderDetailFragment.setArguments(bundle);
                    Constants.mMainActivity.ChangeFragments(myTenderDetailFragment, "MyTenderDetailFragment");
                }
            });


            myViewHolderTender.tvTenderId.setText(mTenderModelArrayList.get(i).getPk_id());
            myViewHolderTender.tvTenderName.setText(mTenderModelArrayList.get(i).getTender_name());
            try {
                myViewHolderTender.tvDate.setText(ConvertDate(mTenderModelArrayList.get(i).getCreated_date()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            myViewHolderTender.tvCost.setText(mTenderModelArrayList.get(i).getCurrency() + " " + mTenderModelArrayList.get(i).getCost());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Method to convert date
    private String ConvertDate(String created_at) {

        DateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.ENGLISH);
        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = originalFormat.parse(created_at);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String formattedDate = targetFormat.format(date);

        return formattedDate;
    }


    @Override
    public int getItemCount() {
        return mTenderModelArrayList.size();
    }


    public class MyViewHolderTender extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_tender_id)
        TextView tvTenderId;
        @BindView(R.id.tv_tender_name)
        TextView tvTenderName;
        @BindView(R.id.tv_cost)
        TextView tvCost;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.linear_layout_view_details)
        LinearLayout linearLayoutViewDetails;


        public MyViewHolderTender(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}
