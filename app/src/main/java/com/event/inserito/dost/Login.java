package com.event.inserito.dost;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Response;


public class Login extends AppCompatActivity implements View.OnClickListener {

    public static String fileName = "user_cred";
    private SharedPreferences user_cred;
    private String phoneNo;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Persistent Login Credentials
        user_cred = getSharedPreferences(fileName, 0);
        phoneNo = user_cred.getString("phoneNo","");
        password = user_cred.getString("password", "");
        if(!phoneNo.isEmpty() && !password.isEmpty()) {
            Intent intent = new Intent(Login.this,ProfileActivity.class);
            finish();
            startActivity(intent);
        }
        setContentView(R.layout.activity_login);

        //Onclick Listener For Submit Button
        Button button = (Button)findViewById(R.id.submit);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final Context context = getApplicationContext();

        Toast toast = Toast.makeText(context, "Logging in..Please Wait", Toast.LENGTH_SHORT);
        toast.show();

        phoneNo = ((EditText) findViewById(R.id.phoneNo)).getText().toString();
        password = ((EditText) findViewById(R.id.password)).getText().toString();

        // REST API CALL for Verification of Credentials
        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(getString(R.string.server_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        VerifyCredentials verifyCredentialsModel = restAdapter.create(VerifyCredentials.class);
        Call<InseritoUser> call = verifyCredentialsModel.postData(phoneNo,password);

        call.enqueue(new Callback<InseritoUser>() {
            @Override
            public void onResponse(Call<InseritoUser> call, Response<InseritoUser> response) {
                try {
                    if (response.body().resp) {
                        //Saving Credentials in file for persistent login
                        SharedPreferences.Editor editor = user_cred.edit();
                        editor.putString("phoneNo", phoneNo);
                        editor.putString("password", password);
                        editor.putString("username",response.body().userName);
                        editor.commit();
                        Intent intent = new Intent(Login.this,ProfileActivity.class);
                        finish();
                        startActivity(intent);
                    } else {
                        Toast toast = Toast.makeText(context, "Invalid Number", Toast.LENGTH_LONG);
                        toast.show();
                    }
                } catch (Exception e) {
                    Toast toast = Toast.makeText(context, "Error Occured", Toast.LENGTH_LONG);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<InseritoUser> call, Throwable t) {
                Toast toast = Toast.makeText(context, "Error Occured", Toast.LENGTH_LONG);
                toast.show();
            }
        });

}
}
