package com.pauapps.pau.allrandomizer.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.pauapps.pau.allrandomizer.AnalyticsApplication;
import com.pauapps.pau.allrandomizer.Utils.DB;
import com.pauapps.pau.allrandomizer.Utils.Lists;
import com.pauapps.pau.allrandomizer.R;

import java.util.ArrayList;
import java.util.Random;

public class ListsActivity extends Activity {
    private Tracker mTracker;
    private InterstitialAd mInterstitialAd;

    ListView listview;
    String text;
    TextView txt;
    ArrayList<String> list = new ArrayList<String>();
    DB db = new DB(this);

    Lists l = new Lists();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        listview = (ListView) findViewById(R.id.listview);
        txt = (TextView) findViewById(R.id.insertText);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            addAll(b.getString("title"), this);
        }


        MobileAds.initialize(this, "ca-app-pub-8428748101355923~9365578732");

        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-8428748101355923/4601969775");//ca-app-pub-8428748101355923/4601969775
        //ca-app-pub-3940256099942544/1033173712
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

    }

    public void addItem(View v) {
        String text = txt.getText().toString();
        list.add(text);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);
        txt.setText("");
    }

    public void randomize(View v) {

        Random r = new Random();

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Share")
                .build());

        if (list.size() < 1) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle("FAIL!");
            builder1.setMessage("Must enter some item");
            builder1.setCancelable(true);

            AlertDialog alert12 = builder1.create();
            alert12.show();
        } else {
            if (r.nextInt(4) == 3) {

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }

            int ran = r.nextInt(list.size());
            final String res = list.get(ran).toString();

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle("Your random result it's...");
            builder1.setMessage(res);
            builder1.setCancelable(true);

            //builder1.setPositiveButtonIcon(this.getResources().getDrawable(R.drawable.clean_button));
            builder1.setPositiveButton(
                    "clean list",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            list.clear();
                            listview.setAdapter(null);
                            dialog.cancel();
                        }
                    });
            //builder1.setNegativeButtonIcon(this.getResources().getDrawable(R.drawable.cancel_button));
            builder1.setNegativeButton(
                    "cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            //builder1.setNeutralButtonIcon(this.getResources().getDrawable(R.drawable.send_button));
            builder1.setNeutralButton("send", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String items = "";
                    for (int z = 0; z < list.size(); z++) {
                        items = items + "\n" + list.get(z);
                    }
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT,
                            "Hey!\nI make a random process of this items: \n" + items +
                                    "\n \nAnd random result is ... \n" + res + "\n\nMake you own lists on " +
                                    "http://bit.ly/AllRandomizer \n\n" +
                                    "Follow us on Twitter: https://twitter.com/pauapps");
                    startActivity(Intent.createChooser(intent, "Share with"));
                }
            });
            //builder1.setIcon(R.drawable.random_button); //Icon

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }

    }

    public void saveList(View v) {
        if (list.size() < 1) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle("FAIL!");
            builder1.setMessage("Must enter some item");
            builder1.setCancelable(true);

            AlertDialog alert12 = builder1.create();
            alert12.show();
        } else {
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            text = input.getText().toString();
            final AlertDialog.Builder title = new AlertDialog.Builder(this);
            title.setTitle("Set a title for this list");
            title.setView(input);
            title.setCancelable(true);
            //title.setIcon(R.drawable.save_button);
            title.setPositiveButton(
                    "Add list",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            text = input.getText().toString();
                            if (text.length() == 0) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(title.getContext());
                                title.setTitle("FAIL!");
                                builder1.setMessage("Must enter a title");
                                builder1.setCancelable(true);

                                AlertDialog alert12 = builder1.create();
                                alert12.show();
                            } else {
                                System.out.println(db.actual_lists());
                                System.out.println(l.max);
                                db.actual_lists();
                                if (db.actual_lists() < db.getMax()) {
                                    if (mInterstitialAd.isLoaded()) {
                                        mInterstitialAd.show();
                                    } else {
                                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                                    }
                                    for (int i = 0; i < list.size(); i++) {
                                        String item = list.get(i).toString();
                                        db.insert(new Lists(text, item), text);
                                    }
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(title.getContext());
                                    title.setTitle("SUCCESS");
                                    builder1.setMessage("Your list " + text + " has been saved!");
                                    builder1.setCancelable(true);

                                    AlertDialog alert12 = builder1.create();
                                    alert12.show();
                                } else {
                                    if (mInterstitialAd.isLoaded()) {
                                        mInterstitialAd.show();
                                    } else {
                                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                                    }
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(title.getContext());
                                    title.setTitle("FAIL");
                                    builder1.setMessage("Your list " + text + " has not been saved! \n" +
                                            "You don't have slots to save more lists");
                                    builder1.setCancelable(true);

                                    AlertDialog alert12 = builder1.create();
                                    alert12.show();
                                }
                            }
                        }
                    });

            title.setNegativeButton(
                    "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = title.create();
            alert11.show();
        }
    }

    public void addAll(String title, Context con) {
        list.addAll(db.getItems(title, con));

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(con,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

    }

}
