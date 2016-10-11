package com.a777.mu.sensordatasender.model;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by root on 16/10/05.
 */
public class SensorEventService implements SensorEventListener {
    private static final String TAG = "SensorEventService";

    private static SensorEventService instance;
    private static Map<Integer, SensorData> sensorDataDict;
    private List<SensorEventServiceListener> listeners = new ArrayList<>();

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
            Log.d(TAG, sensor.getName());
            sensorManager.registerListener(this, sensor, sensorManager.SENSOR_DELAY_UI);
            sensorDataDict.put(sensor.getType(), new SensorData(sensor));
        }
    }

    public List<SensorData> items() {
        return new ArrayList<SensorData>(sensorDataDict.values());
    }

    public SensorData item(Integer sensorType) {
        return sensorDataDict.get(sensorType);
    }

    public void addListener(SensorEventServiceListener listener) {
        listeners.add(listener);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        sensorDataDict.get(sensorEvent.sensor.getType()).setData(sensorEvent.values);
        for (SensorEventServiceListener listener : listeners) {
            listener.onSensorUpdated(items());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public class SensorData implements Serializable {
        private static final long serialVersionUID = 1L;

        public String name;
        public String key;
        public int type;
        public Map<Integer, Float> idDataMap = new HashMap<Integer, Float>();
        public Map<Integer, String> idKeyMap = new HashMap<Integer, String>();
        public boolean isActive = false;
        public boolean isFreezed = false;
        public Sensor sensor;

        private boolean isInitialized = false;

        public SensorData(Sensor sensor) {
            this.sensor = sensor;
            this.name = sensor.getName();
            this.key = this.name;
            this.type = sensor.getType();
        }

        public Map<String, Float> getMap() {
            Map<String, Float> map = new HashMap<String, Float>();
            if (isInitialized) {
                for (Map.Entry<Integer, String> pair : idKeyMap.entrySet()) {
                    map.put(pair.getValue(), idDataMap.get(pair.getKey()));
                }
            }
            return map;
        }

        public JSONObject getJSON() {
            JSONObject dataJson = new JSONObject(getMap());
            JSONObject retJson = new JSONObject();
            try {
                retJson.put(this.key, dataJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return retJson;
        }

        public void setData(float[] dataArr) {
            if (isFreezed) {
                return;
            }
            int i = 0;
            for (float data : dataArr) {
                idDataMap.put(i++, data);
            }
            if (!isInitialized) {
                initialize(dataArr);
            }
        }

        public void updateDataKeys(String sensorName, ArrayList<String> elementNames) {
            this.key = sensorName;
            int i = 0;
            for (String name : elementNames) {
                idKeyMap.put(i++, name);
            }
        }

        private void initialize(float[] dataArr) {
            int i = 0;
            for (float data : dataArr) {
                idKeyMap.put(i++, ((Integer) i).toString());
            }
            isInitialized = true;
        }


    }

    public static void print(String tag, SensorData data) {
        try {
            Log.d(tag, data.name + ", " + data.getJSON().toString(2));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static boolean isActive(List<SensorData> data) {
        for (SensorData d : data) {
            if (d.isActive) {
                return true;
            }
        }
        return false;
    }

    public static String sensorData2JsonString(List<SensorData> data) {
        JSONArray json = new JSONArray();
        for (SensorData d : data) {
            if (!d.isActive) {
                continue;
            }
            json.put(d.getJSON());
        }
        return json.toString();
    }
}
