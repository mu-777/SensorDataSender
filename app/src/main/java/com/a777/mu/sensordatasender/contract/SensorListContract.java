package com.a777.mu.sensordatasender.contract;

/**
 * Created by ryosuke on 16/10/02.
 */
public interface SensorListContract {
    interface View {

    }

    interface UserActions {
        void enable();

        void disable();
    }
}
