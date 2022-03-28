package com.benny.watertank;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class WaterTankResponse {

    @SerializedName("water_tank")
    public ArrayList<WaterTank> waterTanks;

    @SerializedName("success")
    public int success;

    @Override
    public String toString() {
        return "WaterTankResponse{" +
                "water_tank=" + waterTanks +
                ", success=" + success +
                '}';
    }
}
