package com.event.inserito.dost;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SensorConfig extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_config);
        Intent intent = getIntent();
        final long cropId = intent.getLongExtra("cropid",0);
        //Required Attributes for Viewing Crop
        if(cropId == 0) {
            intent = new Intent(SensorConfig.this,ViewCrop.class);
            finish();
            startActivity(intent);
        }
        Button waterSensorButton = (Button) findViewById(R.id.waterSensor);
        waterSensorButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SensorConfig.this,WaterSensor.class);
                intent.putExtra("cropid",cropId);
                finish();
                startActivity(intent);
            }
        });
        Button alienSensorButton = (Button) findViewById(R.id.alienSensor);
        alienSensorButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SensorConfig.this,IntruderSensor.class);
                intent.putExtra("cropid",cropId);
                finish();
                startActivity(intent);
            }
        });
    }
}
