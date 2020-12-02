package com.tradegenie.android.tradegenieapp.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.tradegenie.android.tradegenieapp.R;


public class UpdateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update);

        String latestVersion = getIntent().getStringExtra("Version");

        String title = getApplicationContext().getString(R.string.youAreNotUpdatedTitle);
        String message = getApplicationContext().getString(R.string.youAreNotUpdatedMessage) + " "
                + latestVersion + getApplicationContext().getString(R.string.youAreNotUpdatedMessage1);

        TextView text_dialog = (TextView) findViewById(R.id.text_dialog);
        TextView text_message = (TextView) findViewById(R.id.text_message);

        text_dialog.setText(title);
        text_message.setText(message);

    }

    public void updateClicked(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" +  "com.tradegenie.android.tradegenieapp")));
    }

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        System.exit(0);
    }
}
