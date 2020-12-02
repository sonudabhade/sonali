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

import com.tradegenie.android.tradegenieapp.Fragments.EnquiryDetailsFragment;
import com.tradegenie.android.tradegenieapp.Fragments.EnquiryDetailsPostRequirementFragment;
import com.tradegenie.android.tradegenieapp.Models.PostRequirementModel;
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
public class AdapterPostRequirementList extends RecyclerView.Adapter<AdapterPostRequirementList.MyViewHolderPostRequirement> {

    private Context mContext;
    private ArrayList<PostRequirementModel> mPostRequirementModelArrayList;
    String pk_id;

    public AdapterPostRequirementList(Context mContext, ArrayList<PostRequirementModel> PostRequirementModelArrayList, String pk_id) {
        this.mContext = mContext;
        this.pk_id = pk_id;
        this.mPostRequirementModelArrayList = PostRequirementModelArrayList;

    }

    @NonNull
    @Override
    public MyViewHolderPostRequirement onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_post_requirement_list, null);
        return new MyViewHolderPostRequirement(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderPostRequirement myViewHolderPostRequirement, final int i) {


        if (mPostRequirementModelArrayList.get(i).getPost_type().equals("2")) {

            myViewHolderPostRequirement.tvProductName.setText(mPostRequirementModelArrayList.get(i).getEnquiry_type());


        } else {

            myViewHolderPostRequirement.tvProductName.setText(mPostRequirementModelArrayList.get(i).getProduct_name());

        }

        if (mPostRequirementModelArrayList.get(i).getWaiting_for_response().equals("2")) {

            myViewHolderPostRequirement.tvWaiting.setVisibility(View.GONE);

        } else {

            myViewHolderPostRequirement.tvWaiting.setVisibility(View.VISIBLE);


        }

        myViewHolderPostRequirement.tvQty.setText("Required Quantity : " + mPostRequirementModelArrayList.get(i).getQty());
        myViewHolderPostRequirement.tvPriceRange.setText("Price Range : " + mPostRequirementModelArrayList.get(i).getCurrency() + " " + mPostRequirementModelArrayList.get(i).getPrice_from() + " to " + mPostRequirementModelArrayList.get(i).getPrice_to());
        myViewHolderPostRequirement.tvLeadId.setText("Enquiry ID - " + mPostRequirementModelArrayList.get(i).getLead_id());
        //myViewHolderPostRequirement.tvDate.setText(mPostRequirementModelArrayList.get(i).getCreated_date());

        String date = mPostRequirementModelArrayList.get(i).getCreated_date();
        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date newDate = null;
        try {
            newDate = spf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        date = spf.format(newDate);
        myViewHolderPostRequirement.tvDate.setText(date);

        myViewHolderPostRequirement.linearLayoutBuyLead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EnquiryDetailsPostRequirementFragment enquiryDetailsFragment = new EnquiryDetailsPostRequirementFragment();
                Bundle bundle = new Bundle();
                bundle.putString("pk_id", pk_id);
                bundle.putString("product_name", mPostRequirementModelArrayList.get(i).getProduct_name());
                bundle.putString("qty", mPostRequirementModelArrayList.get(i).getQty());
                bundle.putString("price_from", mPostRequirementModelArrayList.get(i).getPrice_from());
                bundle.putString("price_to", mPostRequirementModelArrayList.get(i).getPrice_to());
                bundle.putString("created_date", mPostRequirementModelArrayList.get(i).getCreated_date());
                bundle.putString("descripation", mPostRequirementModelArrayList.get(i).getDescripation());
                bundle.putString("currency", mPostRequirementModelArrayList.get(i).getCurrency());
                bundle.putString("lead_id", mPostRequirementModelArrayList.get(i).getLead_id());
                bundle.putString("preferred", mPostRequirementModelArrayList.get(i).getPreferred());
                bundle.putString("type_of_buyer", mPostRequirementModelArrayList.get(i).getType_of_buyer());
                bundle.putString("attributes_type_name", mPostRequirementModelArrayList.get(i).getAttributes_type_name());
                bundle.putString("seller_fkid", mPostRequirementModelArrayList.get(i).getSeller_fkid());
                bundle.putString("paid_status", mPostRequirementModelArrayList.get(i).getPaid_status());
                bundle.putString("userName", mPostRequirementModelArrayList.get(i).getUserName());
                bundle.putString("UA_email", mPostRequirementModelArrayList.get(i).getUA_email());
                bundle.putString("UA_mobile", mPostRequirementModelArrayList.get(i).getUA_mobile());
                bundle.putString("contactArray", mPostRequirementModelArrayList.get(i).getContactDetailsArray());

                enquiryDetailsFragment.setArguments(bundle);
                Constants.mMainActivity.ChangeFragments(enquiryDetailsFragment, "EnquiryDetailsPostRequirementFragment");
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPostRequirementModelArrayList.size();
    }


    public class MyViewHolderPostRequirement extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_product_name)
        TextView tvProductName;
        @BindView(R.id.tv_waiting)
        TextView tvWaiting;
        @BindView(R.id.tv_qty)
        TextView tvQty;
        @BindView(R.id.tv_lead_id)
        TextView tvLeadId;
        @BindView(R.id.tv_price_range)
        TextView tvPriceRange;
        @BindView(R.id.tv_date)
        TextView tvDate;
        /*
         @BindView(R.id.tv_address)
           TextView tvAddress;
           */
        @BindView(R.id.linear_layout_buy_lead)
        LinearLayout linearLayoutBuyLead;

        public MyViewHolderPostRequirement(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}
