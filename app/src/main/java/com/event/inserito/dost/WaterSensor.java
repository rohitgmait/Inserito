package com.event.inserito.dost;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WaterSensor extends AppCompatActivity {

    private static AlarmManager alarm;
    private static Intent intent;
    private static PendingIntent pintent;
    private static String TAG = "WaterSensor";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_sensor);
        final Context context = getApplicationContext();
        intent = getIntent();
        final long cropId = intent.getLongExtra("cropid", 0);
        //Checking Whether Crop Exists Or Not
        if(cropId > 0 ) {
            Button button = (Button)findViewById(R.id.submit);
            button.setOnClickListener(new View.OnClickListener() {
                //Submit Button Listener
                public void onClick(View v) {
                    EditText height = (EditText)findViewById(R.id.height);
                    Retrofit restAdapter = new Retrofit.Builder()
                            .baseUrl(getString(R.string.server_URL))
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    WaterAlert waterAlertApi = restAdapter.create(WaterAlert.class);
                    Call<InseritoResponse> call = waterAlertApi.postData(cropId,height.getText().toString());
                    call.enqueue(new Callback<InseritoResponse>() {
                        @Override
                        public void onResponse(Call<InseritoResponse>call, Response<InseritoResponse> response) {
                            try {
                                if(response.body().resp) {
                                    //Setting Alarm Service For Regular Updates On Whether Water is Above Level Or Not
                                    setAlarm(context);
                                    Toast toast = Toast.makeText(context, "Water Sensor Configured Successfully", Toast.LENGTH_LONG);
                                    toast.show();
                                    Intent intent = new Intent(WaterSensor.this,ViewCrop.class);
                                    finish();
                                    startActivity(intent);
                                } else {
                                    Toast toast = Toast.makeText(context, "Error occured at Server While Configuring Water Sensor", Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            } catch (Exception e) {
                                Toast toast = Toast.makeText(context, "Error occured at application and server While Configuring Water Sensor", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }
                        @Override
                        public void onFailure(Call<InseritoResponse>call, Throwable t) {
                            Toast toast = Toast.makeText(context, "Error occured at application While Configuring Water Sensor", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });

                }
            });
        } else {
            Toast toast = Toast.makeText(context, "No Crop Configured", Toast.LENGTH_LONG);
            toast.show();
            Intent intent = new Intent(WaterSensor.this,ViewCrop.class);
            finish();
            startActivity(intent);
        }
    }
    public static void setAlarm(Context context) {
        alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        intent = new Intent(context, WaterAlarmBroadcastReceiver.class);
        pintent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if(null != alarm && null != pintent) {
            alarm.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, 180000, pintent);
            Log.d(TAG,"Alarm Set");
        }
    }
    public void cancelAlarm(Context context){
        if(null != alarm && null != pintent){
            alarm.cancel(pintent);
        }
    }
}
