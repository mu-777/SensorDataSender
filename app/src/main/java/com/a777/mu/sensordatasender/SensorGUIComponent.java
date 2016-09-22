package com.a777.mu.sensordatasender;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by ryosuke on 16/08/12.
 */

public class SensorGUIComponent {

    private static final String TAG = "SensorGUIComponent";

    RelativeLayout mLinearLayout;
    boolean IsEnable = false;

    public SensorGUIComponent(Context context, String title) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLinearLayout = (RelativeLayout) inflater.inflate(R.layout.card_base, null);
        ((TextView) mLinearLayout.findViewById(R.id.tv_title)).setText(title);
        ((Switch) mLinearLayout.findViewById(R.id.switch_enable)).setChecked(IsEnable);
        ((Switch) mLinearLayout.findViewById(R.id.switch_enable))
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        IsEnable = isChecked;
                    }
                });
    }

}

