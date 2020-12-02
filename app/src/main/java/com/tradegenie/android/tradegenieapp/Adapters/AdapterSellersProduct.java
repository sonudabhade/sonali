package com.tradegenie.android.tradegenieapp.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.tradegenie.android.tradegenieapp.Fragments.EnquiryFragment;
import com.tradegenie.android.tradegenieapp.Fragments.ProductViewFragment;
import com.tradegenie.android.tradegenieapp.Models.ProductListBuyModel;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;
import com.tradegenie.android.tradegenieapp.Utility.SessionManager;
import com.tradegenie.android.tradegenieapp.Utility.UtilityMethods;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterSellersProduct extends RecyclerView.Adapter<AdapterSellersProduct.ViewHolder> {

    private Context mContext;
    private List<ProductListBuyModel> mProductListPendingModelArrayList;
    private SessionManager mSessionManager;

    public AdapterSellersProduct(Context mContext, List<ProductListBuyModel> mProductListPendingModelArrayList) {
        this.mContext = mContext;
        this.mProductListPendingModelArrayList = mProductListPendingModelArrayList;
        mSessionManager = new SessionManager(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_product_list_buy, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {


        final ProductListBuyModel productListBuyModel = mProductListPendingModelArrayList.get(i);


        try {
            Picasso.get().load(productListBuyModel.getImage_name()).placeholder(R.drawable.default_document).into(viewHolder.ivProductImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewHolder.tvProductName.setText(productListBuyModel.getProduct_name());
        viewHolder.tvCompanyName.setText(productListBuyModel.getComp_name());

        final String favorite = productListBuyModel.getFavorite();
        String location = productListBuyModel.getBussaddress();
        String price = productListBuyModel.getCost();

//        if (!price.equals("")) {
//
//            viewHolder.tvCost.setText(mProductListPendingModelArrayList.get(i).getCurrency() + " " + productListBuyModel.getCost());
//
//        } else {
//
//            viewHolder.tvCost.setVisibility(View.GONE);
//
//
//        }

        if (!price.equals("")) {

            if (price.equals("0")){
                viewHolder.tvCost.setText(mProductListPendingModelArrayList.get(i).getCurrency() + " - Enquiry for cost");
            }else {
                viewHolder.tvCost.setText(mProductListPendingModelArrayList.get(i).getCurrency() + " " + productListBuyModel.getCost());
            }

        } else {

            viewHolder.tvCost.setVisibility(View.GONE);


        }

        if (!location.equals("")) {

//            viewHolder.tvLocation.setText(productListBuyModel.getBussaddress());
            try {
                viewHolder.tvLocation.setText(productListBuyModel.getCity());
                viewHolder.tvCounntry.setText(productListBuyModel.getCountry());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            viewHolder.linearLayoutLocation.setVisibility(View.GONE);

        }

        if (favorite.equals("1")) {

            try {
                viewHolder.ivFavourite.setImageResource(R.drawable.ic_favourite_active);
//                Picasso.get().load(R.drawable.ic_favourite_active).placeholder(R.drawable.ic_favourite_active).into(viewHolder.ivFavourite);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            try {
                viewHolder.ivFavourite.setImageResource(R.drawable.ic_favourite_inactive);
//                Picasso.get().load(R.drawable.ic_favourite_inactive).placeholder(R.drawable.ic_favourite_inactive).into(viewHolder.ivFavourite);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        viewHolder.ivFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (favorite.equals("1")) {

                    try {
                        Picasso.get().load(R.drawable.ic_favourite_inactive).placeholder(R.drawable.ic_favourite_inactive).into(viewHolder.ivFavourite);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    mProductListPendingModelArrayList.get(i).setFavorite("2");

                    CallSetFavourite(mProductListPendingModelArrayList.get(i).getPk_id());

                    notifyDataSetChanged();

                } else {

                    try {
                        Picasso.get().load(R.drawable.ic_favourite_active).placeholder(R.drawable.ic_favourite_active).into(viewHolder.ivFavourite);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    mProductListPendingModelArrayList.get(i).setFavorite("1");

                    CallSetFavourite(mProductListPendingModelArrayList.get(i).getPk_id());


                    notifyDataSetChanged();

                }


            }
        });

        viewHolder.LinearLayoutEnquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EnquiryFragment enquiryFragment = new EnquiryFragment();
                Bundle bundle = new Bundle();
                bundle.putString("PK_id", productListBuyModel.getPk_id());
                enquiryFragment.setArguments(bundle);
                Constants.mMainActivity.ChangeFragments(enquiryFragment, "EnquiryFragment");


            }
        });

        viewHolder.LinearLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ProductViewFragment productViewFragment = new ProductViewFragment();
                Bundle bundle = new Bundle();
                bundle.putString("PK_id", productListBuyModel.getPk_id());
                productViewFragment.setArguments(bundle);
                Constants.mMainActivity.ChangeFragmentsAdd(productViewFragment, "ProductViewFragment");

            }
        });

        viewHolder.linearLayoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ProductViewFragment productViewFragment = new ProductViewFragment();
                Bundle bundle = new Bundle();
                bundle.putString("PK_id", productListBuyModel.getPk_id());
                productViewFragment.setArguments(bundle);
                Constants.mMainActivity.ChangeFragmentsAdd(productViewFragment, "ProductViewFragment");

            }
        });

    }

    @Override
    public int getItemCount() {
        return mProductListPendingModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_product_image)
        ImageView ivProductImage;
        @BindView(R.id.tv_product_name)
        TextView tvProductName;
        @BindView(R.id.iv_favourite)
        ImageView ivFavourite;
        @BindView(R.id.tv_company_name)
        TextView tvCompanyName;
        @BindView(R.id.tv_location)
        TextView tvLocation;
        @BindView(R.id.tv_cost)
        TextView tvCost;
        @BindView(R.id.linear_layout_main)
        LinearLayout linearLayoutMain;
        @BindView(R.id.Linear_layout_enquiry)
        LinearLayout LinearLayoutEnquiry;
        @BindView(R.id.Linear_layout_view)
        LinearLayout LinearLayoutView;
        @BindView(R.id.linear_layout_location)
        LinearLayout linearLayoutLocation;
        @BindView(R.id.tv_counntry)
        TextView tvCounntry;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //Method to Call Set Favourite
    private void CallSetFavourite(final String productid) {

        //UtilityMethods.tuchOff(relativeLayoutProductView);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_FEVORITE_SUBMIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", "CallSetFavourite" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error_code").equals("1")) {


                    } else if (jsonObject.getString("error_code").equals("0")) {
                    } else if (jsonObject.getString("error_code").equals("2")) {

                    } else if (jsonObject.getString("error_code").equals("10")) {

                        UtilityMethods.UserInactivePopup(mContext);
                    } else {
                        UtilityMethods.showInfoToast(mContext, jsonObject.getString("message"));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                // UtilityMethods.tuchOn(relativeLayoutProductView);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        UtilityMethods.showErrorToast(mContext, mContext.getString(R.string.check_internet_problem));

                    } else if (error instanceof AuthFailureError) {

                        UtilityMethods.showErrorToast(mContext, mContext.getString(R.string.auth_fail));
                    } else if (error instanceof ServerError) {

                        UtilityMethods.showErrorToast(mContext, mContext.getString(R.string.server_error));
                    } else if (error instanceof NetworkError) {

                        UtilityMethods.showErrorToast(mContext, mContext.getString(R.string.network_error));
                    } else if (error instanceof ParseError) {

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
                // UtilityMethods.tuchOn(relativeLayoutProductView);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                if (mSessionManager.getStringData(Constants.KEY_USER_TYPE).equals("seller")) {

                    params.put("user_id", mSessionManager.getStringData(Constants.KEY_SELLER_UID));


                } else {

                    params.put("user_id", mSessionManager.getStringData(Constants.KEY_BUYER_UID));


                }

                params.put("productid", productid);
                params.put("utype", mSessionManager.getStringData(Constants.KEY_USER_TYPE));

                Log.e("check", "=======>" + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

// stringRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(mContext).add(stringRequest).setRetryPolicy(new DefaultRetryPolicy(Constants.KEY_TIME_OUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}
