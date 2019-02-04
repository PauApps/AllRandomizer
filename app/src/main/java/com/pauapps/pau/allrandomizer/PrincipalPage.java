package com.pauapps.pau.allrandomizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by Pau on 25/9/2018.
 * test
 */

public class PrincipalPage extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_page);

        DB db = new DB(this);

        System.out.println(db.actual_lists());


        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

    }

    public void randomize(View v) {
        String name = "randomize";
        startActivity(new Intent(this, MainActivity.class));
        Log.i(TAG, "Setting screen name: " + name);
        mTracker.setScreenName("Image~" + name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void list(View v) {
        startActivity(new Intent(this, MyLists.class));

    }

    public void about(View v) {

    }
}
