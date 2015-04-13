package com.globallogic.zoo.helpers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.globallogic.zoo.R;
import com.globallogic.zoo.activities.AnimalDetailsActivity;
import com.globallogic.zoo.activities.WelcomeActivity;
import com.globallogic.zoo.models.Animal;

/**
 * Created by rodrigo on 4/11/15.
 */
abstract public class NotificationHelper {

    public interface OnNotificationListener {
        public long getId();

        public void onNotification();
    }

    public static final String MULTI_NOTIFICATION = "MULTI_NOTIFICATION";
    private static final int NOTIFICATION_ID = 1;
    private static int notificationCount = 1;

    private static PendingIntent pendingIntent;
    private static String content;
    private static String title;

    private static OnNotificationListener onNotificationListener;

    public static void makeNotification(Context context, long animalId) {
        Animal animal = Animal.getById(animalId);

        if (checkAnimalActivityOnScreen(animalId)) {
            if (notificationCount > 1) {
                pendingIntent = getWelcomeActivityPendingIntent(context);
                content = String.format(context.getString(R.string.notification_multi_content),
                        notificationCount);
                title = String.format(context.getString(R.string.notification_multi_title),
                        notificationCount);
            } else {
                pendingIntent = getAnimalDetailPendingIntent(context, animalId);
                content = String.format(context.getString(R.string.notification_single_content),
                        animal.getName());
                title = context.getString(R.string.notification_single_title);
            }

            SharedPreferencesHelper.incrementAnimalNotificationCount(context, animalId);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.zooimg)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setNumber(notificationCount++)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, builder.build());

        } else {
            onNotificationListener.onNotification();
        }
    }


    private static PendingIntent getAnimalDetailPendingIntent(Context context, long animalId) {
        Intent resultIntent = new Intent(context, AnimalDetailsActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        stackBuilder.addParentStack(AnimalDetailsActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        resultIntent.putExtra(AnimalDetailsActivity.ANIMAL, animalId);

        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static PendingIntent getWelcomeActivityPendingIntent(Context context) {
        Intent resultIntent = new Intent(context, WelcomeActivity.class);
        resultIntent.putExtra(MULTI_NOTIFICATION, true);
        return PendingIntent.getActivity(context, 0, resultIntent, 0);
    }

    public static void cancelNotification(Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (onNotificationListener != null) {
            SharedPreferencesHelper.resetAnimalNotificationCount(context,
                    onNotificationListener.getId());
        }

        notificationManager.cancel(NOTIFICATION_ID);

        if (notificationCount > 1) {
            notificationCount = 1;
        }
    }

    private static boolean checkAnimalActivityOnScreen(long animalId) {
        return onNotificationListener == null ||
                onNotificationListener.getId() != animalId;
    }

    public static void registerListener(OnNotificationListener onNotificationListener) {
        NotificationHelper.onNotificationListener = onNotificationListener;
    }

    public static void unregisterListener() {
        if (NotificationHelper.onNotificationListener != null) {
            onNotificationListener = null;
        }
    }
}
