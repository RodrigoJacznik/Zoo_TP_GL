package com.globallogic.zoo.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import com.globallogic.zoo.R;
import com.globallogic.zoo.broadcastreceivers.LowBatteryBroadcastReceiver;
import com.globallogic.zoo.helpers.SharedPreferencesHelper;

/**
 * Created by rodrigo on 4/11/15.
 */
public abstract class BaseActivity extends ActionBarActivity {

    protected LowBatteryBroadcastReceiver lowBatteryBroadcastReceiver;
    protected ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int theme = SharedPreferencesHelper.getTheme(this);
        setTheme(theme);

        super.onCreate(savedInstanceState);
        lowBatteryBroadcastReceiver = new LowBatteryBroadcastReceiver();
        actionBar = getSupportActionBar();
    }

    protected void setUpActionBar() {
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
        actionBar.setIcon(R.drawable.ic_action_logo);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(lowBatteryBroadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_LOW));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (lowBatteryBroadcastReceiver != null) {
            unregisterReceiver(lowBatteryBroadcastReceiver);
        }
    }
}
