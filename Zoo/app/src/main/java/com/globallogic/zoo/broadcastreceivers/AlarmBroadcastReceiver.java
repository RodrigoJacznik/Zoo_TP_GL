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
import com.globallogic.zoo.models.Animal;

/**
 * Created by GL on 06/04/2015.
 */
public class AlarmBroadcastReceiver extends BroadcastReceiver {
    public static final String ANIMAL_ID = "ANIMAL_ID";

    public AlarmBroadcastReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        vibrate(context, 1000);
        int animalId = intent.getIntExtra(ANIMAL_ID, -1);
        makeNotification(context, animalId);
    }

    private void makeNotification(Context context, int animalId) {
        int notificationId = 1;
        PendingIntent pendingIntent = getPendingIntent(context, animalId);

        String content = String.format(context.getString(R.string.notification_content),
                Animal.getAnimalList().get(animalId).getName());

        NotificationCompat.Builder builder = getAnimalNotificationBuilder(context,
                pendingIntent, content);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, builder.build());
    }

    private void vibrate(Context context, int time) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(time);
    }

    private NotificationCompat.Builder getAnimalNotificationBuilder(
            Context context, PendingIntent pendingIntent, String content) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.sheep)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(content);

        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        return builder;
    }

    private PendingIntent getPendingIntent(Context context, int animalId) {
        Intent resultIntent = new Intent(context, AnimalDetailsActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        stackBuilder.addParentStack(AnimalDetailsActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        resultIntent.putExtra(AnimalDetailsActivity.ANIMAL, animalId);

        return stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
