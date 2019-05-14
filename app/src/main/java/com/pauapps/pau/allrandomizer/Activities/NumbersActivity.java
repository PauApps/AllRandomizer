package com.pauapps.pau.allrandomizer.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.pauapps.pau.allrandomizer.R;

import java.util.Random;

public class NumbersActivity extends Activity {
    private static final String TAG = "NumbersActivity";
    EditText number1;
    EditText number2;
    int num;
    private InterstitialAd mInterstitialAd;
    Random r = new Random();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.numbers);

        number1 = findViewById(R.id.number1);
        number2 = findViewById(R.id.number2);

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

    public void sum1(View v) {
        num = Integer.parseInt(number1.getText().toString());
        num++;
        number1.setText("" + num);
    }

    public void res1(View v) {
        num = Integer.parseInt(number1.getText().toString());
        num--;
        number1.setText("" + num);
    }

    public void sum2(View v) {
        num = Integer.parseInt(number2.getText().toString());
        num++;
        number2.setText("" + num);
    }

    public void res2(View v) {
        num = Integer.parseInt(number2.getText().toString());
        num--;
        number2.setText("" + num);
    }

    public void rand(View v) {
        number1 = findViewById(R.id.number1);
        number2 = findViewById(R.id.number2);
        int num1 = Integer.parseInt(String.valueOf(number1.getText()));
        int num2 = Integer.parseInt(String.valueOf(number2.getText()));
        int result = r.nextInt((num2 - num1) + 1) + num1;

        showRandom(String.valueOf(result));
    }

    public void showRandom(final String result) {
        if (r.nextInt(5) == 3) {

            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Log.d(TAG, "The interstitial wasn't loaded yet.");
            }
        }
        System.out.println(result);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Your random result it's...");
        builder1.setMessage(result);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Okey!",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        number1.setText(R.string._0);
                        number2.setText(R.string._0);
                        dialog.cancel();
                    }
                });
        builder1.setNeutralButton("send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,
                        "Hey!\nI make a random process\nfrom number: " + number1.getText()
                                + "\nto number: " + number2.getText() +
                                "\n\nAnd random result is ...\n" + result + "\n\n" +
                                "Make you own random result on " +
                                "http://bit.ly/AllRandomizer\n\n" +
                                "Follow us on Twitter: https://twitter.com/pauapps");
                startActivity(Intent.createChooser(intent, "Share with"));
            }
        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
