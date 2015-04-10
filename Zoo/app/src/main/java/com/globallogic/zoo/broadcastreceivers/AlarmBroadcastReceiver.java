package com.globallogic.zoo.broadcastreceivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import com.globallogic.zoo.R;
import com.globallogic.zoo.activities.AnimalDetailsActivity;
import com.globallogic.zoo.activities.WelcomeActivity;
import com.globallogic.zoo.models.Animal;

/**
 * Created by GL on 06/04/2015.
 */
public class AlarmBroadcastReceiver extends BroadcastReceiver {
    public static final String ANIMAL_ID = "ANIMAL_ID";
    public static final String MULTI_NOTIFICATION = "MULTI_NOTIFICATION";

    private static int notificationCount = 0;

    public AlarmBroadcastReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        vibrate(context, 1000);

        long animalId = intent.getLongExtra(ANIMAL_ID, -1);
        makeNotification(context, animalId);
    }

    private void makeNotification(Context context, long animalId) {
        int notificationId = 1;

        String content = String.format(context.getString(R.string.notification_content),
                Animal.getById(animalId).getName());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.sheep)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(content);

        builder.setAutoCancel(true)
               .setNumber(notificationCount++);

        PendingIntent pendingIntent;

        if (notificationCount > 1) {
            pendingIntent = getWelcomeActivityPendingIntent(context);
        } else {
            pendingIntent = getAnimalDetailPendingIntent(context, animalId);
        }

        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, builder.build());
    }

    private void vibrate(Context context, int time) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(time);
    }

    private PendingIntent getAnimalDetailPendingIntent(Context context, long animalId) {
        Intent resultIntent = new Intent(context, AnimalDetailsActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        stackBuilder.addParentStack(AnimalDetailsActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        resultIntent.putExtra(AnimalDetailsActivity.ANIMAL, animalId);

        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getWelcomeActivityPendingIntent(Context context) {
        Intent resultIntent = new Intent(context, WelcomeActivity.class);
        resultIntent.putExtra(MULTI_NOTIFICATION, true);
        return PendingIntent.getActivity(context, 0, resultIntent, 0);
    }

    static public void resetNotificationCount(Boolean bool) {
        if (bool) {
            notificationCount = 0;
        }
    }
}
