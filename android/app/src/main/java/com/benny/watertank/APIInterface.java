package com.benny.watertank;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {

    @GET("/water_tank/readAll.php")
    Call<WaterTankResponse> getLatestWaterLevel();

}
