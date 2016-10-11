package com.a777.mu.sensordatasender.view;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.a777.mu.sensordatasender.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 16/10/10.
 */
public class KeyChangeDialogFragment extends DialogFragment {

    private final static String TAG = "KeyChangeDialogFragment";
    private final static String KEY_SENSOR_NAME = "SENSOR_NAME";
    private final static String KEY_DATA_ELEMENTS = "DATA_ELEMENTS";

    private DialogInterface.OnClickListener okClickListener;
    private DialogInterface.OnClickListener cancelClickListener;
    private EditText etSensorName;
    private List<EditText> etElementNames;

    public static KeyChangeDialogFragment getInstance(String name, ArrayList<String> elements) {
        KeyChangeDialogFragment fragment = new KeyChangeDialogFragment();
        Bundle args = new Bundle();
        args.putString(KEY_SENSOR_NAME, name);
        args.putStringArrayList(KEY_DATA_ELEMENTS, elements);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String sensorName = getArguments().getString(KEY_SENSOR_NAME);
        ArrayList<String> elementNames = getArguments().getStringArrayList(KEY_DATA_ELEMENTS);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.key_change_fragment_title)
                .setView(generateKeyEditView(sensorName, elementNames))
                .setPositiveButton("OK", okClickListener)
                .setNegativeButton("Cancel", cancelClickListener);
        return builder.create();
    }

    private LinearLayout generateKeyEditView(String sensorName, ArrayList<String> elementNames) {
        LinearLayout keyEditView = new LinearLayout(getContext());
        keyEditView.setOrientation(LinearLayout.VERTICAL);

        etSensorName = new EditText(this.getContext());
        etSensorName.setText(sensorName);
        keyEditView.addView(etSensorName);

        etElementNames = new ArrayList<EditText>();
        for (String elementName : elementNames) {
            EditText et = new EditText(this.getContext());
            et.setText(elementName);
            keyEditView.addView(et);
            etElementNames.add(et);
        }

        return keyEditView;
    }

    public String getSensorName() {
        return etSensorName.getText().toString();
    }

    public ArrayList<String> getElementNames() {
        ArrayList<String> ret = new ArrayList<String>();
        for (EditText et : etElementNames) {
            ret.add(et.getText().toString());
        }
        return ret;
    }

    public void setOnOkClickListener(DialogInterface.OnClickListener listener) {
        this.okClickListener = listener;
    }

    public void setOnCancelClickListener(DialogInterface.OnClickListener listener) {
        this.cancelClickListener = listener;
    }

    @Override
    public void onPause() {
        super.onPause();
        dismiss();
    }
}
