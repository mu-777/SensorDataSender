package com.a777.mu.sensordatasender;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

//REF: http://tb-lab.hatenablog.jp/entry/2015/02/14/210611

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "MainActivity";
    private static final double RAD2DEG = 180 / Math.PI;

    private SensorManager mSensorManager;
    private WSManager mWebSocketManager;

    private SensorGUIComponent mAccelComp;
    private SensorGUIComponent mMagneticComp;
    private SensorGUIComponent mOrientationComp;
    private boolean mIsConnected = false;

    float[] rotationMatrix = new float[9];
    float[] gravity = new float[3];
    float[] geomagnetic = new float[3];
    float[] attitude = new float[3];

    private boolean mIsMagSensor = true;
    private boolean mIsAccSensor = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        initWebSocket();
        initSensorComps();
        test();

    }

    private void test() {
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mWebSocketManager.isConnected()) {
                    String txt = ((EditText) findViewById(R.id.editText)).getText().toString();
                    Log.d(TAG, txt);
                    mWebSocketManager.sendText(txt);
                }
            }
        });
    }

    private void initSensorComps() {
        Context context = getApplicationContext();
        LinearLayout cardLinear = (LinearLayout) this.findViewById(R.id.cardLinear);

        mAccelComp = new SensorGUIComponent(context, "Accel");
        cardLinear.addView(mAccelComp.mLinearLayout);

        mMagneticComp = new SensorGUIComponent(context, "Magnetic");
        cardLinear.addView(mMagneticComp.mLinearLayout);

        mOrientationComp = new SensorGUIComponent(context, "Orientation");
        cardLinear.addView(mOrientationComp.mLinearLayout);

    }

    private void initWebSocket() {
        mWebSocketManager = new WSManager();

        findViewById(R.id.btn_connect).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!mWebSocketManager.isConnected()) {
                    String ip = ((EditText) findViewById(R.id.text_ip)).getText().toString();
                    String port = ((EditText) findViewById(R.id.text_port)).getText().toString();
                    mWebSocketManager
                            .setUri("ws://" + ip + ":" + port)
                            .setAdapter(new WebSocketAdapter() {
                                public void onConnected(WebSocket websocket, Map<String, List<String>> headers) {
                                    findViewById(R.id.btn_connect).setBackgroundColor(Color.GREEN);
                                }

                                public void onDisconnected(WebSocket websocket,
                                                           WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame,
                                                           boolean closedByServer) {
                                    findViewById(R.id.btn_connect).setBackgroundColor(Color.GRAY);
                                }
                            })
                            .start();
                } else {
                    mWebSocketManager.disconnect();
                }
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_MAGNETIC_FIELD:
                geomagnetic = event.values.clone();
                break;
            case Sensor.TYPE_ACCELEROMETER:
                gravity = event.values.clone();
                break;
            default:
                return;
        }

        JSONObject jsonObject = new JSONObject();

        if (mAccelComp.IsEnable && gravity != null) {
            JSONObject accelJson = new JSONObject();
            try {
                accelJson.put("x", (double) gravity[0]);
                accelJson.put("y", (double) gravity[1]);
                accelJson.put("z", (double) gravity[2]);
                jsonObject.put("accel", accelJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (mMagneticComp.IsEnable && geomagnetic != null) {
            JSONObject magJson = new JSONObject();
            try {
                magJson.put("x", (double) gravity[0]);
                magJson.put("y", (double) gravity[1]);
                magJson.put("z", (double) gravity[2]);
                jsonObject.put("geomagnetic", magJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        if (mOrientationComp.IsEnable && geomagnetic != null && gravity != null) {
            SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic);
            SensorManager.getOrientation(rotationMatrix, attitude);
            JSONObject oriJson = new JSONObject();
            try {
                oriJson.put("azimuth", attitude[0] * RAD2DEG);
                oriJson.put("pitch", attitude[1] * RAD2DEG);
                oriJson.put("roll", attitude[2] * RAD2DEG);
                jsonObject.put("ori", oriJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (mWebSocketManager.isConnected()) {
            mWebSocketManager.sendText(jsonObject.toString());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onResume() {
        super.onResume();

        // センサの取得
        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        // センサマネージャへリスナーを登録(implements SensorEventListenerにより、thisで登録する)
        for (Sensor sensor : sensors) {

            if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
                mIsMagSensor = true;
            }

            if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
                mIsAccSensor = true;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
