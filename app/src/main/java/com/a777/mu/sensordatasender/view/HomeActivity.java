package com.a777.mu.sensordatasender.view;

import android.content.Context;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.a777.mu.sensordatasender.R;
import com.a777.mu.sensordatasender.contract.SensorListContract;
import com.a777.mu.sensordatasender.contract.WebSocketContract;
import com.a777.mu.sensordatasender.model.SensorEventService;
import com.a777.mu.sensordatasender.presenter.SensorListPresenter;
import com.a777.mu.sensordatasender.presenter.WebSocketPresenter;
import com.a777.mu.sensordatasender.presenter.WebSocketThreadPresenter;

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
        webSocketPresenter = new WebSocketThreadPresenter(this);
        sensorListPresenter = new SensorListPresenter(this, (SensorManager) getSystemService(SENSOR_SERVICE));
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
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "restart");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d(TAG, "onKeyDown");
            sensorListAdapter.notifyDataSetChanged();
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void startDetailActivity(SensorEventService.SensorData data) {
        DetailActivity.start(this, data);
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
        sendText("Success to connect!");
        Toast.makeText(HomeActivity.this, "Connected!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void disconnected() {
        Log.d(TAG, "disconnected!");
    }

    @Override
    public void sendText(String message) {
        webSocketPresenter.sendText(message);
    }

    @Override
    public void onSensorItemClick(SensorEventService.SensorData data) {
        SensorEventService.print(TAG, data);
        startDetailActivity(data);
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
