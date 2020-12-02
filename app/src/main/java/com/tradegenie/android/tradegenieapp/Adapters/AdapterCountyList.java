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
import com.tradegenie.android.tradegenieapp.Models.CountryListModel;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Activity.SignUpOrSignInActivity;

import java.util.ArrayList;


public class AdapterCountyList extends ArrayAdapter<CountryListModel> {


    private Context mContext;
    private ArrayList<CountryListModel> mCountryListModelArrayList;
    private LayoutInflater mLayoutInflater;
    private int mResource;
    private int mTextViewResourceId;
    SignUpOrSignInActivity mSignUpOrSignInActivity;






    public AdapterCountyList(@NonNull Context context, int resource, int textViewResourceId, @NonNull ArrayList<CountryListModel> objects, SignUpOrSignInActivity signUpOrSignInActivity) {
        super(context, resource, textViewResourceId, objects);
        mContext=context;
        mCountryListModelArrayList=objects;
        mTextViewResourceId=textViewResourceId;
        mResource=resource;
        mLayoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSignUpOrSignInActivity=signUpOrSignInActivity;
    }


    @Override
    public int getCount() {
        super.getCount();
        return mCountryListModelArrayList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = mLayoutInflater.inflate(R.layout.adapter_country_list_row,null,true);
        TextView mTextView= (TextView) convertView.findViewById(R.id.txt_country_name);
        ImageView mImageView=(ImageView) convertView.findViewById(R.id.iv_country_flag);
        mTextView.setText(mCountryListModelArrayList.get(position).getCountry_name());
        try {
            Picasso.get().load(mCountryListModelArrayList.get(position).getCountry_flag()).placeholder(R.drawable.ic_launcher_foreground).into(mImageView);
        } catch (Exception e) {
            e.printStackTrace();
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