package com.event.inserito.dost;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by rgupt on 08-10-2016.
 */
public interface AddCropDB {
    @FormUrlEncoded
    @POST("/addCrop.php")
    Call<AddCropResponse> postData(@Field("phoneno") String phoneno,@Field("crop") String crop,@Field("month") String month,@Field("day") String day);

    //This method is used for "GET"
    @GET("/addCrop.php")
    void getData(@Query("phoneno") String username);
}
