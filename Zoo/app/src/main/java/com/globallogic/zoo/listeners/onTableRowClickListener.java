package com.globallogic.zoo.listeners;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.globallogic.zoo.R;
import com.globallogic.zoo.models.Schedule;
import com.globallogic.zoo.helpers.ContentProviderHelper;

/**
 * Created by GL on 07/04/2015.
 */
public class onTableRowClickListener implements View.OnClickListener {
    private Schedule schedule;
    private Context context;

    public onTableRowClickListener(Schedule schedule, Context context) {
        this.schedule = schedule;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        String eventName = ContentProviderHelper.checkUserHasAnEvent(context, schedule);

        if (eventName == null) {
            Toast.makeText(context,
                    context.getString(R.string.animaldetailsactivity_without_event),
                    Toast.LENGTH_SHORT).show();
        } else {
            String template = context.getString(R.string.animaldetailsactivity_with_event);
            Toast.makeText(context, String.format(template, eventName), Toast.LENGTH_LONG).show();
        }
    }
}
