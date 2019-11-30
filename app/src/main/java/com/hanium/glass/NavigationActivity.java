package com.hanium.glass;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.hanium.glass.Augmented.GLClearRenderer;
import com.hanium.glass.Utils.ScreenUtils;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class NavigationActivity extends AppCompatActivity {

    Switch arSwitch;
    double longitude;
    double latitude;
    String direction;
    static public Context nContext;

    private FrameLayout arSurface;
    private FrameLayout linearLayoutTmap;
    // OpenGL
    private GLSurfaceView glSurfaceView;
    public GLClearRenderer renderer;

    private int RESULT_PERMISSIONS = 100;

    TMapPoint tMapPointStart;
    TMapView mapView;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        nContext = this;
        arSwitch = (Switch) findViewById(R.id.ARSwitch);
        linearLayoutTmap = (FrameLayout) findViewById(R.id.linearLayoutTmap);

        arSurface = (FrameLayout) findViewById(R.id.arSurface);

       // mapView = ((MainActivity)MainActivity.mContext).tMapview;

        mapView = new TMapView(this);

        mapView.setSKTMapApiKey("");



        //토글버튼 클릭
        arSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) { // AR mode
//                  //  finish();
//                    Intent intent = new Intent(NavigationActivity.this, TranslationActivity.class);
//                    startActivity(intent);
                    arSurface.setVisibility(View.VISIBLE);


                } else { // tMap mode
                    arSurface.setVisibility(View.INVISIBLE);
                }
            }
        });

        intent = getIntent();
        final double end_lat = intent.getExtras().getDouble("end_lat");
        final double end_lon = intent.getExtras().getDouble("end_lon");

        final TMapData tMapdata = new TMapData();
        final TMapPoint tMapPointEnd = new TMapPoint(end_lat, end_lon);

        startAR();
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(NavigationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            try {

                String locationProvider = LocationManager.GPS_PROVIDER;
                Location location = lm.getLastKnownLocation(locationProvider);

               // TMapPoint location = tmapgps.getLocation();

                if(location == null) {
                    longitude = 126.998922;
                    latitude = 37.558555;
                }
                else {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                }

                Log.d("location : ", latitude + ", longitude : " + longitude);

                Log.d("ADD VIEW", "");
                mapView.setCenterPoint(longitude, latitude);
                linearLayoutTmap.addView(mapView);

                tMapPointStart = new TMapPoint(latitude, longitude);

                tMapdata.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, tMapPointStart, tMapPointEnd, new TMapData.FindPathDataListenerCallback() {
                    @Override
                    public void onFindPathData(TMapPolyLine polyLine) {
                        Log.i("POLYLINE","");
                        mapView.addTMapPath(polyLine);
                    }
                });

                tMapdata.findPathDataAllType(TMapData.TMapPathType.PEDESTRIAN_PATH, tMapPointStart, tMapPointEnd, new TMapData.FindPathDataAllListenerCallback() {
                    @Override
                    public void onFindPathDataAll(Document document) {
                        Element root = document.getDocumentElement();
                        NodeList nodeListPlacemark = root.getElementsByTagName("Placemark");
                        // for (int i = 0; i < nodeListPlacemark.getLength(); i++) {
                        NodeList nodeListPlacemarkItem = nodeListPlacemark.item(2).getChildNodes();
                        // for (int j = 0; j < nodeListPlacemarkItem.getLength(); j++) {
                        if (nodeListPlacemarkItem.item(23).getNodeName().equals("tmap:turnType")) {
                            Log.d("turnType", nodeListPlacemarkItem.item(23).getTextContent().trim());
                            direction = nodeListPlacemarkItem.item(23).getTextContent().trim();
                            if(direction.equals("11"))  renderer.setAngleZ(0f);
                            else if(direction.equals("12"))  renderer.setAngleZ(90f);//좌회전
                            else if(direction.equals("13"))  renderer.setAngleZ(-90f);
                            else if(direction.equals("14"))  renderer.setAngleZ(180f);
                            else if(direction.equals("16"))  renderer.setAngleZ(120f);
                            else if(direction.equals("17"))  renderer.setAngleZ(30f);
                            else if(direction.equals("18"))  renderer.setAngleZ(-30f);
                            else if(direction.equals("19"))  renderer.setAngleZ(-120f);

                        }
                        //}
                        //}
                    }
                });


            } catch (Exception e) {
                Log.d("INITERROR:: ", e.getMessage());
            }
        }

        try {
            LocationListener gpsLocationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();
                        mapView.setCenterPoint(longitude, latitude);

                        Log.d("location changed: ", latitude + ", longitude : " + longitude);

                        if(abs(longitude- end_lon) < 0.0001 && abs(latitude-end_lat) < 0.0001) Toast.makeText(getApplicationContext(),"목적지에 도착하였습니다",Toast.LENGTH_SHORT).show();
                        else {

                            tMapPointStart = new TMapPoint(latitude, longitude);

                            tMapdata.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, tMapPointStart, tMapPointEnd, new TMapData.FindPathDataListenerCallback() {
                                @Override
                                public void onFindPathData(TMapPolyLine polyLine) {
                                    mapView.addTMapPath(polyLine);
                                }
                            });

                            tMapdata.findPathDataAllType(TMapData.TMapPathType.PEDESTRIAN_PATH, tMapPointStart, tMapPointEnd, new TMapData.FindPathDataAllListenerCallback() {
                                @Override
                                public void onFindPathDataAll(Document document) {
                                    Element root = document.getDocumentElement();
                                    NodeList nodeListPlacemark = root.getElementsByTagName("Placemark");
                                    // for (int i = 0; i < nodeListPlacemark.getLength(); i++) {
                                    NodeList nodeListPlacemarkItem = nodeListPlacemark.item(2).getChildNodes();
                                    // for (int j = 0; j < nodeListPlacemarkItem.getLength(); j++) {
                                    if (nodeListPlacemarkItem.item(23).getNodeName().equals("tmap:turnType")) {
                                        Log.d("turnType", nodeListPlacemarkItem.item(23).getTextContent().trim());
                                        direction = nodeListPlacemarkItem.item(23).getTextContent().trim();
                                        if (direction.equals("11")) renderer.setAngleZ(90f);
                                        else if (direction.equals("12"))
                                            renderer.setAngleZ(0f);//좌회전
                                        else if (direction.equals("13")) renderer.setAngleZ(-90f);
                                        else if (direction.equals("14")) renderer.setAngleZ(180f);
                                        else if (direction.equals("16")) renderer.setAngleZ(120f);
                                        else if (direction.equals("17")) renderer.setAngleZ(30f);
                                        else if (direction.equals("18")) renderer.setAngleZ(-30f);
                                        else if (direction.equals("19")) renderer.setAngleZ(-120f);


                                    }
                                    //}
                                    //}
                                }
                            });
                        }

                    }
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                public void onProviderEnabled(String provider) {
                }

                public void onProviderDisabled(String provider) {
                }
            };
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, gpsLocationListener);
        }catch (Exception e){
            Log.d("GPSERROR",e.getMessage());
        }

    }

    private void startAR(){
        try{
            float screenHeight = ScreenUtils.getScreenHeight(this);


            ViewGroup.LayoutParams arParams = arSurface.getLayoutParams();
            arParams.height = (int)screenHeight;

            arSurface.setLayoutParams(arParams);

            FrameLayout content = (FrameLayout)findViewById(R.id.arSurface);

            renderer = new GLClearRenderer();

            // GL 서피스뷰 생성
            glSurfaceView = new GLSurfaceView(this);
            glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);

            // set format as translucent
            glSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
            glSurfaceView.setRenderer(renderer);


            renderer.setAngleX(-70f);

            content.addView(glSurfaceView);
            glSurfaceView.setZOrderMediaOverlay(true);


        }catch (Exception e){
            Log.d("ARERROR :: ",e.getMessage());
        }
    }


    @Override
    public void onBackPressed(){

        //uNavigationActivity.
        finish();
    }

}