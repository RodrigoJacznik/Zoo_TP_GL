package com.globallogic.zoo.broadcastreceivers;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.ImageView;

import com.globallogic.zoo.R;

/**
 * Created by GL on 06/04/2015.
 */
public class LowBatteryBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(context.getString(R.string.lowbatteryalertdialog_low_battery));
        ImageView view = new ImageView(context);
        view.setImageResource(R.drawable.mapa_zoo);
        alertDialog.setView(view);
        alertDialog.setPositiveButton(context.getString(R.string.lowbatteryalertdialog_ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
        });
        alertDialog.show();
    }
}
