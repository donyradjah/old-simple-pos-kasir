package com.possederhana.kasir.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class HomeFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    KAlertDialog pDialog;

    ArrayList<Kategori> kategoris = new ArrayList<>();
    RecyclerView rvListMenu;
    private SectionedRecyclerViewAdapter sectionedAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        init(view);

        return view;
    }

    void init(View view) {

        myRef = database.getReference("najieb-pos");
        sectionedAdapter = new SectionedRecyclerViewAdapter();

        pDialog = new KAlertDialog(getActivity().getApplicationContext(), KAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Sedang Mengambil Data ...");
        pDialog.setCancelable(false);

//        if (!pDialog.isShowing()) {
//            pDialog.show();
//        }

        rvListMenu = view.findViewById(R.id.listMenu);
        rvListMenu.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
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

                    Kategori kategori = new Kategori(Integer.parseInt(data.getKey()), data.child("kategori").getValue().toString());
                    System.out.println("Kategori : " + data.child("kategori").getValue().toString());
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
                    MenuSection menuSection = new MenuSection(kategori, kategori.getProduks());
                    System.out.println("Kategori : " + kategori.getKategori());
                    sectionedAdapter.addSection(menuSection);
                }

                rvListMenu.setAdapter(sectionedAdapter);
                sectionedAdapter.notifyDataSetChanged();
//                pDialog.dismissWithAnimation();

                Toast.makeText(getActivity().getApplicationContext(), "selesai", Toast.LENGTH_LONG).show();
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
