package com.globallogic.zoo.broadcastreceivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

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
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);

        int animalId = intent.getIntExtra(ANIMAL_ID, -1);
        String content = String.format(context.getString(R.string.notification_content),
                Animal.getAnimalList().get(animalId).getName());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.sheep)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(content);

        Intent resultIntent = new Intent(context, AnimalDetailsActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        stackBuilder.addParentStack(AnimalDetailsActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        resultIntent.putExtra(AnimalDetailsActivity.ANIMAL, animalId);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        int notificationId = 001;
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, builder.build());
    }
}
