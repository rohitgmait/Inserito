package com.event.inserito.dost;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ViewCrop extends AppCompatActivity {

    private static MediaPlayer mplayer;

    public static MediaPlayer getMplayer() {
        return mplayer;
    }

    public static void setMplayer(MediaPlayer mplayer) {
        ViewCrop.mplayer = mplayer;
    }

    private static SharedPreferences user_cred;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(mplayer != null && mplayer.isPlaying()) {
            mplayer.stop();
        }
        user_cred = getSharedPreferences(Login.fileName, 0);
        String cropJSON = user_cred.getString("crop", "");
        Context context = getApplicationContext();
        //Required Attributes for Viewing Crop
        if(cropJSON.isEmpty()) {
            Toast toast = Toast.makeText(context, "No Crop Configured", Toast.LENGTH_LONG);
            toast.show();
            Intent intent = new Intent(ViewCrop.this,AddCrop.class);
            finish();
            startActivity(intent);
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = new GsonBuilder().create();
        final Crop crop = gson.fromJson(cropJSON, Crop.class);
        setContentView(R.layout.activity_view_crop);
        TextView cropName = (TextView) findViewById(R.id.cropName);
        cropName.setText(crop.name);
        Button setUpButton = (Button) findViewById(R.id.setUp);
        setUpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ViewCrop.this,SensorConfig.class);
                intent.putExtra("cropid",crop.id);
                finish();
                startActivity(intent);
            }
        });

    }
}
