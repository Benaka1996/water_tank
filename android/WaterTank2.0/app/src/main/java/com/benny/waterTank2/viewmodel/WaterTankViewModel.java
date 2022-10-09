package com.benny.waterTank2.viewmodel;

import android.app.Application;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.benny.waterTank2.network.APIClient;
import com.benny.waterTank2.network.APIInterface;
import com.benny.waterTank2.network.response.WaterTankResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WaterTankViewModel extends AndroidViewModel {
    private static final String TAG = "WaterTankViewModel";

    private final APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    private final Handler handler = new Handler();
    private final MutableLiveData<WaterTankResponse> waterTankStatusMutableLiveData = new MutableLiveData<>();

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            apiInterface.getLatestWaterLevel().enqueue(new Callback<WaterTankResponse>() {
                @Override
                public void onResponse(Call<WaterTankResponse> call, Response<WaterTankResponse> response) {
                    if (response.isSuccessful()) {
                        WaterTankResponse waterTankResponse = response.body();
                        waterTankStatusMutableLiveData.setValue(waterTankResponse);
                        Log.d(TAG, "onResponse: " + waterTankResponse.toString());
                    } else {
                        Toast.makeText(getApplication().getApplicationContext(), "Connection failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<WaterTankResponse> call, Throwable t) {
                    Toast.makeText(getApplication().getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
            handler.postDelayed(this, 2500);
        }
    };

    public WaterTankViewModel(@NonNull Application application) {
        super(application);
        fetchWaterTankData();
    }

    private void fetchWaterTankData() {
        handler.postDelayed(runnable, 5000);
    }

    public MutableLiveData<WaterTankResponse> getWaterTankStatusLiveData() {
        return waterTankStatusMutableLiveData;
    }

    @Override
    protected void onCleared() {
        handler.removeCallbacks(runnable);
        super.onCleared();
    }
}
