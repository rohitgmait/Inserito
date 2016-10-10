package com.event.inserito.dost;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by rgupt on 09-10-2016.
 */
public interface IntruderAlert {

    @FormUrlEncoded
    @POST("/intruderAlert.php")
    Call<IntruderAlertModel> postData(@Field("cropid") long cropId);

    @FormUrlEncoded
    @POST("/intruderAlertRegister.php")
    Call<InseritoResponse> postData(@Field("cropid") long cropId,
                                    @Field("starttime") long startTime,
                                    @Field("endtime") long endTime);
}
