package com.a777.mu.sensordatasender.view;

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
import com.a777.mu.sensordatasender.presenter.WebSocketPresenter;

public class HomeActivity
        extends AppCompatActivity
        implements WebSocketContract.View, SensorListContract.View {

    private static final String TAG = "HomeActivity";

    private WebSocketContract.UserActions webSocketPresenter;
    private SensorListContract.UserActions sensorListPresenter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        webSocketPresenter = new WebSocketPresenter(getApplication(), this);
        setupViews();
    }

    private void setupViews() {
        findViewById(R.id.btn_connect).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onConnectBtnClicked();
            }
        });

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_sensors);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);



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
}
