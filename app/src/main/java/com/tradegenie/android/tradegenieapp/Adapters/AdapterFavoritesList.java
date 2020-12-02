package com.tradegenie.android.tradegenieapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tradegenie.android.tradegenieapp.Activity.MainActivity;
import com.tradegenie.android.tradegenieapp.Fragments.EnquiryFragment;
import com.tradegenie.android.tradegenieapp.Fragments.FavouritesFragment;
import com.tradegenie.android.tradegenieapp.Fragments.ProductListApprovedFragment;
import com.tradegenie.android.tradegenieapp.Fragments.ProductViewFragment;
import com.tradegenie.android.tradegenieapp.Models.FavoritesListModel;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;
import com.tradegenie.android.tradegenieapp.Utility.InternetConnection;
import com.tradegenie.android.tradegenieapp.Utility.UtilityMethods;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 *
 */
public class AdapterFavoritesList extends RecyclerView.Adapter<AdapterFavoritesList.MyViewHolderProductListPending> {



    private Context mContext;
    private ArrayList<FavoritesListModel> mFavoritesListModelArrayList;
    private FavouritesFragment mFavouritesFragment;


    public AdapterFavoritesList(Context mContext, ArrayList<FavoritesListModel> FavoritesListModelArrayList, FavouritesFragment favouritesFragment) {
        this.mContext = mContext;
        this.mFavoritesListModelArrayList = FavoritesListModelArrayList;
        mFavouritesFragment=favouritesFragment;

    }

    @NonNull
    @Override
    public MyViewHolderProductListPending onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_product_list_buy, null);
        return new MyViewHolderProductListPending(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolderProductListPending myViewHolderProductListPending, final int i) {

        final FavoritesListModel favoritesListModel = mFavoritesListModelArrayList.get(i);


        try {
            Picasso.get().load(favoritesListModel.getImage_name()).placeholder(R.drawable.default_document).into(myViewHolderProductListPending.ivProductImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        myViewHolderProductListPending.tvCountry.setText(favoritesListModel.getCountry_name());
        myViewHolderProductListPending.tvProductName.setText(favoritesListModel.getProduct_name());
        myViewHolderProductListPending.tvCompanyName.setText(favoritesListModel.getComp_name());
        myViewHolderProductListPending.tvLocation.setText(favoritesListModel.getAddress());
//        myViewHolderProductListPending.tvCost.setText(mFavoritesListModelArrayList.get(i).getCurrency()+" "+ favoritesListModel.getCost());
        String price = favoritesListModel.getCost();
        if (!price.equals("")) {
            if (price.equals("0")){
                myViewHolderProductListPending.tvCost.setText(mFavoritesListModelArrayList.get(i).getCurrency()+" - Enquiry for cost");
            }else {
                myViewHolderProductListPending.tvCost.setText(mFavoritesListModelArrayList.get(i).getCurrency()+" "+ favoritesListModel.getCost());
            }

        }

        final String favorite = favoritesListModel.getFavorite();

        if (favorite.equals("1")) {

            try {
                Picasso.get().load(R.drawable.ic_favourite_active).placeholder(R.drawable.ic_favourite_active).into(myViewHolderProductListPending.ivFavourite);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            try {
                Picasso.get().load(R.drawable.ic_favourite_inactive).placeholder(R.drawable.ic_favourite_inactive).into(myViewHolderProductListPending.ivFavourite);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        myViewHolderProductListPending.ivFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setMessage("Do you really want to remove this product from favorites?");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (favorite.equals("1")) {

                            try {
                                Picasso.get().load(R.drawable.ic_favourite_inactive).placeholder(R.drawable.ic_favourite_inactive).into(myViewHolderProductListPending.ivFavourite);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            mFavoritesListModelArrayList.get(i).setFavorite("2");

                            mFavouritesFragment.CallSetFavourite(mFavoritesListModelArrayList.get(i).getPk_id());

                            notifyDataSetChanged();

                        } else {

                            try {
                                Picasso.get().load(R.drawable.ic_favourite_active).placeholder(R.drawable.ic_favourite_active).into(myViewHolderProductListPending.ivFavourite);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            mFavoritesListModelArrayList.get(i).setFavorite("1");

                            mFavouritesFragment.CallSetFavourite(mFavoritesListModelArrayList.get(i).getPk_id());


                            notifyDataSetChanged();

                        }

                    }
                });
                builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert11 = builder1.create();
                alert11.show();





            }
        });

        myViewHolderProductListPending.LinearLayoutEnquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EnquiryFragment enquiryFragment = new EnquiryFragment();
                Bundle bundle = new Bundle();
                bundle.putString("PK_id", favoritesListModel.getPk_id());
                enquiryFragment.setArguments(bundle);
                Constants.mMainActivity.ChangeFragments(enquiryFragment, "EnquiryFragment");


            }
        });

        myViewHolderProductListPending.LinearLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ProductViewFragment productViewFragment = new ProductViewFragment();
                Bundle bundle = new Bundle();
                bundle.putString("PK_id", favoritesListModel.getPk_id());
                productViewFragment.setArguments(bundle);
                Constants.mMainActivity.ChangeFragmentsAdd(productViewFragment, "ProductViewFragment");

            }
        });

        myViewHolderProductListPending.linearLayoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ProductViewFragment productViewFragment = new ProductViewFragment();
                Bundle bundle = new Bundle();
                bundle.putString("PK_id", favoritesListModel.getPk_id());
                productViewFragment.setArguments(bundle);
                Constants.mMainActivity.ChangeFragmentsAdd(productViewFragment, "ProductViewFragment");

            }
        });
    }

    @Override
    public int getItemCount() {
        return mFavoritesListModelArrayList.size();
    }


    public class MyViewHolderProductListPending extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_product_image)
        ImageView ivProductImage;
        @BindView(R.id.tv_product_name)
        TextView tvProductName;
       @BindView(R.id.tv_counntry)
        TextView tvCountry;
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

        public MyViewHolderProductListPending(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
