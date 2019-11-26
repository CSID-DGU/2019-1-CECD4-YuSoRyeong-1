package com.hanium.glass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

public class RecommandActivity extends AppCompatActivity implements View.OnClickListener {

    Button place1DetailButton;
    Button place2DetailButton;
    Button place3DetailButton;


    Button startNavi;

    TMapView mapView;
    TextView  name;
    TextView address;
    TextView telNo;
    TextView detail;
    ImageView detailImage;

    RelativeLayout detailLayout;
    RelativeLayout recommandLayout;

    Intent intent;
    double end_lat;
    double end_lon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        mapView = ((MainActivity)MainActivity.mContext).tMapview;
        place1DetailButton = (Button)findViewById(R.id.place1);
        place2DetailButton = (Button)findViewById(R.id.place2);
        place3DetailButton = (Button)findViewById(R.id.place3);

        startNavi= (Button)findViewById(R.id.searchRootButton);

        name = (TextView)findViewById(R.id.detailName);
        address=(TextView)findViewById(R.id.detailLocation);
        telNo = (TextView)findViewById(R.id.detailTel);
        detail = (TextView)findViewById(R.id.detailContent);
        detailImage = (ImageView)findViewById(R.id.detailImage);

        detailLayout = (RelativeLayout)findViewById(R.id.detailLayout);
        recommandLayout = (RelativeLayout) findViewById(R.id.relativeLayout2);

        place1DetailButton.setOnClickListener(this);
        place2DetailButton.setOnClickListener(this);
        place3DetailButton.setOnClickListener(this);

        startNavi.setOnClickListener(this);

        intent = getIntent();
        final int gu = intent.getExtras().getInt("gu");
        Log.d("구이름",gu + "");
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.searchRootButton) {
            finish();
            Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
            double new_lat = end_lat;
            double new_lon = end_lon;
            intent.putExtra("end_lat", new_lat);
            intent.putExtra("end_lon",new_lon);

            startActivity(intent);
        }
        else {
            TMapData data = new TMapData();

            String strData = ((Button) v).getText().toString();


            Log.d("Detail", strData);
            // setContentView(R.layout.activity_detail);
            try {
                data.findAllPOI(strData, new TMapData.FindAllPOIListenerCallback() {
                    @Override
                    public void onFindAllPOI(final ArrayList<TMapPOIItem> arrayList) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (arrayList.size() > 0) {
                                    TMapPOIItem poi = arrayList.get(0);
                                    name.setText(poi.getPOIName());
                                    address.setText(poi.getPOIAddress().replace("null", ""));
                                    telNo.setText(poi.telNo);
                                    detail.setText(poi.desc);

                                    end_lat = poi.getPOIPoint().getLatitude();
                                    end_lon = poi.getPOIPoint().getLongitude();


                                    Log.d("CONTENT", poi.getPOIContent() + "\n" + poi.desc);


                                }
                            }
                        });
                    }
                });

                if(strData.equals("N서울타워")) detailImage.setImageResource(R.drawable.i_nseoultower);
                else if (strData.equals("만선호프")) detailImage.setImageResource(R.drawable.i_fullshiphof);
                else if  (strData.equals("덕수궁")) detailImage.setImageResource(R.drawable.deoksugung);

                detailLayout.setVisibility(View.VISIBLE);
                recommandLayout.setVisibility(View.INVISIBLE);

            } catch (Exception e) {
                Log.d("Show detail", e.getMessage());
            }
        }

    }
    private  long time =0;
    @Override
    public void onBackPressed() {

        if(System.currentTimeMillis()-time>=2000){
            time=System.currentTimeMillis();
            detailLayout.setVisibility(View.INVISIBLE);
            recommandLayout.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(),"뒤로 버튼을 한번 더 누르면 메인으로 이동합니다.",Toast.LENGTH_SHORT).show();
        }else if(System.currentTimeMillis()-time<2000){
            finish();
            return;
        }


    }

}