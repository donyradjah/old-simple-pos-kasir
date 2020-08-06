package com.possederhana.kasir.DaftarTransaksi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kinda.alert.KAlertDialog;
import com.possederhana.kasir.DaftarMenu.DaftarMenuActivity;
import com.possederhana.kasir.DaftarMenu.MenuSection;
import com.possederhana.kasir.R;
import com.possederhana.kasir.model.Kategori;
import com.possederhana.kasir.model.Produk;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

import static android.content.ContentValues.TAG;

public class DaftarTransaksiActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    KAlertDialog pDialog;

    ArrayList<Kategori> kategoris = new ArrayList<>();
    RecyclerView rvListMenu;

    ImageView btnMenu;
    TextView txtMenu;

    private SectionedRecyclerViewAdapter sectionedAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_transaksi);


    }

    void init() {

        myRef = database.getReference();
        sectionedAdapter = new SectionedRecyclerViewAdapter();

        pDialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Sedang Mengambil Data ...");
        pDialog.setCancelable(false);

        if (!pDialog.isShowing()) {
            pDialog.show();
        }

        rvListMenu = findViewById(R.id.listMenu);
        rvListMenu.setLayoutManager(new LinearLayoutManager(this));
        rvListMenu.setHasFixedSize(true);
        rvListMenu.setItemAnimator(new DefaultItemAnimator());


        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                kategoris.clear();
                sectionedAdapter.removeAllSections();

                for (DataSnapshot data : dataSnapshot.child("kategori").getChildren()) {

                    Kategori kategori = new Kategori();
                    kategori.setId(Integer.parseInt(data.getKey()));
                    kategori.setKategori(data.child("kategori").getValue().toString());
                    kategori.setProduks(new ArrayList<Produk>());
                    kategoris.add(kategori);
                }

                for (DataSnapshot data : dataSnapshot.child("produk").getChildren()) {

                    Produk produk = new Produk();

                    produk.setId(Integer.parseInt(data.getKey()));
                    produk.setNamaProduk(data.child("namaProduk").getValue().toString());
                    produk.setHarga(Integer.parseInt(data.child("harga").getValue().toString()));
                    produk.setKodeProduk(data.child("kodeProduk").getValue().toString());
                    produk.setGambarProduk(data.child("gambarProduk").getValue().toString());
                    produk.setKategoriId(data.child("kategoriId").getValue().toString());
                    produk.setStatus(data.child("status").getValue().toString());

                    String[] kategoriId = data.child("kategoriId").getValue().toString().split(",");

                    for (String s : kategoriId) {
                        int idKategori = Integer.parseInt(s);
                        int keyKategori = cariKategoriById(idKategori);
                        if (keyKategori >= 0) {
                            ArrayList<Produk> produks = kategoris.get(keyKategori).getProduks();

                            produks.add(produk);
                            kategoris.get(keyKategori).setProduks(produks);
                        }
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }
}