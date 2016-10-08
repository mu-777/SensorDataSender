package com.a777.mu.sensordatasender.model;

import java.util.List;

/**
 * Created by root on 16/10/08.
 */
public interface SensorEventServiceListener {
    void onSensorUpdated(List<SensorEventService.SensorData> data);
}
