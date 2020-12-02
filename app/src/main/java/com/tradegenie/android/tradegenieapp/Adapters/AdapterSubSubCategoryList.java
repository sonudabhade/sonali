package com.tradegenie.android.tradegenieapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.tradegenie.android.tradegenieapp.Models.SubSubCategoryModel;
import com.tradegenie.android.tradegenieapp.R;

import java.util.ArrayList;


public class AdapterSubSubCategoryList extends ArrayAdapter<SubSubCategoryModel> {


    private Context mContext;
    private ArrayList<SubSubCategoryModel> mSubSubCategoryModelArrayList;
    private LayoutInflater mLayoutInflater;
    private int mResource;
    private int mTextViewResourceId;






    public AdapterSubSubCategoryList(@NonNull Context context, int resource, int textViewResourceId, @NonNull ArrayList<SubSubCategoryModel> objects) {
        super(context, resource, textViewResourceId, objects);
        mContext=context;
        mSubSubCategoryModelArrayList=objects;
        mTextViewResourceId=textViewResourceId;
        mResource=resource;
        mLayoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        super.getCount();
        return mSubSubCategoryModelArrayList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = mLayoutInflater.inflate(R.layout.adapter_gender_spinner_row_select,null,true);
        TextView mTextView= (TextView) convertView.findViewById(R.id.classNameTextView);
        mTextView.setText(mSubSubCategoryModelArrayList.get(position).getTitle());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView=mLayoutInflater.inflate(R.layout.adapter_gender_spinner_row_select,null,true);
        TextView mTextView= (TextView) convertView.findViewById(R.id.classNameTextView);
        mTextView.setText(mSubSubCategoryModelArrayList.get(position).getTitle());
        return convertView;
    }

}