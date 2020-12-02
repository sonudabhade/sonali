package com.tradegenie.android.tradegenieapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tradegenie.android.tradegenieapp.Models.SubCategoryModel;
import com.tradegenie.android.tradegenieapp.R;

import java.util.ArrayList;


public class AdapterSpinnerSubCategoryList extends ArrayAdapter<SubCategoryModel> {


    private Context mContext;
    private ArrayList<SubCategoryModel> mSubCategoryModelArrayList;
    private LayoutInflater mLayoutInflater;
    private int mResource;
    private int mTextViewResourceId;






    public AdapterSpinnerSubCategoryList(@NonNull Context context, int resource, int textViewResourceId, @NonNull ArrayList<SubCategoryModel> objects) {
        super(context, resource, textViewResourceId, objects);
        mContext=context;
        mSubCategoryModelArrayList=objects;
        mTextViewResourceId=textViewResourceId;
        mResource=resource;
        mLayoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        super.getCount();
        return mSubCategoryModelArrayList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = mLayoutInflater.inflate(R.layout.adapter_gender_spinner_row_select,null,true);
        TextView mTextView= (TextView) convertView.findViewById(R.id.classNameTextView);
        mTextView.setText(mSubCategoryModelArrayList.get(position).getTitle());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView=mLayoutInflater.inflate(R.layout.adapter_gender_spinner_row_select,null,true);
        TextView mTextView= (TextView) convertView.findViewById(R.id.classNameTextView);
        mTextView.setText(mSubCategoryModelArrayList.get(position).getTitle());
        return convertView;
    }

}