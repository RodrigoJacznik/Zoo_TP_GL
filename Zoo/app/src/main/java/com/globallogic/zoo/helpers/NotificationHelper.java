package com.globallogic.zoo.helpers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.globallogic.zoo.R;
import com.globallogic.zoo.activities.WelcomeActivity;
import com.globallogic.zoo.models.Animal;

abstract public class NotificationHelper {

    public interface OnNotificationListener {
        public long getAnimalId();

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
        if (checkAnimalActivityOnScreen(animalId)) {

            ZooDatabaseHelper db = new ZooDatabaseHelper(context);
            Animal animal = db.getAnimalById(animalId);

            if (notificationCount > 1) {
                prepareMultipleNotification(context);
            } else {
                prepareSimpleNotification(context, animal);
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

    private static void prepareSimpleNotification(Context context, Animal animal) {
        pendingIntent = getAnimalDetailPendingIntent(context, animal.getId());
        content = String.format(context.getString(R.string.notification_single_content),
                animal.getName());
        title = context.getString(R.string.notification_single_title);
    }

    private static void prepareMultipleNotification(Context context) {
        pendingIntent = getWelcomeActivityPendingIntent(context);
        content = String.format(context.getString(R.string.notification_multi_content),
                notificationCount);
        title = String.format(context.getString(R.string.notification_multi_title),
                notificationCount);
    }


    private static PendingIntent getAnimalDetailPendingIntent(Context context, long animalId) {
        Intent resultIntent = new Intent(context, WelcomeActivity.class);
        resultIntent.putExtra(WelcomeActivity.ANIMAL, animalId);

        return PendingIntent.getActivity(context, 0, resultIntent, 0);
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
                    onNotificationListener.getAnimalId());
        }

        notificationManager.cancel(NOTIFICATION_ID);

        if (notificationCount > 1) {
            notificationCount = 1;
        }
    }

    private static boolean checkAnimalActivityOnScreen(long animalId) {
        return onNotificationListener == null ||
                onNotificationListener.getAnimalId() != animalId;
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
