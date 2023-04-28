package com.example.crudapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBDataSource {
    private SQLiteDatabase database;
    private DBHelper dbHelper;

    private  String[] allColumns = {
            DBHelper.COLUMN_ID,
            DBHelper.COLUMN_JUDUL,
            DBHelper.COLUMN_PENULIS,
            DBHelper.COLUMN_KATEGORI,
    };

    public DBDataSource(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Buku createBuku(String judul, String penulis, String kategori) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_JUDUL, judul);
        values.put(DBHelper.COLUMN_PENULIS, penulis);
        values.put(DBHelper.COLUMN_KATEGORI, kategori);

        long insertID = database.insert(DBHelper.TABLE_NAME, null, values);
        Cursor cursor = database.query(DBHelper.TABLE_NAME, allColumns,
                DBHelper.COLUMN_ID + " = " + insertID, null , null, null,null);
        cursor.moveToFirst();
        Buku newBuku = cursorToBuku(cursor);
        cursor.close();
        return newBuku;
    };

    private Buku cursorToBuku(Cursor cursor) {
        Buku buku = new Buku();

        buku.setId(cursor.getLong(0));
        buku.setJudul(cursor.getString(1));
        buku.setPenulis(cursor.getString(2));
        buku.setKategori(cursor.getString(3));
        return buku;
    };

    public ArrayList<Buku> getAllBuku() {
        ArrayList<Buku> dataList = new ArrayList<>();
        Cursor cursor = database.query(DBHelper.TABLE_NAME, allColumns, null, null, null, null, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            Buku buku = cursorToBuku(cursor);
            dataList.add(buku);
            cursor.moveToNext();
        }
        cursor.close();
        return dataList;
    }

    public Buku getBuku(long id) {
        // Inisialisasi buku
        Buku buku = new Buku();

        // Query SELECT
        Cursor cursor = database.query(DBHelper.TABLE_NAME, allColumns, "id =" + id, null,null,null, null);
        cursor.moveToFirst();

        buku = cursorToBuku(cursor);

        cursor.close();
        return buku;
    }

    public void updateBuku(Buku buku) {
        // Mengambil ID buku
        String strFilter = "id=" + buku.getId();

        // Memasukkan ke content values
        ContentValues args = new ContentValues();

        // Masukkan data sesuai dengan kolom pada tabel
        args.put(DBHelper.COLUMN_JUDUL, buku.getJudul());
        args.put(DBHelper.COLUMN_PENULIS, buku.getPenulis());
        args.put(DBHelper.COLUMN_KATEGORI, buku.getKategori());

        // Update query
        database.update(DBHelper.TABLE_NAME, args, strFilter, null);
    }

    public void deleteBuku(long id) {
        String filterID = "id=" + id;
        database.delete(DBHelper.TABLE_NAME, filterID,null);
    }

}

