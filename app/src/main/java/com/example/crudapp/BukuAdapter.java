package com.example.crudapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;

public class BukuAdapter extends RecyclerView.Adapter<BukuAdapter.BukuViewHolder> {
    public ArrayList<Buku> dataList;
    private onDataListener mOnDataListener;

    public BukuAdapter(ArrayList<Buku> dataList, onDataListener bukuListener) {
        this.dataList = dataList;
        this.mOnDataListener = bukuListener;
    }

    @NonNull
    @Override
    public BukuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView view = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_buku, parent, false);
        return new BukuViewHolder(view, mOnDataListener);
    }

    @Override
    public void onBindViewHolder(BukuViewHolder holder, int position) {
        Buku buku = dataList.get(holder.getAdapterPosition());

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getRandomColor();

        holder.cvMain.setCardBackgroundColor(color);
        holder.etJudul.setText("Judul\t\t\t\t\t\t:\t\t" + buku.getJudul());
        holder.etPenulis.setText("Penulis\t\t\t\t:\t\t" + buku.getPenulis());
        holder.etKategori.setText("Kategori\t\t\t:\t\t" + buku.getKategori());

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class BukuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CardView cvMain;
        private TextView etJudul, etPenulis, etKategori;
        onDataListener bukuListener;

        public BukuViewHolder(View itemView, onDataListener bukuListener) {
            super(itemView);
            cvMain = itemView.findViewById(R.id.cvMain);
            etJudul = itemView.findViewById(R.id.TV_judul_buku);
            etPenulis = itemView.findViewById(R.id.TV_nama_penulis);
            etKategori = itemView.findViewById(R.id.TV_kategori);
            this.bukuListener = bukuListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            bukuListener.onDataClick(dataList.get(getAdapterPosition()), getAdapterPosition());
        }
    }

    //interface data listener
    public interface onDataListener {
        void onDataClick(Buku buku, int position);
    }
}

