package com.a777.mu.sensordatasender.contract;

import android.content.Context;

/**
 * Created by ryosuke on 16/10/02.
 */
public interface WebSocketContract {

    interface View {
        void connected();

        void disconnected();

        void sendText(String message);
    }

    interface UserActions {
        void connect(String ip, String port);

        void disconnect();

        boolean isConnected();

        void sendText(String message);
    }


}
