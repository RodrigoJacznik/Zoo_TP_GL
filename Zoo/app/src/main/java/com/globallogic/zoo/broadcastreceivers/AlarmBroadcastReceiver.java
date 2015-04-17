package com.globallogic.zoo.broadcastreceivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;

import com.globallogic.zoo.helpers.NotificationHelper;

/**
 * Created by GL on 06/04/2015.
 */
public class AlarmBroadcastReceiver extends BroadcastReceiver {
    public static final String ANIMAL_ID = "ANIMAL_ID";
    private static int alarmCount = 0;

    public AlarmBroadcastReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        vibrate(context, 1000);

        long animalId = intent.getLongExtra(ANIMAL_ID, -1);
        NotificationHelper.makeNotification(context, animalId);
    }

    private void vibrate(Context context, int time) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(time);
    }

    public static void sendVibrateBroadcast(Context context, int seconds, long animalId) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);

        intent.putExtra(ANIMAL_ID, animalId);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, alarmCount++, intent, 0);

        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + seconds * 1000, alarmIntent);
    }
}
