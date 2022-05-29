package com.benny.waterTank2.network;

import com.benny.waterTank2.Constants;
import com.benny.waterTank2.network.response.WaterTankResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {

    @GET(Constants.WATER_LEVEL_API)
    Call<WaterTankResponse> getLatestWaterLevel();

}
