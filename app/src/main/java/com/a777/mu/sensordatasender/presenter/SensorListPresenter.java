package com.a777.mu.sensordatasender.presenter;

import android.hardware.SensorManager;

import com.a777.mu.sensordatasender.contract.SensorListContract;
import com.a777.mu.sensordatasender.model.SensorEventService;

/**
 * Created by root on 16/10/05.
 */
public class SensorListPresenter implements SensorListContract.UserActions {

    private SensorListContract.View sensorListView;
    private SensorEventService sensorService;

    public SensorListPresenter(SensorListContract.View sensorListView, SensorManager sensorManager) {
        this.sensorService = SensorEventService.getInstance(sensorManager);
        this.sensorListView = sensorListView;
        this.sensorListView.updateSensorList(this.sensorService.items());
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

}
