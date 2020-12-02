package com.tradegenie.android.tradegenieapp.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tradegenie.android.tradegenieapp.Models.HomePostEnquiryModel;
import com.tradegenie.android.tradegenieapp.R;

import java.util.List;

public class HomePostRequirementAdapter extends RecyclerView.Adapter<HomePostRequirementAdapter.ViewHolder> {
    private List<HomePostEnquiryModel> postEnquiryModelList;


    public HomePostRequirementAdapter(List<HomePostEnquiryModel> postEnquiryModelList) {

        this.postEnquiryModelList = postEnquiryModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_home_post_req_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        try{
            int position = i;
            position = position % postEnquiryModelList.size();
//            viewHolder.tv_post_req.setText(postEnquiryModelList.get(position).getQty()+" quantity required for "+postEnquiryModelList.get(position).getEnquiry_type());
            viewHolder.tv_post_req.setText("New product: "+postEnquiryModelList.get(position).getEnquiry_type());
            try {
                Picasso.get().load(postEnquiryModelList.get(position).getCat_image_path()).placeholder(R.drawable.ic_notification_logo).into(viewHolder.ic_cat);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public int getItemCount() {
//        return postEnquiryModelList.size();
        //return 1;
        Log.e("error", "Error");
        return Integer.MAX_VALUE;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_post_req;
        ImageView ic_cat;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_post_req = itemView.findViewById(R.id.tv_post_req);
            ic_cat = itemView.findViewById(R.id.ic_cat);
        }
    }
}
