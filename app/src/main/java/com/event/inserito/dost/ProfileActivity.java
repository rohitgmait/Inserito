package com.event.inserito.dost;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final SharedPreferences user_cred = getSharedPreferences(Login.fileName, 0);
        final Context context = getApplicationContext();
        String userName = user_cred.getString("username"," ");
        if(userName.isEmpty()) {
            Toast toast = Toast.makeText(context, "Unauthorised...", Toast.LENGTH_LONG);
            toast.show();
            Intent intent = new Intent(ProfileActivity.this,Login.class);
            finish();
            startActivity(intent);
        }
        TextView profilename = (TextView) findViewById(R.id.username);
        profilename.setText(userName);
        Button addCropButton = (Button) findViewById(R.id.addCrop);
        addCropButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, AddCrop.class);
                finish();
                startActivity(intent);
            }
        });
        Button viewCropButton = (Button) findViewById(R.id.viewCrop);
        viewCropButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,ViewCrop.class);
                finish();
                startActivity(intent);
            }
        });
    }
}
