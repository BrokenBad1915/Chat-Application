package com.example.chatics;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class splashscreen extends AppCompatActivity {

    private int x= 1500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        //can also use threadpoolexecutor but voh samaj nahi aa rha
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(splashscreen.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        },x);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
}