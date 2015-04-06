package com.globallogic.zoo.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;

/**
 * Created by GL on 06/04/2015.
 */
public class AlarmBroadcastReceiver extends BroadcastReceiver {

    public AlarmBroadcastReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(3000);
        Log.d("AlarmBroadcastReceiver", "Vibroo");
    }
}
