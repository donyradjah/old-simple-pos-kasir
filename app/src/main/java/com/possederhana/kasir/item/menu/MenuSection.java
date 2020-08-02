package com.possederhana.kasir.item.menu;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.possederhana.kasir.R;
import com.possederhana.kasir.model.Kategori;
import com.possederhana.kasir.model.Produk;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;

public class MenuSection extends Section {

    private final Kategori kategori;
    private final ArrayList<Produk> produks;

    public MenuSection(Kategori kategori, ArrayList<Produk> produks) {
        super(SectionParameters.builder()
                .headerResourceId(R.layout.menu_kategori_header)
                .itemResourceId(R.layout.menu_produk_item)
                .build());

        this.kategori = kategori;
        this.produks = produks;
    }

    @Override
    public int getContentItemsTotal() {
        return produks.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        // return a custom instance of ViewHolder for the items of this section
        return new MyItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyItemViewHolder itemHolder = (MyItemViewHolder) holder;

        // bind your view here
        itemHolder.tvItem.setText(produks.get(position).getNamaProduk());
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        // return an empty instance of ViewHolder for the headers of this section
        return new MyHeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        super.onBindHeaderViewHolder(holder);

        MyHeaderViewHolder itemHolder = (MyHeaderViewHolder) holder;

        itemHolder.tvItem.setText(kategori.getKategori());
    }

    static class MyItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvItem;

        public MyItemViewHolder(View itemView) {
            super(itemView);

            tvItem = (TextView) itemView.findViewById(R.id.tvItem);
        }
    }

    static class MyHeaderViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvItem;

        public MyHeaderViewHolder(View itemView) {
            super(itemView);

            tvItem = (TextView) itemView.findViewById(R.id.tvItem);
        }
    }
}
