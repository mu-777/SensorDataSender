package com.a777.mu.sensordatasender.contract;

import java.util.List;

/**
 * Created by ryosuke on 16/10/02.
 */
public interface SensorDetailContract {
    interface View {
        List<String> getDataNames();

        String getDataKey();

    }

    interface UserActions {
        void enable();

        void disable();
    }

}
