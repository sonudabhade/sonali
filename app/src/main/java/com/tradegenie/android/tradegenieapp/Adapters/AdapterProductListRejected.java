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
import com.tradegenie.android.tradegenieapp.Fragments.AddNewProductEdit;
import com.tradegenie.android.tradegenieapp.Fragments.ProductListRejectedFragment;
import com.tradegenie.android.tradegenieapp.Fragments.ProductViewFragment;
import com.tradegenie.android.tradegenieapp.Models.ProductListRejectedModel;
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
public class AdapterProductListRejected extends RecyclerView.Adapter<AdapterProductListRejected.MyViewHolderProductListPending> {


    @BindView(R.id.iv_product_image)
    ImageView ivProductImage;
    @BindView(R.id.tv_product_name)
    TextView tvProductName;
    @BindView(R.id.tv_cost)
    TextView tvCost;
    @BindView(R.id.tv_cat_name)
    TextView tvCatName;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.Linear_layout_delete)
    LinearLayout LinearLayoutDelete;
    @BindView(R.id.Linear_layout_edit)
    LinearLayout LinearLayoutEdit;
    private Context mContext;
    private ArrayList<ProductListRejectedModel> mProductListRejectedModelArrayList;
    private ProductListRejectedFragment mProductListRejectedFragment;

    public AdapterProductListRejected(Context mContext, ArrayList<ProductListRejectedModel> mProductListRejectedModelArrayList, ProductListRejectedFragment productListRejectedFragment) {
        this.mContext = mContext;
        this.mProductListRejectedModelArrayList = mProductListRejectedModelArrayList;
        mProductListRejectedFragment = productListRejectedFragment;
    }

    @NonNull
    @Override
    public MyViewHolderProductListPending onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_product_list_rejected, null);
        return new MyViewHolderProductListPending(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderProductListPending myViewHolderProductListPending, int i) {

        final ProductListRejectedModel productListRejectedModel = mProductListRejectedModelArrayList.get(i);

        myViewHolderProductListPending.tvProductName.setText(productListRejectedModel.getProduct_name());
        myViewHolderProductListPending.tvCatName.setText(productListRejectedModel.getCat_name() + "/" + productListRejectedModel.getSubcat_name());
        myViewHolderProductListPending.tvRemark.setText(productListRejectedModel.getCancel_remark());


        try {
            Picasso.get().load(productListRejectedModel.getImage_name()).placeholder(R.drawable.default_document).into(myViewHolderProductListPending.ivProductImage);
        } catch (Exception e) {

        }

        if (productListRejectedModel.getCost().equalsIgnoreCase("")) {


            myViewHolderProductListPending.tvCost.setVisibility(View.GONE);


        } else {

            myViewHolderProductListPending.tvCost.setVisibility(View.VISIBLE);

            myViewHolderProductListPending.tvCost.setText(productListRejectedModel.getCurrency() + " " + productListRejectedModel.getCost());


        }

        myViewHolderProductListPending.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProductViewFragment productViewFragment = new ProductViewFragment();
                Bundle bundle = new Bundle();
                bundle.putString("PK_id", productListRejectedModel.getPk_id());
                productViewFragment.setArguments(bundle);
                Constants.mMainActivity.ChangeFragmentsAdd(productViewFragment, "ProductViewFragment");

            }
        });


        myViewHolderProductListPending.LinearLayoutEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AddNewProductEdit addNewProductEdit = new AddNewProductEdit();
                Bundle bundle = new Bundle();
                bundle.putString("PK_id", productListRejectedModel.getPk_id());
                bundle.putString("from_Pending", "from_Pending");
                addNewProductEdit.setArguments(bundle);
                Constants.mMainActivity.ChangeFragments(addNewProductEdit, "AddNewProductEdit");


            }
        });


        myViewHolderProductListPending.LinearLayoutDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Do you really want to delete the product ?");
                builder.setCancelable(false);

                builder.setPositiveButton(

                        R.string.YES,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (InternetConnection.isInternetAvailable(mContext)) {

                                    mProductListRejectedFragment.DeleteProduct(productListRejectedModel.getPk_id());


                                } else {

                                    UtilityMethods.showInternetDialog(mContext);

                                }


                            }
                        });

                builder.setNegativeButton(

                        R.string.NO,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                            }
                        });


                AlertDialog alert11 = builder.create();
                alert11.setCanceledOnTouchOutside(false);
                alert11.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mProductListRejectedModelArrayList.size();
    }


    public class MyViewHolderProductListPending extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_product_image)
        ImageView ivProductImage;
        @BindView(R.id.tv_product_name)
        TextView tvProductName;
        @BindView(R.id.tv_cost)
        TextView tvCost;
        @BindView(R.id.tv_cat_name)
        TextView tvCatName;
        @BindView(R.id.tv_remark)
        TextView tvRemark;
        @BindView(R.id.Linear_layout_delete)
        LinearLayout LinearLayoutDelete;
        @BindView(R.id.Linear_layout_edit)
        LinearLayout LinearLayoutEdit;

        public MyViewHolderProductListPending(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
