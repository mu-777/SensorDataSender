package com.a777.mu.sensordatasender.contract;


import java.util.ArrayList;
import java.util.Map;

/**
 * Created by ryosuke on 16/10/02.
 */
public interface SensorDetailContract {
    interface View {
        void setDataTable(Map<String, Float> dataMap);

        void updateDataTable(Map<String, Float> dataMap);

        void setDataName(String dataName);

        void setFreezeButton(boolean flag);

        void showFragment(String sensorName, ArrayList<String> elementNames);
    }

    interface UserActions {
        void freezeData(boolean flag);

        void onDataViewClicked();

        void onDataKeysChanged(String sensorName, ArrayList<String> elementNames);
    }

}
