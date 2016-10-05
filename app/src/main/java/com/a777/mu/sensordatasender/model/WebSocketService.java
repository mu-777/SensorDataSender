package com.a777.mu.sensordatasender.model;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketExtension;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.util.List;
import java.util.Map;

public class WebSocketService extends IntentService {
    private static final String TAG = "WebSocketService";

    public static final String ACTION_CONNECT = "ACTION_CONNECT";
    public static final String ACTION_ONCONNECTED = "ACTION_ONCONNECTED";
    public static final String ACTION_DISCONNECT = "ACTION_DISCONNECT";
    public static final String ACTION_ONDISCONNECTED = "ACTION_ONDISCONNECTED";
    public static final String ACTION_SENDTEXT = "ACTION_SENDTEXT";

    public static final String PARAM_IP = "PARAM_IP";
    public static final String PARAM_PORT = "PARAM_PORT";
    public static final String PARAM_MESSAGE = "PARAM_SENDTEXT";


    private static final int TIMEOUT = 5000;

    private static WebSocket ws = null;
    private boolean isConnected = false;

    public WebSocketService() {
        super("WebSocketService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_CONNECT.equals(action)) {
                handleActionConnect(
                        intent.getStringExtra(PARAM_IP),
                        intent.getStringExtra(PARAM_PORT));
            } else if (ACTION_DISCONNECT.equals(action)) {
                handleActionDisconnect();
            } else if (ACTION_SENDTEXT.equals(action)) {
                handleActionSendText(intent.getStringExtra(PARAM_MESSAGE));
            }
        }
    }

    private void handleActionConnect(String ip, String port) {
        try {
            ws = connect("ws://" + ip + ":" + port);
            Log.d(TAG, ws.getURI().toString());
            ws.sendText("Success to connect");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleActionDisconnect() {
        Log.d(TAG, "Disconnecting...");
        if (ws == null) {
            return;
        }
        ws.disconnect();
        ws = null;
    }

    private void handleActionSendText(String message) {
        if (ws == null) {
            return;
        }
        ws.sendText(message);

    }

    private WebSocket connect(String uri) throws Exception {
        Log.d(TAG, "Connecting...");
        return new WebSocketFactory()
                .createSocket(uri, TIMEOUT)
                .addListener(new WebSocketAdapter() {
                    // A text message arrived from the server.
                    public void onTextMessage(WebSocket websocket, String message) {
                        Log.d(TAG, "message :" + message);
                    }

                    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) {
                        Log.d(TAG, "Connected");
                        isConnected = true;
                        LocalBroadcastManager
                                .getInstance(getApplicationContext())
                                .sendBroadcast(new Intent(ACTION_ONCONNECTED));
                    }

                    public void onDisconnected(WebSocket websocket,
                                               WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame,
                                               boolean closedByServer) {
                        Log.d(TAG, "Disconnected");
                        isConnected = false;
                        LocalBroadcastManager
                                .getInstance(getApplicationContext())
                                .sendBroadcast(new Intent(ACTION_ONDISCONNECTED));
                    }
                })
                .addExtension(WebSocketExtension.PERMESSAGE_DEFLATE)
                .connect();
    }

}
