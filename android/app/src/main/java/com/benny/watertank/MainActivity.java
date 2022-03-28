package com.benny.watertank;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private final Handler handler = new Handler();
    private TextView sensarValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensarValue = findViewById(R.id.sensor_value);

        createNotificationChannel();
        subscribe();

        handler.post(new Runnable() {
            @Override
            public void run() {
                APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
                apiInterface.getLatestWaterLevel().enqueue(new Callback<WaterTankResponse>() {
                    @Override
                    public void onResponse(Call<WaterTankResponse> call, Response<WaterTankResponse> response) {
                        if (response.isSuccessful()) {
                            WaterTankResponse body = response.body();
                            ArrayList<WaterTank> waterTanks = body.waterTanks;
                            if (!waterTanks.isEmpty()) {
                                WaterTank waterTank = waterTanks.get(0);
                                sensarValue.setText(waterTank.waterLevel);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<WaterTankResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + call);
                    }
                });
                handler.removeCallbacks(this);
                handler.postDelayed(this, 5000);
            }
        });

        //FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> Log.d(TAG, "onSuccess: " + instanceIdResult.getToken()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        CharSequence name = "FirebaseNotification";
        String description = "Firebase Notification Description";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("101", name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    private void subscribe() {
        FirebaseMessaging.getInstance()
                .subscribeToTopic("water_tank")
                .addOnCompleteListener(task -> {

                });

    }

}