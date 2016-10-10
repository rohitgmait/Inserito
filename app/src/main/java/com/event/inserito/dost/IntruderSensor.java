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
import android.widget.TimePicker;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IntruderSensor extends AppCompatActivity {

    private static AlarmManager alarm;
    private static Intent intent;
    private static PendingIntent pintent;
    private static String TAG = "IntruderSensor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intruder_alert);
        intent = getIntent();
        final long cropId = intent.getLongExtra("cropid", 0);
        Button submitButton = (Button) findViewById(R.id.submit);
        final Context context = getApplicationContext();

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TimePicker startTimePicker = (TimePicker) findViewById(R.id.startTime);
                TimePicker endTimePicker = (TimePicker) findViewById(R.id.endTime);
                long startTime = startTimePicker.getHour()*3600+startTimePicker.getMinute()*60;
                long endTime = endTimePicker.getHour()*3600+endTimePicker.getMinute()*60;
                if(endTime>startTime) {
                    Retrofit restAdapter = new Retrofit.Builder()
                            .baseUrl(getString(R.string.server_URL))
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    IntruderAlert intruderAlert = restAdapter.create(IntruderAlert.class);
                    Call<InseritoResponse> call = intruderAlert.postData(cropId, startTime, endTime);
                    call.enqueue(new Callback<InseritoResponse>() {
                        @Override
                        public void onResponse(Call<InseritoResponse>call, Response<InseritoResponse> response) {
                            try {
                                if(response.body().resp) {
                                    //Setting Alarm Service For Regular Updates On Whether Intruder is there or not
                                    setAlarm(context);
                                    Toast toast = Toast.makeText(context, "Intruder Sensor Configured Successfully", Toast.LENGTH_LONG);
                                    toast.show();
                                    Intent intent = new Intent(IntruderSensor.this,ViewCrop.class);
                                    finish();
                                    startActivity(intent);
                                } else {
                                    Toast toast = Toast.makeText(context, "Error occured at Server While Configuring Intruder Sensor", Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            } catch (Exception e) {
                                Toast toast = Toast.makeText(context, "Error occured at application and server While Configuring Intruder Sensor", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }
                        @Override
                        public void onFailure(Call<InseritoResponse>call, Throwable t) {
                            Toast toast = Toast.makeText(context, "Error occured at application While Configuring Intruder Sensor", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });

                } else {
                    Toast toast = Toast.makeText(context, "End Time should be greater than Start Time", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }
    public static void setAlarm(Context context) {
        alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        intent = new Intent(context, IntruderAlarmBroadcastReceiver.class);
        pintent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if(null != alarm && null != pintent) {
            alarm.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, 180000, pintent);
            Log.d(TAG, "Alarm Set");
        }
    }
    public void cancelAlarm(Context context){
        if(null != alarm && null != pintent){
            alarm.cancel(pintent);
        }
    }
}
