package com.benny.watertank;

import com.google.gson.annotations.SerializedName;

public class WaterTank {

    @SerializedName("id")
    public String id;

    @SerializedName("water_level")
    public String waterLevel;

    @SerializedName("time_stamp")
    public String timeStamp;

    @Override
    public String toString() {
        return "WaterTank{" +
                "id='" + id + '\'' +
                ", water_level='" + waterLevel + '\'' +
                ", time_stamp='" + timeStamp + '\'' +
                '}';
    }
}
