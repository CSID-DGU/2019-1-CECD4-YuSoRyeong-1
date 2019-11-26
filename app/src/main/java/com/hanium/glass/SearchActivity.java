package com.hanium.glass;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    EditText keywordView;
    ListView listView;
    ArrayAdapter<POI> mAdapter;
    Button btn;
    TMapView mapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        keywordView = (EditText) findViewById(R.id.edit_keyword);
        listView = (ListView) findViewById(R.id.listView);
        mAdapter = new ArrayAdapter<POI>(this, android.R.layout.simple_list_item_1){
            @Override

            public View getView(int position, View convertView, ViewGroup parent)

            {

                View view = super.getView(position, convertView, parent);

                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                tv.setTextColor(Color.WHITE);

                return view;

            }

        };
        listView.setAdapter(mAdapter);
        mapView = ((MainActivity)MainActivity.mContext).tMapview;

        btn = (Button) findViewById(R.id.btn_search);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchPOI();
            }

        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                POI poi = (POI) listView.getItemAtPosition(position);
                final double latPOI = poi.getPoint().getLatitude();
                final double lonPOI = poi.getPoint().getLongitude();

                AlertDialog.Builder oDialog = new AlertDialog.Builder(SearchActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);

                oDialog.setTitle(poi.toString())
                        .setMessage(poi.item.getPOIAddress().replace("null",""))
                        .setNeutralButton("길찾기", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                finish();
                                Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                                intent.putExtra("end_lat", latPOI);
                                intent.putExtra("end_lon",lonPOI);

                                startActivity(intent);
                            }
                        })
                        .setCancelable(true)

                        .show();

            }
        });

    }

    private void searchPOI() {
        TMapData data = new TMapData();
        String keyword = keywordView.getText().toString();
        if (!TextUtils.isEmpty(keyword)) {
            data.findAllPOI(keyword, new TMapData.FindAllPOIListenerCallback() {
                @Override
                public void onFindAllPOI(final ArrayList<TMapPOIItem> arrayList) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.clear();

                            for (TMapPOIItem poi : arrayList) {
                                mAdapter.add(new POI(poi));
                            }

                            if (arrayList.size() > 0) {
                                TMapPOIItem poi = arrayList.get(0);
                            }
                        }
                    });
                }
            });
        }
    }
}
