package com.tradegenie.android.tradegenieapp.Utility;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tradegenie.android.tradegenieapp.Activity.SignUpOrSignInActivity;
import com.tradegenie.android.tradegenieapp.Activity.SplashScreenActivity;
import com.tradegenie.android.tradegenieapp.R;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class UtilityMethods {

    public static void tuchOff(RelativeLayout relativeLayout) {
        if (relativeLayout != null) {
            relativeLayout.setVisibility(View.VISIBLE);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }


    }

    public static void tuchOn(RelativeLayout relativeLayout) {
        if (relativeLayout != null) {
            relativeLayout.setVisibility(View.GONE);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }


    }

    public static void showToast(Context context, String message) {

        try {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showErrorToast(Context context, String message) {

        try {
            Toasty.error(context, message, Toast.LENGTH_LONG, true).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showSuccessToast(Context context, String message) {

        try {
            Toasty.success(context, message, Toast.LENGTH_LONG, true).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showInfoToast(Context context, String message) {

        try {
            Toasty.info(context, message, Toast.LENGTH_LONG, true).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showInfoToastDownload(Context context, String message) {

        try {
            Toasty.info(context, message, 6000, true).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showWarningToast(Context context, String message) {

        try {
            Toasty.warning(context, message, Toast.LENGTH_LONG, true).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showInternetDialog(Context context) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("No Internet Connection")
                .setContentText("Check your internet connection and try again.")
                .show();


    }
//
//    public static void showBackPressDialog(Context context) {
//        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
//                .setTitleText("Warning")
//                .setContentText("If you press back, your entered data will get lost.")
//                .show();
//
//
//    }

    public static boolean isDismiss = false;
    public static boolean showBackPressDialog(Context context) {
//        final boolean[] isDismiss = {false};
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Warning")
                .setContentText("Do you want to exit?")
                .setConfirmText("Yes")
                .setCancelText("No")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        isDismiss = true;
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        isDismiss = false;
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();
        return isDismiss;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static String setTextUpperCase(String caption) {
        if (caption.equalsIgnoreCase("")) {
            return caption;
        } else {
            String upperString = caption.substring(0, 1).toUpperCase() + caption.substring(1);
            caption = upperString;
        }
        return caption;
    }


    public static void UserInactivePopup(final Context context) {
        final SessionManager mSessionManager = new SessionManager(context);
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Something went wrong, please contact to tradeGenie support team");
        builder1.setCancelable(true);

        builder1.setPositiveButton(

                R.string.OK,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        /*if (!mSessionManager.getStringData(Constants.KEY_STUDNET_ID).equals("")) {
                            //I am student now


                            if (mSessionManager.getStringData(Constants.KEY_REMEMBER_ME).equals("true")) {
                                mSessionManager.putStringData(Constants.KEY_STUDNET_ID, "");
                                mSessionManager.putStringData(Constants.KEY_STUDNET_NAME, "");
                                mSessionManager.putStringData(Constants.KEY_STUDNET_GENDER, "");
                                mSessionManager.putStringData(Constants.KEY_STUDNET_PROFILE, "");
                                mSessionManager.putStringData(Constants.KEY_STUDNET_EMAIL, "");
                                mSessionManager.putStringData(Constants.KEY_STUDNET_DOB, "");
                                mSessionManager.putStringData(Constants.KEY_STUDNET_ADDRESS, "");
                                mSessionManager.putStringData(Constants.KEY_STUDNET_SCHOOLNAME, "");


                            } else {
                                mSessionManager.removeAll();
                            }

                        } else if (!mSessionManager.getStringData(Constants.KEY_TEACHER_ID).equals("")) {
                            //I am teacher now
                            mSessionManager.removeAll();
                        }


                        Intent intent = new Intent(context, MainActivity.class);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);*/

                        //Intent intent=new Intent(context, SignUpOrSignInActivity.class);

                        mSessionManager.removeAll();


                        Intent intent = new Intent(context, SplashScreenActivity.class);
                        context.startActivity(intent);


                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.setCanceledOnTouchOutside(false);
        alert11.show();

    }


}
