package com.event.inserito.dost;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by rgupt on 25-09-2016.
 */
public class WaterSensorService extends IntentService {
    private static String TAG = "WaterSensorService";
    public WaterSensorService() {
        super("WaterSensorService");
    }
    private void sendNotification(String msg) {
        try {
            NotificationManager mNotificationManager = (NotificationManager)
                    this.getSystemService(Context.NOTIFICATION_SERVICE);

            Intent intent = new Intent(this, ViewCrop.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                    intent, 0);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_action_name)
                            .setContentTitle("Water Level Alert!!!!")
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(msg))
                            .setContentText(msg)
                            .setPriority(2);
            if(!(ViewCrop.getMplayer() != null && ViewCrop.getMplayer().isPlaying())) {
                MediaPlayer mPlayer = new MediaPlayer();
                mPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mPlayer.setDataSource(getApplicationContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
                mPlayer.prepare();
                mPlayer.start();
                ViewCrop.setMplayer(mPlayer);
            }

            mBuilder.setContentIntent(contentIntent);
            mBuilder.setAutoCancel(true);
            mNotificationManager.notify(123, mBuilder.build());
        } catch (Exception e) {

        }
    }
    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        // Normally we would do some work here, like download a file.
        // For our sample, we just sleep for 5 seconds.
        try {
            SharedPreferences user_cred = getSharedPreferences(Login.fileName, 0);
            String cropJSON = user_cred.getString("crop", "");
            if(!cropJSON.isEmpty()) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = new GsonBuilder().create();
                final Crop crop = gson.fromJson(cropJSON, Crop.class);
                Retrofit restAdapter = new Retrofit.Builder()
                        .baseUrl(getString(R.string.server_URL))
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                WaterAlert waterAlertApi = restAdapter.create(WaterAlert.class);
                Call<WaterAlertModel> call = waterAlertApi.postData(crop.id);
                call.enqueue(new retrofit2.Callback<WaterAlertModel>() {
                    @Override
                    public void onResponse(Call<WaterAlertModel>call, Response<WaterAlertModel> response) {
                        try {
                            if(response.body().result.contains("Success")) {
                                if(response.body().alert == 1) {
                                    sendNotification("Water Level High : " + response.body().height + " inches");
                                }
                            } else {

                            }
                        } catch (Exception e) {
                        }
                    }
                    @Override
                    public void onFailure(Call<WaterAlertModel>call, Throwable t) {
                    }
                });
                WaterAlarmBroadcastReceiver.completeWakefulIntent(intent);
            } else {
                Log.d(TAG, "No Crop Configured Still Service is Running");
                WaterAlarmBroadcastReceiver.completeWakefulIntent(intent);
            }


        } catch (Exception e) {
            // Restore interrupt status.
            Thread.currentThread().interrupt();
        }
    }

}
