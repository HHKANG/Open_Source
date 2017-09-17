package com.example.samsung.os;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class Main_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Intent intent = getIntent();
        String key = intent.getStringExtra("key");
        Toast.makeText(getApplicationContext(),key,Toast.LENGTH_SHORT).show();
    }
}