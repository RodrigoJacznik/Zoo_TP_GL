package com.globallogic.zoo.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.globallogic.zoo.R;
import com.globallogic.zoo.helpers.SharedPreferencesHelper;

public class SettingsActivity extends BaseActivity implements
        CompoundButton.OnClickListener, DialogInterface.OnClickListener {

    public final static String THEME = "THEME";

    private Switch sTheme;
    private boolean isExtraActivate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sTheme = (Switch) findViewById(R.id.settingsactivity_switch);
        isExtraActivate = SharedPreferencesHelper.isExtraActivate(this);
        sTheme.setChecked(isExtraActivate);

        sTheme.setOnClickListener(this);

        Button dummy = (Button) findViewById(R.id.settingsactivity_dummybutton);
        dummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this,
                        getResources().getString(R.string.settingsactivity_toast_msg),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(getString(R.string.settingsactivity_alert_msg))
                .setNegativeButton(getString(R.string.settingsactivity_negative), this)
                .setPositiveButton(getString(R.string.settingsactivity_positive), this)
                .show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            default:
            case Dialog.BUTTON_POSITIVE:
                setThemeAndRestart();
                break;
            case Dialog.BUTTON_NEGATIVE:
                sTheme.setChecked(isExtraActivate);
                dialog.dismiss();
                break;
        }
    }

    private void setThemeAndRestart() {
        int themeId = isExtraActivate ? R.style.AppTheme : R.style.ExtraTheme;
        sTheme.setChecked(! isExtraActivate);

        SharedPreferencesHelper.setTheme(SettingsActivity.this, themeId);
        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}