package com.a777.mu.sensordatasender.view;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.a777.mu.sensordatasender.R;
import com.a777.mu.sensordatasender.contract.SensorDetailContract;
import com.a777.mu.sensordatasender.model.SensorEventService;
import com.a777.mu.sensordatasender.presenter.SensorDetailPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailActivity
        extends AppCompatActivity
        implements SensorDetailContract.View {

    private static final String TAG = "DetailActivity";
    private static final String EXTRA_SENSORTYPE_NAME = "EXTRA_SENSORTYPE_NAME";

    private SensorDetailContract.UserActions sensorDetailPresenter;

    public static void start(Context context, SensorEventService.SensorData data) {
        final Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_SENSORTYPE_NAME, data.type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setupViews();

        int dataType = getIntent().getIntExtra(EXTRA_SENSORTYPE_NAME, 0);
        sensorDetailPresenter = new SensorDetailPresenter(this, dataType);
    }

    private void setupViews() {
        CompoundButton freezeBtn = (CompoundButton) findViewById(R.id.btn_freeze_data);
        freezeBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (sensorDetailPresenter == null) {
                    return;
                }
                sensorDetailPresenter.freezeData(isChecked);
            }
        });

        View dataView = (View) findViewById(R.id.view_detail_data);
        dataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sensorDetailPresenter.onDataViewClicked();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        super.onKeyDown(keyCode, event);
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Log.d(TAG, "onKeyDown");
//            return true;
//        }
//        return false;
//    }

    @Override
    public void setDataName(String dataName) {
        ((TextView) findViewById(R.id.txt_sensor_name)).setText(dataName);
    }

    @Override
    public void setDataTable(Map<String, Float> dataMap) {
        TableLayout table = (TableLayout) findViewById(R.id.tbl_sensor_data);

        int rowIdx = 0;
        for (Map.Entry<String, Float> pair : dataMap.entrySet()) {
            getLayoutInflater().inflate(R.layout.content_sensor_datatablerow, table);
            TableRow row = (TableRow) table.getChildAt(rowIdx);
            ((TextView) row.findViewById(R.id.txt_datatable_key)).setText(pair.getKey());
            ((TextView) row.findViewById(R.id.txt_datatable_data)).setText(String.valueOf(pair.getValue()));
            rowIdx++;
        }
    }

    @Override
    public void updateDataTable(Map<String, Float> dataMap) {
        TableLayout table = (TableLayout) findViewById(R.id.tbl_sensor_data);
        int rowIdx = 0;
        for (Map.Entry<String, Float> pair : dataMap.entrySet()) {
            TableRow row = (TableRow) table.getChildAt(rowIdx);
            ((TextView) row.findViewById(R.id.txt_datatable_key)).setText(pair.getKey());
            ((TextView) row.findViewById(R.id.txt_datatable_data)).setText(String.valueOf(pair.getValue()));
            rowIdx++;
        }
    }

    @Override
    public void setFreezeButton(boolean flag) {
        CompoundButton freezeBtn = (CompoundButton) findViewById(R.id.btn_freeze_data);
        freezeBtn.setChecked(flag);
    }

    @Override
    public void showFragment(String sensorName, ArrayList<String> elementNames) {
        final KeyChangeDialogFragment fragment = KeyChangeDialogFragment.getInstance(sensorName, elementNames);
        fragment.setOnOkClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, fragment.getSensorName());
                sensorDetailPresenter.onDataKeysChanged(fragment.getSensorName(), fragment.getElementNames());
            }
        });
        fragment.setOnCancelClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, fragment.getElementNames().get(0));
            }
        });
        fragment.show(getSupportFragmentManager(), TAG + "_keyChangeDialog");

    }
}
