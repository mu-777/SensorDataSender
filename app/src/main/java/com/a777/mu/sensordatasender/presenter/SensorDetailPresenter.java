package com.a777.mu.sensordatasender.presenter;

import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.a777.mu.sensordatasender.contract.SensorDetailContract;
import com.a777.mu.sensordatasender.model.SensorEventService;
import com.a777.mu.sensordatasender.model.SensorEventServiceListener;
import com.a777.mu.sensordatasender.view.KeyChangeDialogFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 16/10/10.
 */
public class SensorDetailPresenter
        implements SensorDetailContract.UserActions, SensorEventServiceListener {

    private final static String TAG = "SensorDetailPresenter";

    private SensorDetailContract.View sensorDetailView;
    private SensorEventService sensorEventService;
    private int dataType;

    public SensorDetailPresenter(SensorDetailContract.View sensorDetailView, int dataType) {
        this.sensorDetailView = sensorDetailView;
        this.dataType = dataType;
        this.sensorEventService = SensorEventService.getInstance();
        initialize();
    }

    private void initialize() {
        sensorEventService.addListener(this);

        SensorEventService.SensorData data = sensorEventService.item(dataType);
        sensorDetailView.setDataName(data.name);
        sensorDetailView.setDataTable(data.getMap());
        sensorDetailView.setFreezeButton(data.isFreezed);
    }

    @Override
    public void freezeData(boolean flag) {
        SensorEventService.SensorData data = sensorEventService.item(dataType);
        data.isFreezed = flag;
    }

    @Override
    public void onDataViewClicked() {
        Log.d(TAG, "onDataViewClicked");
        SensorEventService.SensorData data = sensorEventService.item(dataType);
        sensorDetailView.showFragment(data.key, new ArrayList<String>(data.getMap().keySet()));
    }

    @Override
    public void onDataKeysChanged(String sensorName, ArrayList<String> elementNames) {
        SensorEventService.SensorData data = sensorEventService.item(dataType);
        data.updateDataKeys(sensorName, elementNames);
        sensorDetailView.setDataName(sensorName);
        sensorDetailView.updateDataTable(data.getMap());
    }

    @Override
    public void onSensorUpdated(List<SensorEventService.SensorData> dataList) {
        SensorEventService.SensorData data = sensorEventService.item(dataType);
        sensorDetailView.updateDataTable(data.getMap());
    }
}
