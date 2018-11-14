package com.pauapps.pau.allrandomizer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pau on 14/10/2018.
 */

public class MyLists extends AppCompatActivity {
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;

    DB db = new DB(this);

    List<Lists> list = new ArrayList();

    String title;

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_lists);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        list.addAll(db.getLists());

        TextView count = (TextView) findViewById(R.id.count);

        count.setText("Lists remain: "+db.ACTUAL_LISTS+"/"+db.MAX_LISTS);

        recycler = (RecyclerView) findViewById(R.id.reciclador);

        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        adapter = new getLists(list, this);
        recycler.setAdapter(adapter);
    }

    public void randomize(View v) {
        Intent intent = new Intent(this, PrincipalPage.class);
        intent.putExtra("title", title);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startService(intent);

    }
}

class getLists extends RecyclerView.Adapter<getLists.ViewLists> {
    private List<Lists> count;
    Context context;

    public static class ViewLists extends RecyclerView.ViewHolder {

        public TextView tit;
        public ImageView ran;
        public ImageView del;

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

                    db.removeList();
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
