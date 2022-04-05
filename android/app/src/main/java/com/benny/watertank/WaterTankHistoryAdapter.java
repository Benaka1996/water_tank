package com.benny.watertank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class WaterTankHistoryAdapter extends ListAdapter<WaterTank, WaterTankHistoryAdapter.WaterTankHistoryViewHolder> {

    private static final DiffUtil.ItemCallback<WaterTank> DIFF_ITEM_CALLBACK = new DiffUtil.ItemCallback<WaterTank>() {
        @Override
        public boolean areItemsTheSame(@NonNull WaterTank oldWaterTank, @NonNull WaterTank newWaterTank) {
            return oldWaterTank.id.equals(newWaterTank.id);
        }

        @Override
        public boolean areContentsTheSame(@NonNull WaterTank oldWaterTank, @NonNull WaterTank newWaterTank) {
            return Objects.equals(oldWaterTank, newWaterTank);
        }
    };
    private final LayoutInflater layoutInflater;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a", Locale.getDefault());

    protected WaterTankHistoryAdapter(Context context) {
        super(DIFF_ITEM_CALLBACK);
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public WaterTankHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.layout_water_tank_history_adapter, parent, false);
        return new WaterTankHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WaterTankHistoryViewHolder waterTankHistoryViewHolder, int position) {
        WaterTank waterTank = getItem(position);
        waterTankHistoryViewHolder.getIndex().setText(String.format(Locale.getDefault(), "%d", (position + 1)));
        waterTankHistoryViewHolder.getSensorValue().setText(String.format("Sensor Value : %s", waterTank.waterLevel));
        long timeStamp = Long.parseLong(waterTank.timeStamp);
        String timeDate = simpleDateFormat.format(new Date(timeStamp));
        waterTankHistoryViewHolder.getFillTime().setText(String.format("Fill time : %s", timeDate));
    }

    protected static class WaterTankHistoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView index;
        private final TextView sensorValue;
        private final TextView fillTime;

        public WaterTankHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            index = itemView.findViewById(R.id.water_tank_index);
            sensorValue = itemView.findViewById(R.id.sensor_value);
            fillTime = itemView.findViewById(R.id.fill_time);
        }

        public TextView getIndex() {
            return index;
        }

        public TextView getSensorValue() {
            return sensorValue;
        }

        public TextView getFillTime() {
            return fillTime;
        }
    }
}
