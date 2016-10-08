package com.a777.mu.sensordatasender.presenter;


import android.util.Log;

import com.a777.mu.sensordatasender.contract.WebSocketContract;
import com.a777.mu.sensordatasender.model.WebSocketThread;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.util.List;
import java.util.Map;

/**
 * Created by root on 16/10/04.
 */
public class WebSocketThreadPresenter implements WebSocketContract.UserActions {

    private static final String TAG = "WebSocketThreadPresenter";

    private WebSocketContract.View webSocketView;
    private WebSocketThread wsThread;
    private boolean isConnected = false;


    public WebSocketThreadPresenter(WebSocketContract.View webSocketView) {
        this.webSocketView = webSocketView;
        this.wsThread = new WebSocketThread();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public void connect(String ip, String port) {
        try {
            wsThread.setUri("ws://" + ip + ":" + port)
                    .setAdapter(new WebSocketAdapter() {
                        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) {
                            onConnect();
                        }

                        public void onDisconnected(WebSocket websocket,
                                                   WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame,
                                                   boolean closedByServer) {
                            onDisconnect();
                        }
                    })
                    .start();
        } catch (IllegalThreadStateException e) {
            e.printStackTrace();
            disconnect();
            connect(ip, port);
        }
    }

    @Override
    public void disconnect() {
        wsThread.disconnect();
    }

    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public void sendText(String message) {
        wsThread.sendText(message);
    }

    private void onConnect() {
        isConnected = true;
        webSocketView.connected();
    }

    private void onDisconnect() {
        isConnected = false;
        webSocketView.disconnected();
    }
}
