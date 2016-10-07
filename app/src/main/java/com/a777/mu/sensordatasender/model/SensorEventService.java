package com.a777.mu.sensordatasender.model;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;

import com.a777.mu.sensordatasender.view.SensorListAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by root on 16/10/05.
 */
public class SensorEventService implements SensorEventListener {

    private static SensorEventService instance;
    private Map<Integer, SensorData> sensorDataDict;

    public static SensorEventService getInstance(SensorManager sensorManager) {
        if (instance == null) {
            instance = new SensorEventService(sensorManager);
        }
        return instance;
    }

    public static SensorEventService getInstance() {
        return instance;
    }

    private SensorEventService(SensorManager sensorManager) {
        sensorDataDict = new HashMap<Integer, SensorData>();

        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor : sensors) {
            sensorManager.registerListener(this, sensor, sensorManager.SENSOR_DELAY_UI);
            sensorDataDict.put(sensor.getType(), new SensorData(sensor));
        }
    }

    public List<SensorData> items() {
        return (List<SensorData>) sensorDataDict.values();
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        sensorDataDict.get(sensorEvent.sensor.getType()).setData(sensorEvent.values);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public class SensorData {
        public String name;
        public String key;
        public Map<Integer, Float> dataMap;
        public Map<Integer, String> dataKeyMap;
        public boolean isActive = false;
        public Sensor sensor;
        private boolean isInitialized = false;

        public SensorData(Sensor sensor) {
            this.sensor = sensor;
            this.name = sensor.getName();
            this.key = this.name;
        }

        public void setData(float[] dataArr) {
            int i = 0;
            for (float data : dataArr) {
                dataMap.put(i++, data);
            }
            if (!isInitialized) {
                initialize(dataArr);
            }
        }

        private void initialize(float[] dataArr) {
            int i = 0;
            for (float data : dataArr) {
                dataKeyMap.put(i++, ((Integer) i).toString());
            }
        }

    }

}
