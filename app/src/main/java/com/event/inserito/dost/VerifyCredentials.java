package com.event.inserito.dost;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by rgupt on 18-09-2016.
 */
public interface VerifyCredentials {
    @FormUrlEncoded
    @POST("/verify.php")
    Call<InseritoUser> postData(@Field("phoneno") String phoneno,@Field("password") String password);

    //This method is used for "GET"
    @GET("/verify.php")
    void getData(@Query("phoneno") String username);
}
