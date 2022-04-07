package com.benny.watertank;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private final NotificationBroadcast notificationBroadcast = new NotificationBroadcast();

    private RecyclerView recyclerView;

    private final APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_WaterTank);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.historyList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        Resources.Theme theme = getTheme();
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(R.attr.colorOnSecondary, typedValue, true);
        dividerItemDecoration.setDrawable(new ColorDrawable(typedValue.data));
        recyclerView.addItemDecoration(dividerItemDecoration);

        WaterTankHistoryAdapter waterTankHistoryAdapter = new WaterTankHistoryAdapter(getApplicationContext());
        recyclerView.setAdapter(waterTankHistoryAdapter);

        createNotificationChannel();
        subscribe();
        fetchWaterTankData();

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(notificationBroadcast, new IntentFilter(NotificationBroadcast.ACTION));

        //FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> Log.d(TAG, "onSuccess: " + instanceIdResult.getToken()));
    }

    public void fetchWaterTankData() {
        apiInterface.getLatestWaterLevel().enqueue(new Callback<WaterTankResponse>() {
            @Override
            public void onResponse(Call<WaterTankResponse> call, Response<WaterTankResponse> response) {
                if (response.isSuccessful()) {
                    WaterTankResponse body = response.body();
                    ArrayList<WaterTank> waterTanks = body.waterTanks;
                    waterTanks.sort((waterTank1, waterTank2) -> Long.compare(Long.parseLong(waterTank2.timeStamp), Long.parseLong(waterTank1.timeStamp)));
                    WaterTankHistoryAdapter waterTankHistoryAdapter = (WaterTankHistoryAdapter) recyclerView.getAdapter();
                    if (waterTankHistoryAdapter != null) {
                        waterTankHistoryAdapter.submitList(waterTanks);
                    }
                }
            }

            @Override
            public void onFailure(Call<WaterTankResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(notificationBroadcast);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        CharSequence name = "FirebaseNotification";
        String description = "Firebase Notification Description";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tamacun);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();

        NotificationChannel channel = new NotificationChannel("water_tank_id", name, importance);
        channel.setDescription(description);
        channel.setSound(sound, audioAttributes);
        channel.setVibrationPattern(new long[]{400, 400});
        channel.enableVibration(true);
        channel.enableLights(true);

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

    public class NotificationBroadcast extends BroadcastReceiver {
        public static final String ACTION = "MESSAGE_RECEIVED";

        @Override
        public void onReceive(Context context, Intent intent) {
            fetchWaterTankData();
        }
    }

}