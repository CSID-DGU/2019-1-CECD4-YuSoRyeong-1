package com.hanium.glass;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.skt.Tmap.TMapView;

public class MapActivity extends AppCompatActivity {
    ImageView seoulMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        seoulMap = (ImageView)findViewById(R.id.seoul);

        seoulMap.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    int x = (int)event.getX();
                    int y = (int)event.getY();

                    Log.d("COORD : ", x + ", " +y);

                    movePage(x, y);
                }
                return false;
            }

        });
    }

    private void movePage(int x, int y) {
        int gu = -1;
        if(x<=681&&x>=637 && y<=402  && y>= 290) gu = 0; // 종로, 중구, 용산
        else if(x<=815&&x>=681 && y<=214  && y>= 94) gu = 1; // 도봉, 강북, 성북, 노원
        else if(x<=810&&x>=739 && y<=383  && y>= 290) gu = 2; // 동대문, 중랑, 성동, 광진
        else if(x<=880&&x>=834 && y<=485  && y>= 445) gu = 3; // 강동, 송파
        else if(x<=777&&x>=668 && y<=530  && y>= 468) gu = 4; // 서초, 강남
        else if(x<=636&&x>=520 && y<=575  && y>= 525) gu = 5; // 동작, 관악, 금천
        else if(x<=510&&x>=406 && y<=479  && y>= 393) gu = 6; //강서, 양천, 영등포, 구로
        else if(x<=602&&x>=533 && y<=353  && y>= 213) gu = 7; //은평, 마포 서대문

        if(gu >= 0) {
            Intent intent = new Intent(MapActivity.this, RecommandActivity.class);
            intent.putExtra("gu", gu);

            startActivity(intent);
        }
        else Toast.makeText(getApplicationContext(),"해당 구역의 중앙을 클릭해주세요",Toast.LENGTH_SHORT).show();


    }

}
