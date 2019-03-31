package com.pauapps.pau.allrandomizer.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.pauapps.pau.allrandomizer.AnalyticsApplication;
import com.pauapps.pau.allrandomizer.R;
import com.pauapps.pau.allrandomizer.Utils.MyLists;

/**
 * Created by Pau on 25/9/2018.
 * 1.4 branch
 */

public class PrincipalPage extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Tracker mTracker;
    int option = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_page);

        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

    }

    public void randomize(View v) {
        String name = "randomize";
        //TODO fer que segons lo que triis se posarà una opció o una altra
        startActivity(new Intent(this, Numbers.class));
        System.out.println(option);
        switch (option) {
            case 0:
                //Lists select
                startActivity(new Intent(this, ListsActivity.class));
                break;
            case 1:
                //NumbersActivity select
                startActivity(new Intent(this, NumbersActivity.class));
                break;
        }
        Log.i(TAG, "Setting screen name: " + name);
        mTracker.setScreenName("Image~" + name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void list(View v) {
        startActivity(new Intent(this, MyLists.class));

    }

    public void options(View v) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_windows, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(v, Gravity.RIGHT, 25, -450);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    public void selectedOption(View v) {
        switch (v.getId()) {
            case R.id.lists:
                option = 0;
                break;
            case R.id.numbers:
                System.out.println("Number");
                option = 1;
                break;
        }
    }
}
