package com.benny.waterTank2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.benny.waterTank2.network.response.WaterTankResponse;
import com.benny.waterTank2.network.response.WaterTankStatus;
import com.benny.waterTank2.viewmodel.WaterTankViewModel;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import waterTank2.R;

public class MainFragment extends Fragment {
    public static final String TAG = "WaterTank";
    private static final int TANK_HEIGHT = 68;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a", Locale.getDefault());
    private final Gson gson = new Gson();
    private final Observer<WaterTankResponse> waterTankDataObserver = new Observer<WaterTankResponse>() {
        @Override
        public void onChanged(WaterTankResponse waterTankResponse) {
            SharedPreferences.Editor editor = waterTankPreference.edit();
            editor.putString(Constants.WATER_TANK_RESPONSE, gson.toJson(waterTankResponse));
            editor.apply();
            setWaterTankInfo(waterTankResponse);
        }
    };

    private void setWaterTankInfo(WaterTankResponse waterTankResponse) {
        if (waterTankResponse != null) {
            int tankContainerHeight = tankContainer.getMeasuredHeight() - 56;
            ArrayList<WaterTankStatus> waterTankStatusList = waterTankResponse.getWaterTankStatusList();
            if (waterTankStatusList != null && !waterTankStatusList.isEmpty()) {
                WaterTankStatus waterTankStatus = waterTankStatusList.get(0);

                double waterLevel = waterTankStatus.getWaterLevel() - 4.5;
                double waterPercentage = (1 - (waterLevel / TANK_HEIGHT)) * 100;
                double fillHeight = waterPercentage * tankContainerHeight / 100;

                ViewGroup.LayoutParams waterLayoutParams = water.getLayoutParams();
                waterLayoutParams.height = (int) fillHeight;
                water.setLayoutParams(waterLayoutParams);

                waterLevelPercentage.setText(String.format(Locale.getDefault(), "Water Level : %.2f %%", waterPercentage));
                timeStamp.setText(String.format("Read time : %s", simpleDateFormat.format(waterTankStatus.getTimeStamp())));
                sensorReading.setText(String.format("Sensor value : %s cm", waterLevel));
            }
        }
    }

    private WaterTankViewModel waterTankViewModel;
    private View tankContainer;
    private View water;
    private TextView waterLevelPercentage;
    private TextView timeStamp;
    private SharedPreferences waterTankPreference;
    private TextView sensorReading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        waterTankPreference = requireContext().getSharedPreferences(Constants.WATER_TANK_PREF, Context.MODE_PRIVATE);
        waterTankViewModel = new ViewModelProvider(this).get(WaterTankViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tankContainer = view.findViewById(R.id.tank_container);
        waterLevelPercentage = view.findViewById(R.id.water_level_percentage);
        timeStamp = view.findViewById(R.id.read_time_text);
        water = view.findViewById(R.id.water);
        sensorReading = view.findViewById(R.id.sensor_reading);

        String responseString = waterTankPreference.getString(Constants.WATER_TANK_RESPONSE, null);
        WaterTankResponse waterTankResponse = gson.fromJson(responseString, WaterTankResponse.class);
        setWaterTankInfo(waterTankResponse);

        waterTankViewModel.getWaterTankStatusLiveData().observe(getViewLifecycleOwner(), waterTankDataObserver);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        waterTankViewModel.getWaterTankStatusLiveData().removeObserver(waterTankDataObserver);
    }
}