package com.hanium.glass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

public class TestActivity extends AppCompatActivity {

    Switch arSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        arSwitch = (Switch) findViewById(R.id.ARSwitch2);

        //토글버튼 클릭
        arSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true){ // AR mode

                } else { // tMap mode
                    finish();
                    Intent intent=new Intent(TestActivity.this, NavigationActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}