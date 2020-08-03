package com.possederhana.kasir.DaftarMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kinda.alert.KAlertDialog;
import com.possederhana.kasir.R;
import com.possederhana.kasir.item.menu.MenuSection;
import com.possederhana.kasir.model.Kategori;
import com.possederhana.kasir.model.Produk;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

import static android.content.ContentValues.TAG;

public class DaftarMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    KAlertDialog pDialog;

    ArrayList<Kategori> kategoris = new ArrayList<>();
    RecyclerView rvListMenu;
    private SectionedRecyclerViewAdapter sectionedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        init();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

                for (Kategori kategori : kategoris) {
                    MenuSection menuSection = new MenuSection(getApplicationContext(), kategori, kategori.getProduks());
                    System.out.println("Kategori : " + kategori.getKategori());
                    sectionedAdapter.addSection(menuSection);
                }

                rvListMenu.setAdapter(sectionedAdapter);
                sectionedAdapter.notifyDataSetChanged();
                pDialog.dismissWithAnimation();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
                pDialog.dismissWithAnimation();
            }
        });

    }

    int cariKategoriById(int id) {
        int i = 0;
        for (Kategori kategori : kategoris) {
            if (kategori.getId() == id) {
                return i;
            }

            i++;
        }

        return -1;
    }

}