package com.a777.mu.sensordatasender.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.a777.mu.sensordatasender.R;
import com.a777.mu.sensordatasender.contract.WebSocketContract;
import com.a777.mu.sensordatasender.model.SensorEventService;

import java.util.List;

/**
 * Created by root on 16/10/05.
 */

public class SensorListAdapter extends RecyclerView.Adapter<SensorListAdapter.SensorProfileHolder> {


    private final Context context;
    private final OnSensorItemClickListener onSensorItemClickListener;
    private List<SensorEventService.SensorData> items;

    public SensorListAdapter(Context context, OnSensorItemClickListener onSensorItemClickListener) {
        this.context = context;
        this.onSensorItemClickListener = onSensorItemClickListener;
    }

    public void updateSensorData(List<SensorEventService.SensorData> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public SensorEventService.SensorData getSensorDataAt(int position) {
        return this.items.get(position);
    }

    @Override
    public SensorProfileHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.sensor_item, parent, false);
        return new SensorProfileHolder(view);
    }

    @Override
//   When notifyDataSetChanged() is called, this is called in each position, which is from getItemCount()
    public void onBindViewHolder(SensorProfileHolder holder, int position) {
        final SensorEventService.SensorData item = getSensorDataAt(position);
        holder.sensorName.setText(item.name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSensorItemClickListener.onSensorItemClick(item);
            }
        });
        holder.activeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onSensorItemClickListener.onSensorItemSwitchClick(item, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }


    interface OnSensorItemClickListener {
        void onSensorItemClick(SensorEventService.SensorData data);

        void onSensorItemSwitchClick(SensorEventService.SensorData data, boolean isChecked);
    }

    public static class SensorProfileHolder extends RecyclerView.ViewHolder {
        private final TextView sensorName;
        private final Switch activeSwitch;

        public SensorProfileHolder(View itemView) {
            super(itemView);
            sensorName = (TextView) itemView.findViewById(R.id.txt_sensor_name);
            activeSwitch = (Switch) itemView.findViewById(R.id.btn_sensor);
        }
    }
}
