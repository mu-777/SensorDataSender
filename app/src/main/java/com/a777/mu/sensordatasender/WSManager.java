package com.a777.mu.sensordatasender;

//http://qiita.com/yanix/items/8bfc00db7cae75d4c6e6

import android.util.Log;
import android.util.StringBuilderPrinter;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketExtension;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.util.List;
import java.util.Map;

/**
 * Created by ryosuke on 16/08/12.
 */
public class WSManager extends Thread {

    private static final String TAG = "WSManager";
    // Sensor Server IP
    private String uri;

    // The timeout value in milliseconds for socket connection.
    private static final int TIMEOUT = 5000;

    private WebSocket ws = null;
    private WebSocketAdapter wsAdapter;
    private static boolean isConnected = false;


    public WSManager() {
    }

    public WSManager setUri(String uri) {
        this.uri = uri;
        return this;
    }

    public WSManager setAdapter(WebSocketAdapter wsAdapter) {
        this.wsAdapter = wsAdapter;
        return this;
    }

    /**
     * Connect to the server.
     */
    public static WebSocket connect(String uri, WebSocketAdapter wsAdapter) throws Exception {
        Log.d(TAG, "Connecting...");
        return new WebSocketFactory()
                .createSocket(uri, TIMEOUT)
                .addListener(wsAdapter)
                .addListener(new WebSocketAdapter() {
                    // A text message arrived from the server.
                    public void onTextMessage(WebSocket websocket, String message) {
                        Log.d(TAG, "message :" + message);
                    }

                    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) {
                        Log.d(TAG, "Connected");
                        isConnected = true;
                    }

                    public void onDisconnected(WebSocket websocket,
                                               WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame,
                                               boolean closedByServer) {
                        Log.d(TAG, "Disconnected");
                        isConnected = false;
                    }
                })
                .addExtension(WebSocketExtension.PERMESSAGE_DEFLATE)
                .connect();
    }

    @Override
    public void run() {
        try {
            ws = connect(uri, wsAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        ws.disconnect();
        ws = null;
    }

    public void sendText(String message) {
        ws.sendText(message);
    }

    public boolean isConnected() {
        return isConnected;
    }

}
