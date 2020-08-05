package com.possederhana.kasir.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.possederhana.kasir.DaftarMenu.DaftarMenuActivity;
import com.possederhana.kasir.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void openDaftarMenu(View view) {
        Intent ListVideo = new Intent(getApplicationContext(), DaftarMenuActivity.class);
        startActivity(ListVideo);
    }
}