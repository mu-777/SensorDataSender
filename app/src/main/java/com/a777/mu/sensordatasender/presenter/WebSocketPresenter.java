package com.a777.mu.sensordatasender.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.a777.mu.sensordatasender.contract.WebSocketContract;
import com.a777.mu.sensordatasender.model.WebSocketService;

/**
 * Created by root on 16/10/04.
 */
public class WebSocketPresenter implements WebSocketContract.UserActions {

    private static final String TAG = "WebSocketPresenter";

    private Context appContext;
    private WebSocketContract.View webSocketView;
    private boolean isConnected = false;

    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (WebSocketService.ACTION_ONCONNECTED.equals(action)) {
                onConnect();
            } else if (WebSocketService.ACTION_ONDISCONNECTED.equals(action)) {
                onDisconnect();
            }
        }
    };

    public WebSocketPresenter(Context appContext, WebSocketContract.View webSocketView) {
        this.appContext = appContext;
        this.webSocketView = webSocketView;

        localBroadcastManager = LocalBroadcastManager.getInstance(appContext);
        IntentFilter filter = new IntentFilter();
        filter.addAction(WebSocketService.ACTION_ONCONNECTED);
        filter.addAction(WebSocketService.ACTION_ONDISCONNECTED);
        localBroadcastManager.registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void connect(String ip, String port) {
        Intent intent = new Intent(appContext, WebSocketService.class);
        intent.setAction(WebSocketService.ACTION_CONNECT);
        intent.putExtra(WebSocketService.PARAM_IP, ip);
        intent.putExtra(WebSocketService.PARAM_PORT, port);
        appContext.startService(intent);
    }

    @Override
    public void disconnect() {
        Intent intent = new Intent(appContext, WebSocketService.class);
        intent.setAction(WebSocketService.ACTION_DISCONNECT);
        appContext.startService(intent);
    }

    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public void sendText(String message) {
        Intent intent = new Intent(appContext, WebSocketService.class);
        intent.setAction(WebSocketService.ACTION_SENDTEXT);
        intent.putExtra(WebSocketService.PARAM_MESSAGE, message);
        appContext.startService(intent);
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
