package com.pauapps.pau.allrandomizer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pau on 14/10/2018.
 */

public class DB extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "lists.db";

    public static final String TABLE_NAME = "lists";
    public static final String TABLE_INDEX = "index";
    public static final String TABLE_TITLE = "title";
    public static final String TABLE_ITEM = "item";

    public int MAX_LISTS = 2;
    public static int ACTUAL_LISTS;

    boolean delete = false;


    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME +
                "( '" + TABLE_INDEX + "' INTEGER  PRIMARY KEY AUTOINCREMENT, "
                + TABLE_TITLE + " TEXT NOT NULL, "
                + TABLE_ITEM + " TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insert(Lists list, String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (ACTUAL_LISTS < MAX_LISTS) {
            if (!delete) {
                delete(title, db);
            }
            values.put(TABLE_TITLE, list.getTitle());
            values.put(TABLE_ITEM, list.getItem());

            db.insert(TABLE_NAME, null, values);
            db.close();
        }
    }

    public void delete(String title, SQLiteDatabase db) {
        try {
            db.delete(TABLE_NAME, TABLE_TITLE + " = '" + title + "'", null);
            delete = true;
        } catch (Exception e) {

        }
    }

    public void delete(String title, Context con) {
        DB bd = new DB(con);
        SQLiteDatabase db = bd.getWritableDatabase();
        db.delete(TABLE_NAME, TABLE_TITLE + " = '" + title + "'", null);
    }

    public List<Lists> getLists() {
        List<Lists> list = new ArrayList<Lists>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{TABLE_TITLE}, null,
                null, TABLE_TITLE, null, null);

        if (cursor.moveToFirst()) {
            do {
                Lists lists = new Lists();
                lists.setTitle(cursor.getString(0));
                list.add(lists);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public ArrayList<String> getItems(String title, Context con) {
        ArrayList<String> list = new ArrayList<>();

        DB bd = new DB(con);
        SQLiteDatabase db = bd.getReadableDatabase();
        String query = "SELECT " + TABLE_ITEM + " FROM " + TABLE_NAME + " WHERE " +
                TABLE_TITLE + " = '" + title + "'";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public void addList() {
        ACTUAL_LISTS = ACTUAL_LISTS + 1;
    }

    public void removeList() {
        ACTUAL_LISTS = ACTUAL_LISTS - 1;
    }
}
