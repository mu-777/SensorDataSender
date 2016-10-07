package com.a777.mu.sensordatasender.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.a777.mu.sensordatasender.contract.SensorDetailContract;

import java.util.List;

/**
 * Created by root on 16/10/05.
 */
public class SensorDetailActivity extends AppCompatActivity implements SensorDetailContract.View {

    public static void start(Context context, String fullRepositoryName) {
        final Intent intent = new Intent(context, SensorDetailActivity.class);
//        intent.putExtra(EXTRA_FULL_REPOSITORY_NAME, fullRepositoryName);
        context.startActivity(intent);
    }

    @Override
    public List<String> getDataNames() {
        return null;
    }

    @Override
    public String getDataKey() {
        return null;
    }
}
