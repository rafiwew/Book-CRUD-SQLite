package com.example.crudapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BukuAdapter.onDataListener {

    DBDataSource dataSource;
    private ExtendedFloatingActionButton mFloatingActionButton;
    private EditText mEditJudul;
    private EditText mEditPenulis;
    private EditText mEditKategori;
    private RecyclerView mRecyclerView;
    private ArrayList<Buku> dataBuku;
    private BukuAdapter adapterBuku;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        dataSource = new DBDataSource(this);
        dataSource.open(); // buka controller
        dataBuku = dataSource.getAllBuku(); // mengambil semua data buku

        ArrayList<Buku> bukuList = dataBuku;
        adapterBuku = new BukuAdapter(bukuList, this);
        mRecyclerView.setAdapter(adapterBuku);

        mFloatingActionButton = findViewById(R.id.btn_tambah_buku);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTambahBuku();
            }
        });

        // loadData();
    }

    // method untuk load data awal pada RecyclerView
    private void loadData() {
        DBDataSource data = new DBDataSource(this);
        data.open();
        dataBuku.addAll(data.getAllBuku());
        adapterBuku.notifyDataSetChanged();
        data.close();
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void dialogTambahBuku() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tambah Buku");
        View view = getLayoutInflater().inflate(R.layout.layout_edit, null);

        mEditJudul = view.findViewById(R.id.judul_buku);
        mEditPenulis = view.findViewById(R.id.nama_penulis);
        mEditKategori = view.findViewById(R.id.kategori);

        dataSource = new DBDataSource(this);
        dataSource.open();
        builder.setView(view);

        builder.setPositiveButton("SIMPAN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                String judulBuku = mEditJudul.getText().toString();
                String namaPenulis = mEditPenulis.getText().toString();
                String kategoriBuku = mEditKategori.getText().toString();

                if (!judulBuku.isEmpty() && !namaPenulis.isEmpty() && !kategoriBuku.isEmpty()) {
                    submitDataBuku();
                } else {
                    Toast.makeText(MainActivity.this, "Gagal! Semua field harus diisi!", Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("BATAL", null);
        Dialog dialog = builder.create();
        dialog.show();
    }

    private void submitDataBuku() {
        String judul = null;
        String penulis = null;
        String kategori = null;

        Buku buku = null;

        judul = mEditJudul.getText().toString();
        penulis = mEditPenulis.getText().toString();
        kategori = mEditKategori.getText().toString();

        buku = dataSource.createBuku(judul, penulis, kategori);
        Toast.makeText(this, "Buku " + buku.getJudul() + " berhasil ditambahkan!", Toast.LENGTH_LONG).show();
        dataBuku.clear(); // clear list dataBuku
        dataBuku.addAll(dataSource.getAllBuku()); // tambahkan data baru dari database ke list
        adapterBuku.notifyDataSetChanged(); // update RecyclerView
    }

    private void updateDataBuku(Buku buku) {
        DBDataSource dataSource = new DBDataSource(this);
        dataSource.open();

        dataSource.updateBuku(buku);
        dataBuku.clear();
        dataBuku.addAll(dataSource.getAllBuku());
        adapterBuku.notifyDataSetChanged();
        dataSource.close();

        Toast.makeText(this, "Buku " + buku.getJudul() + " berhasil diedit!", Toast.LENGTH_LONG).show();

    }

    private void dialogUpdateBuku(final Buku buku) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Data Buku");
        View view = getLayoutInflater().inflate(R.layout.layout_edit, null);

        mEditJudul = view.findViewById(R.id.judul_buku);
        mEditPenulis = view.findViewById(R.id.nama_penulis);
        mEditKategori = view.findViewById(R.id.kategori);

        mEditJudul.setText(buku.getJudul());
        mEditPenulis.setText(buku.getPenulis());
        mEditKategori.setText(buku.getKategori());
        builder.setView(view);

        if (buku != null) {
            builder.setPositiveButton("SIMPAN", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    String judulBaru = mEditJudul.getText().toString();
                    String penulisBaru = mEditPenulis.getText().toString();
                    String kategoriBaru = mEditKategori.getText().toString();

                    // Periksa apakah data berbeda dengan data asli
                    if (!buku.getJudul().equals(judulBaru) ||
                            !buku.getPenulis().equals(penulisBaru) ||
                            !buku.getKategori().equals(kategoriBaru)) {
                        buku.setJudul(judulBaru);
                        buku.setPenulis(penulisBaru);
                        buku.setKategori(kategoriBaru);
                        updateDataBuku(buku);
                    } else {
                        Toast.makeText(MainActivity.this, "Tidak ada data yang diubah!", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }

        builder.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        Dialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void onDataClick(final Buku b, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih Aksi");

        builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialogUpdateBuku(b);
            }
        });

        builder.setNegativeButton("HAPUS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                DBDataSource dataSource = new DBDataSource(MainActivity.this);
                dataSource.open();
                dataSource.deleteBuku(b.getId());
                dataBuku.clear();
                dataBuku.addAll(dataSource.getAllBuku());
                adapterBuku.notifyDataSetChanged();
                dataSource.close();
            }
        });

        builder.setNeutralButton("BATAL", null);

        Dialog dialog = builder.create();
        dialog.show();
    }
}