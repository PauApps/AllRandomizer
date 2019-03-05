package com.pauapps.pau.allrandomizer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Pau on 14/10/2018.
 */

public class MyLists extends AppCompatActivity implements RewardedVideoAdListener {
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;

    DB db = new DB(this);
    Lists l = new Lists();
    List<Lists> list = new ArrayList();

    String title;

    private SharedPreferences sharedPref;
    private RewardedVideoAd mRewardedVideoAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_lists);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        list.addAll(db.getLists());

        TextView count = (TextView) findViewById(R.id.count);

        System.out.println(db.getMax());

        count.setText("Lists remain: " + db.actual_lists() + "/" + db.getMax());

        recycler = (RecyclerView) findViewById(R.id.reciclador);

        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        adapter = new getLists(list, this);
        recycler.setAdapter(adapter);

        MobileAds.initialize(this, "ca-app-pub-8428748101355923~9365578732");
        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();

        System.out.println("Max lists" + db.getMax());
    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-8428748101355923/1453722673",
                new AdRequest.Builder().build());
    }

    public void randomize(View v) {
        Intent intent = new Intent(this, PrincipalPage.class);
        intent.putExtra("title", title);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startService(intent);

    }

    public void moreSlots(View v) {
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        Button b = (Button) findViewById(R.id.ad);
        int color = ContextCompat.getColor(this, R.color.green);
        b.setTextColor(color);
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        loadRewardedVideoAd();
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        System.out.println("Max = " + db.getMax() + " New Max = " + db.getMax() + 1);
        db.updateMax(db.getMax() + 1);
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }

    @Override
    public void onResume() {
        mRewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }
}

class getLists extends RecyclerView.Adapter<getLists.ViewLists> {
    private List<Lists> count;
    Context context;

    public static class ViewLists extends RecyclerView.ViewHolder {

        public TextView tit;
        public ImageView ran;
        public ImageView del;
        public ImageView sen;

        private List<Lists> count;
        Context context;

        DB db = new DB(context);

        public ViewLists(View v, final Context context, final List<Lists> count) {
            super(v);
            this.count = count;
            this.context = context;
            tit = (TextView) v.findViewById(R.id.tit);
            ran = (ImageView) v.findViewById(R.id.ran);
            del = (ImageView) v.findViewById(R.id.del);
            sen = (ImageView) v.findViewById(R.id.sen);

            ran.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Lists getDataAdapters = count.get(position);
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("title", getDataAdapters.getTitle());
                    context.startActivity(intent);
                }
            });
            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Lists getDataAdapters = count.get(position);
                    final String title = getDataAdapters.getTitle().toString();

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setTitle("Attention!");
                    builder1.setMessage("List " + title + " will be lost!");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Delete",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    db.delete(title, context);
                                    Intent intent = new Intent(context, PrincipalPage.class);
                                    context.startActivity(intent);
                                    dialog.cancel();
                                }
                            });

                    builder1.setNegativeButton(
                            "Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert = builder1.create();
                    alert.show();
                }
            });
            sen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Lists getDataAdapters = count.get(position);
                    final String title = getDataAdapters.getTitle().toString();

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT,
                            "Hey! My list "+title+"\nAnd items that contains are : \n"+
                                    db.getItems(title,context)+"\n \nMake you own lists on " +
                                    "http://bit.ly/AllRandomizer");
                    context.startActivity(Intent.createChooser(intent, "Share with"));
                }
            });
        }
    }

    public getLists(List<Lists> count, Context context) {
        super();
        this.count = count;
        this.context = context;
    }

    @Override
    public ViewLists onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_view, viewGroup, false);
        ViewLists viewHolder = new ViewLists(v, context, count);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewLists viewHolder, int i) {
        viewHolder.tit.setText(count.get(i).getTitle());
        viewHolder.tit.setTag(i);
        /*
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyLists ml = new MyLists();
                ml.position = (int) viewHolder.tit.getTag();
            }
        });
        */
    }

    @Override
    public int getItemCount() {
        return count.size();
    }
}
