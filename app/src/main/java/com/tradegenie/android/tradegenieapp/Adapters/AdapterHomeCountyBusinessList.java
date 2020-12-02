package com.tradegenie.android.tradegenieapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tradegenie.android.tradegenieapp.Models.CountryListBusinessModel;
import com.tradegenie.android.tradegenieapp.R;

import java.util.ArrayList;


public class AdapterHomeCountyBusinessList extends ArrayAdapter<CountryListBusinessModel> {


    private Context mContext;
    private ArrayList<CountryListBusinessModel> mCountryListModelArrayList;
    private LayoutInflater mLayoutInflater;
    private int mResource;
    private int mTextViewResourceId;







    public AdapterHomeCountyBusinessList(@NonNull Context context, int resource, int textViewResourceId, @NonNull ArrayList<CountryListBusinessModel> objects) {
        super(context, resource, textViewResourceId, objects);
        mContext=context;
        mCountryListModelArrayList=objects;
        mTextViewResourceId=textViewResourceId;
        mResource=resource;
        mLayoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {
        super.getCount();
        return mCountryListModelArrayList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = mLayoutInflater.inflate(R.layout.adapter_country_flag_row,null,true);
        //TextView mTextView= (TextView) convertView.findViewById(R.id.txt_country_name);
        ImageView mImageView=(ImageView) convertView.findViewById(R.id.iv_country_flag);
        //mTextView.setVisibility(View.GONE);
        try {
            Picasso.get().load(mCountryListModelArrayList.get(position).getCountry_flag()).placeholder(R.drawable.ic_launcher_foreground).into(mImageView);
        } catch (Exception e) {
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView=mLayoutInflater.inflate(R.layout.adapter_country_list_row,null,true);
        TextView mTextView= (TextView) convertView.findViewById(R.id.txt_country_name);
        mTextView.setText(mCountryListModelArrayList.get(position).getCountry_name());
        ImageView mImageView=(ImageView) convertView.findViewById(R.id.iv_country_flag);
        mTextView.setText(mCountryListModelArrayList.get(position).getCountry_name());
        try {
            Picasso.get().load(mCountryListModelArrayList.get(position).getCountry_flag()).placeholder(R.drawable.ic_launcher_foreground).into(mImageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

}