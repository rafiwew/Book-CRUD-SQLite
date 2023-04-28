package com.example.crudapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME =  "tbl_buku";
    public static final String COLUMN_ID =  "id";
    public static final String COLUMN_JUDUL =  "judul";
    public static final String COLUMN_PENULIS =  "penulis";
    public static final String COLUMN_KATEGORI =  "kategori";
    private static final String DATABASE_NAME =  "db_buku";
    private static final int DATABASE_VERSION =  1;

    // Create SQL
    private static final String CREATE_TABLE = "CREATE TABLE " +
            TABLE_NAME + " (" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_JUDUL + " varchar not null, " +
            COLUMN_PENULIS + " varchar not null, " +
            COLUMN_KATEGORI + " varchar not null " +
            " )";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHelper.class.getName(), "Upgrade DB dari versi");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
