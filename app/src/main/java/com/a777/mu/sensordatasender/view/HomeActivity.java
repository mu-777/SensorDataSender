package com.a777.mu.sensordatasender.view;

import android.content.Context;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.a777.mu.sensordatasender.R;
import com.a777.mu.sensordatasender.contract.SensorListContract;
import com.a777.mu.sensordatasender.contract.WebSocketContract;
import com.a777.mu.sensordatasender.model.SensorEventService;
import com.a777.mu.sensordatasender.presenter.SensorListPresenter;
import com.a777.mu.sensordatasender.presenter.WebSocketPresenter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class HomeActivity
        extends AppCompatActivity
        implements WebSocketContract.View, SensorListContract.View, SensorListAdapter.OnSensorItemClickListener {

    private static final String TAG = "HomeActivity";

    private SensorListAdapter sensorListAdapter;

    private WebSocketContract.UserActions webSocketPresenter;
    private SensorListContract.UserActions sensorListPresenter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupViews();
    }

    private void setupViews() {
        findViewById(R.id.btn_connect).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onConnectBtnClicked();
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_sensors);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        sensorListAdapter = new SensorListAdapter(
                (Context) this,
                (SensorListAdapter.OnSensorItemClickListener) this);
        recyclerView.setAdapter(sensorListAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        webSocketPresenter = new WebSocketPresenter(getApplication(), this);
        sensorListPresenter = new SensorListPresenter(this, (SensorManager) getSystemService(SENSOR_SERVICE));
    }

    @Override
    public void startDetailActivity(String sensorName) {
        SensorDetailActivity.start(this, sensorName);
    }

    @Override
    public void updateSensorList(List<SensorEventService.SensorData> data) {
        for (SensorEventService.SensorData d : data) {
            Log.d(TAG, d.name);
        }
        sensorListAdapter.updateSensorData(data);
    }

    private void onConnectBtnClicked() {
        if (webSocketPresenter.isConnected()) {
            webSocketPresenter.disconnect();
        } else {
            String ip = ((EditText) findViewById((R.id.txt_ip))).getText().toString();
            String port = ((EditText) findViewById((R.id.txt_port))).getText().toString();
            webSocketPresenter.connect(ip, port);
        }
    }

    @Override
    public void connected() {
        Log.d(TAG, "connected!");
    }

    @Override
    public void disconnected() {
    }

    @Override
    public void sendText(String message) {
        webSocketPresenter.sendText(message);
    }

    @Override
    public void onSensorItemClick(SensorEventService.SensorData data) {
        SensorEventService.print(TAG, data);

    }

    @Override
    public void onSensorItemSwitchClick(SensorEventService.SensorData data, boolean isChecked) {
        if (isChecked) {
            sensorListPresenter.enable(data);
        } else {
            sensorListPresenter.disable(data);
        }
    }

}
