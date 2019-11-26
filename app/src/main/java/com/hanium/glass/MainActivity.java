package com.hanium.glass;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.skt.Tmap.TMapView;

public class MainActivity extends AppCompatActivity {
    public TMapView tMapview;
    public static Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        tMapview = new TMapView(this);



        tMapview.setSKTMapApiKey("");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
    }

    public void onButtonNavigationClicked(View v) {
        Intent intent=new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    public void onButtonTranslationClicked(View v) {
        Intent intent=new Intent(MainActivity.this, MapActivity.class);

        startActivity(intent);
    }
}
