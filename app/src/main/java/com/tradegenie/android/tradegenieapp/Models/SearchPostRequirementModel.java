package com.tradegenie.android.tradegenieapp.Models;

import java.util.ArrayList;

public class SearchPostRequirementModel {

    String pk_id,cat_name;

    ArrayList<SearchSubCatArrayModel> mSearchSubCatArrayModelArrayList=new ArrayList<>();


    public ArrayList<SearchSubCatArrayModel> getmSearchSubCatArrayModelArrayList() {
        return mSearchSubCatArrayModelArrayList;
    }

    public void setmSearchSubCatArrayModelArrayList(ArrayList<SearchSubCatArrayModel> mSearchSubCatArrayModelArrayList) {
        this.mSearchSubCatArrayModelArrayList = mSearchSubCatArrayModelArrayList;
    }

    public String getPk_id() {
        return pk_id;
    }

    public void setPk_id(String pk_id) {
        this.pk_id = pk_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }
}
