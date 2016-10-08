package com.a777.mu.sensordatasender.presenter;

import android.hardware.SensorManager;
import android.util.Log;

import com.a777.mu.sensordatasender.contract.SensorListContract;
import com.a777.mu.sensordatasender.model.SensorEventService;
import com.a777.mu.sensordatasender.model.SensorEventServiceListener;

import java.util.List;

/**
 * Created by root on 16/10/05.
 */
public class SensorListPresenter implements SensorListContract.UserActions, SensorEventServiceListener {

    private static final String TAG = "SensorListPresenter";

    private SensorListContract.View sensorListView;
    private SensorEventService sensorService;

    public SensorListPresenter(SensorListContract.View sensorListView, SensorManager sensorManager) {
        this.sensorService = SensorEventService.getInstance(sensorManager);
        this.sensorService.addListener(this);
        this.sensorListView = sensorListView;
        this.sensorListView.updateSensorList(this.sensorService.items());
    }

    @Override
    public void enable(SensorEventService.SensorData data) {
        data.isActive = true;
    }

    @Override
    public void disable(SensorEventService.SensorData data) {
        data.isActive = false;
    }

    @Override
    public void onSensorUpdated(List<SensorEventService.SensorData> data) {
        String jsonStr = SensorEventService.sensorData2JsonString(data);
        if (SensorEventService.isActive(data)) {
            sensorListView.sendText(jsonStr);
        }
    }
}
