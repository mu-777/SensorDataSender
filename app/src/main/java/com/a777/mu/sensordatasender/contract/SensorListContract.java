package com.a777.mu.sensordatasender.contract;

import com.a777.mu.sensordatasender.model.SensorEventService;

import java.util.List;

/**
 * Created by ryosuke on 16/10/02.
 */

public interface SensorListContract {
    interface View {
        void updateSensorList(List<SensorEventService.SensorData> data);

        void startDetailActivity(String sensorName);
    }

    interface UserActions {
        void enable();

        void disable();
    }
}
