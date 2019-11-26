package com.hanium.glass;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.graphics.PixelFormat;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.hanium.glass.Augmented.GLClearRenderer;
import com.hanium.glass.Utils.ScreenUtils;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.Timer;
import java.util.TimerTask;

import static android.view.MotionEvent.ACTION_DOWN;

import static android.view.MotionEvent.ACTION_MOVE;

import static android.view.MotionEvent.ACTION_UP;

public class TranslationActivity extends AppCompatActivity {
    static public Context tContext;
    private FrameLayout arSurface;


    // OpenGL
    private GLSurfaceView glSurfaceView;
    public GLClearRenderer renderer;

    //Mnager Components
    private  GpsManager gpsManager;

    private static CameraPreview surfaceView;
    private SurfaceHolder holder;
    private static Camera mCamera;
    private int RESULT_PERMISSIONS = 100;
    public static TranslationActivity getInstance;

    Switch arSwitch;
    String direction;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation);
        tContext = this;
        // 카메라 프리뷰를 전체화면으로 보여주기 위해 셋팅한다.
        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //           WindowManager.LayoutParams.FLAG_FULLSCREEN);

        arSwitch = (Switch) findViewById(R.id.ARSwitch2);

        //토글버튼 클릭
        arSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true){ // AR mode

                } else { // tMap mode
                   // finish();
                    Intent intent=new Intent(TranslationActivity.this, NavigationActivity.class);
                    startActivity(intent);
                }
            }
        });

        try {
            arSurface = (FrameLayout) findViewById(R.id.arSurface);
            arSurface.setVisibility(View.VISIBLE);
            //안드로이드 6.0 이상 버전에서는 CAMERA 권한 허가를 요청한다.
            requestPermissionCamera();
            // init GPS Manager


        }
        catch (Exception e){
            Log.d("Exception", "cant initilaize" + e.getMessage());
        }
    }

    private void moveToCurrentLocation() {
        try {
            Location currentLocation = gpsManager.getCurrentLocation();

            Log.d("LOCATION", currentLocation.getLongitude()+ " ," +currentLocation.getLatitude());
            //    mapView.setLocationPoint(currentLocation.getLongitude(), currentLocation.getLatitude());
            //  mapView.setCenterPoint(currentLocation.getLongitude(), currentLocation.getLatitude());
        } catch (Exception ex) {
            Log.d("Exception :: ", "cant find location"+ex.getMessage());
        }
    }

    public static Camera getCamera(){
        return mCamera;
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
            Log.d("Exception :: ",e.getMessage());
        }
    }

    private void updateDirection(){
        //renderer.se
    }
    public boolean requestPermissionCamera(){
        int sdkVersion = Build.VERSION.SDK_INT;
        if(sdkVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(TranslationActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        RESULT_PERMISSIONS);
            }else {
                startAR();
            }
        }else{  // version 6 이하일때
            startAR();
            return true;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if (RESULT_PERMISSIONS == requestCode) {

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한 허가시
                startAR();
            } else {
                // 권한 거부시
            }
            return;
        }

    }



}
