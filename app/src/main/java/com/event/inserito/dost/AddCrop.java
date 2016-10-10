package com.event.inserito.dost;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddCrop extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_crop);
        Context context = getApplicationContext();

        SharedPreferences user_cred = getSharedPreferences(Login.fileName, 0);
        String cropJSON = user_cred.getString("crop", "");
        if(!cropJSON.isEmpty()) {
            Toast toast = Toast.makeText(context, "Crop Already Configured", Toast.LENGTH_LONG);
            toast.show();
            Intent intent = new Intent(AddCrop.this,ViewCrop.class);
            finish();
            startActivity(intent);
        }

        //Setting Values for Days and Months Spinner
        List<String> days = new ArrayList<String>();
        for(int i=1;i<=31;i++) {
            days.add(String.valueOf(i));
        }
        List<String> months = new ArrayList<String>();
        for(int i=1;i<=6;i++) {
            months.add(String.valueOf(i));
        }

        //Setting Spinner for Crop,Month and Days
        Spinner crop = (Spinner) findViewById(R.id.crop);
        Spinner month = (Spinner) findViewById(R.id.month);
        Spinner day = (Spinner) findViewById(R.id.day);
        ArrayAdapter<CharSequence> cropAdapter = ArrayAdapter.createFromResource(this,
                R.array.crop_array, R.layout.inserito_spinner_item);
        ArrayAdapter<String> daysAdapter = new ArrayAdapter<String>(this,R.layout.inserito_spinner_item,days);
        ArrayAdapter<String> monthsAdapter = new ArrayAdapter<String>(this,R.layout.inserito_spinner_item,months);
        cropAdapter.setDropDownViewResource(R.layout.inserito_spinner_dropdown_item);
        daysAdapter.setDropDownViewResource(R.layout.inserito_spinner_dropdown_item);
        monthsAdapter.setDropDownViewResource(R.layout.inserito_spinner_dropdown_item);
        crop.setAdapter(cropAdapter);
        month.setAdapter(monthsAdapter);
        day.setAdapter(daysAdapter);

        //Adding Listener on Submit Button
        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        final Context context = getApplicationContext();

        final SharedPreferences user_cred = getSharedPreferences(Login.fileName, 0);
        String phoneNo = user_cred.getString("phoneNo","");

        //Empty Credentials will force user to login Again
        if(phoneNo.isEmpty()) {
            Intent intent = new Intent(AddCrop.this,Login.class);
            finish();
            startActivity(intent);
        }

        //Getting the value of selected menu item
        Spinner spinner = (Spinner)findViewById(R.id.crop);
        final String cropname = spinner.getSelectedItem().toString();
        spinner = (Spinner)findViewById(R.id.month);
        final String month = spinner.getSelectedItem().toString();
        spinner = (Spinner)findViewById(R.id.day);
        final String day = spinner.getSelectedItem().toString();

        //Checking the value of spinner if it is empty
        if(!cropname.isEmpty()&&!month.isEmpty()&&!day.isEmpty()) {
            Retrofit restAdapter = new Retrofit.Builder()
                    .baseUrl(getString(R.string.server_URL))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            AddCropDB addCropModel = restAdapter.create(AddCropDB.class);
            Call<AddCropResponse> call = addCropModel.postData(phoneNo, cropname, month, day);
            call.enqueue(new Callback<AddCropResponse>() {
                @Override
                public void onResponse(Call<AddCropResponse> call, Response<AddCropResponse> response) {
                    try {
                        if (response.body().resp) {
                            //Saving Details of Crop in a User Preferences File
                            SharedPreferences.Editor editor = user_cred.edit();
                            Crop crop = new Crop(response.body().id,cropname,month,day);
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = new GsonBuilder().create();
                            editor.putString("crop",gson.toJson(crop));
                            editor.commit();
                            Toast toast = Toast.makeText(context, "Crop Added Successfully", Toast.LENGTH_LONG);
                            Intent intent = new Intent(AddCrop.this,ViewCrop.class);
                            finish();
                            startActivity(intent);
                        } else {
                            Toast toast = Toast.makeText(context, "Error Occured At Server", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    } catch (Exception e) {
                        Toast toast = Toast.makeText(context, "Error Occured At Application, Try Again", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }

                @Override
                public void onFailure(Call<AddCropResponse> call, Throwable t) {
                    Toast toast = Toast.makeText(context, "Error Occured At Application, Try Again", Toast.LENGTH_LONG);
                    toast.show();
                }
            });
        } else {
            Toast toast = Toast.makeText(context, "Please Select All the fields", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
