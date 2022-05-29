package com.benny.waterTank2.network.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class WaterTankResponse {

    @SerializedName("water_tank_status")
    public ArrayList<WaterTankStatus> waterTankStatusList;

    @SerializedName("success")
    public int success;

    public ArrayList<WaterTankStatus> getWaterTankStatusList() {
        return waterTankStatusList;
    }

    @Override
    public String toString() {
        return "WaterTankResponse{" +
                "water_tank=" + waterTankStatusList +
                ", success=" + success +
                '}';
    }
}
