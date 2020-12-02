package com.tradegenie.android.tradegenieapp.Utility;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "myloginapp";


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("refreshedToken", "refreshedToken " + refreshedToken);
        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);

    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);
    }

    private void storeRegIdInPref(String token) {
        SessionManager mSessionManager = new SessionManager(getApplicationContext());


        if (!token.equals(""))
            mSessionManager.putStringData(Constants.KEY_FCM_ID, token);

        Log.e("check","KEY_FCM_ID"+Constants.KEY_FCM_ID);


    }
}