package com.benny.waterTank2.network.response;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class WaterTankStatus {
    @SerializedName("id")
    private int id;
    @SerializedName("type")
    private String statusType;
    @SerializedName("value")
    private double waterLevel;
    @SerializedName("time_stamp")
    private long timeStamp;

    public double getWaterLevel() {
        return waterLevel;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getStatusType() {
        return statusType;
    }

    @NonNull
    @Override
    public String toString() {
        return "WaterTankStatus{" +
                "id=" + id +
                ", statusType='" + statusType + '\'' +
                ", waterLevel=" + waterLevel +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
