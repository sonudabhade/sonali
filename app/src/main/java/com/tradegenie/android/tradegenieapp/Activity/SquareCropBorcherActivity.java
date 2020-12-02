package com.tradegenie.android.tradegenieapp.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.Button;

import com.theartofdev.edmodo.cropper.CropImageView;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SquareCropBorcherActivity extends AppCompatActivity {


    public static boolean isCrop = false;
    @BindView(R.id.img_crop)
    CropImageView imgCrop;
    @BindView(R.id.btn_done)
    Button btnDone;
    @BindView(R.id.btn_cancel)
    Button btnCancel;

    String WhichImage = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square_crop);
        ButterKnife.bind(this);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        width = width - 80;
        imgCrop.getLayoutParams().height = width;
        imgCrop.getLayoutParams().width = width;

        imgCrop.setBackground(null);
        imgCrop.setScaleType(CropImageView.ScaleType.FIT_CENTER);

        Bitmap bitmap=Constants.mMainActivity.thumbnail_rb;

        imgCrop.setImageBitmap(Constants.mMainActivity.thumbnail_rb);


    }

    @OnClick(R.id.btn_done)
    public void onViewClicked() {

        isCrop = true;
        Intent returnIntent = new Intent();


        if (WhichImage.equals("")) {
            Constants.mMainActivity.thumbnail_rb = imgCrop.getCroppedImage();
            setResult(3, returnIntent);
        }


        finish();
    }


    @OnClick(R.id.btn_cancel)
    public void onViewClickedCancel() {

        Constants.mMainActivity.thumbnail_rb=null;
        Constants.mMainActivity.thumbnail_r=null;

        finish();
    }


    @Override
    public void onBackPressed() {
        //  super.onBackPressed();

        Constants.mMainActivity.thumbnail_rb=null;
        Constants.mMainActivity.thumbnail_r=null;


        finish();

    }

}
