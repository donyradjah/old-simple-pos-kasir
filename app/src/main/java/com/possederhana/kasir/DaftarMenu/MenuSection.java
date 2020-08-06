package com.possederhana.kasir.DaftarMenu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kinda.alert.KAlertDialog;
import com.possederhana.kasir.R;
import com.possederhana.kasir.helper.BaseApiService;
import com.possederhana.kasir.helper.UrlApi;
import com.possederhana.kasir.model.Kategori;
import com.possederhana.kasir.model.Produk;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuSection extends Section {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    private final Context context;
    private final Kategori kategori;
    private final ArrayList<Produk> produks;
    private KAlertDialog pDialog;
    private BaseApiService apiService;

    public MenuSection(Activity activity,Context context, Kategori kategori, ArrayList<Produk> produks) {
        super(SectionParameters.builder()
                .headerResourceId(R.layout.menu_kategori_header)
                .itemResourceId(R.layout.menu_produk_item)
                .build());

        this.pDialog = new KAlertDialog(activity, KAlertDialog.PROGRESS_TYPE);
        this.pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        this.pDialog.setTitleText("Sedang memproses ..");
        this.pDialog.setCancelable(false);

        this.apiService = UrlApi.getAPIService();

        this.kategori = kategori;
        this.produks = produks;
        this.context = context;
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

    void gantiStatus(int id, String status) {
        if (!pDialog.isShowing()) {
            pDialog.show();
        }

        apiService.gantiStatus(id, status).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        try {
                            String data = response.body().string();
                            Log.d("LOGIN", data);
                            JSONObject result = new JSONObject(data);

                            Log.d("LOGIN", result.getString("success"));
                            if (!result.getBoolean("success")) {
                                pDialog.dismiss();

                            } else {
                                pDialog.dismiss();

                            }

                        } catch (JSONException | IOException | NullPointerException e) {
                            e.printStackTrace();
                            pDialog.dismiss();
                        }
                    } else {
                        pDialog.dismiss();

                    }
                } else {
                    pDialog.dismiss();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyItemViewHolder itemHolder = (MyItemViewHolder) holder;
        Log.d("Produk", produks.get(position).getNamaProduk());
        String url = "https://homeschoolingmalang.com/kasir-cms/upload/produk/" + produks.get(position).getGambarProduk();

        Picasso.with(context).load(url).into(itemHolder.imgItem);

        if (produks.get(position).getStatus().equals("tersedia")) {
            itemHolder.switchItem.setChecked(true);
        } else {
            itemHolder.switchItem.setChecked(false);
        }

        // bind your view here
        itemHolder.tvItem.setText(produks.get(position).getNamaProduk());
        itemHolder.tvHarga.setText(formatRupiah((double) produks.get(position).getHarga()));

        itemHolder.switchItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    gantiStatus(produks.get(position).getId(), "tersedia");
                } else {
                    gantiStatus(produks.get(position).getId(), "kosong");
                }
            }
        });
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
        Log.d("Kategori Produk", kategori.getKategori());
        itemHolder.tvItem.setText(kategori.getKategori());
    }

    static class MyItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvItem, tvHarga;
        private final ImageView imgItem;
        private final Switch switchItem;

        public MyItemViewHolder(View itemView) {
            super(itemView);

            tvItem = (TextView) itemView.findViewById(R.id.tvItem);
            tvHarga = (TextView) itemView.findViewById(R.id.tvHarga);
            imgItem = itemView.findViewById(R.id.imgItem);
            switchItem = itemView.findViewById(R.id.switchItem);
        }
    }

    static class MyHeaderViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvItem;

        public MyHeaderViewHolder(View itemView) {
            super(itemView);

            tvItem = (TextView) itemView.findViewById(R.id.tvTitle);
        }
    }

    private String formatRupiah(Double number) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }
}
