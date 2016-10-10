package com.event.inserito.dost;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by rgupt on 01-10-2016.
 */
public interface WaterAlert {
        @FormUrlEncoded
        @POST("/waterAlert.php")
        Call<WaterAlertModel> postData(@Field("cropid") long cropId);

        @FormUrlEncoded
        @POST("/waterAlertRegister.php")
        Call<InseritoResponse> postData(@Field("cropid") long cropId,
                                       @Field("height") String height);

}
