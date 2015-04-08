package com.globallogic.zoo.listeners;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.globallogic.zoo.R;
import com.globallogic.zoo.models.Schudle;

/**
 * Created by GL on 07/04/2015.
 */
public class onTableRowClickListener implements View.OnClickListener {
    private Schudle schudle;
    private Context context;

    public static final String[] EVENT_PROYECTION = new String[] {
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
    };

    private static final int EVENT_ID_INDEX = 0;
    private static final int EVENT_TITLE_INDEX = 1;
    private static final int EVENT_DTSTART_INDEX = 2;
    private static final int EVENT_DTEND_INDEX = 3;

    public onTableRowClickListener(Schudle schudle, Context context) {
        this.schudle = schudle;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        Cursor cur;
        ContentResolver cr = context.getContentResolver();
        Uri uri = CalendarContract.Events.CONTENT_URI;

        String selection = "(" +
                "( ? between " + CalendarContract.Events.DTSTART +
                " AND " + CalendarContract.Events.DTEND + ")" +
                    " OR " +
                "( ? between " + CalendarContract.Events.DTSTART +
                " AND " + CalendarContract.Events.DTEND + ")" +
                    " OR " +
                "( " + CalendarContract.Events.DTSTART + " between ? AND ? ))";

        String start = String.valueOf(schudle.getInitialHour().getTime());
        String end = String.valueOf(schudle.getFinalHour().getTime());

        String[] selectionargs = new String[] {start, end, start, end};

        cur = cr.query(uri, EVENT_PROYECTION, selection, selectionargs, null);

        if (cur.getCount() < 1) {
            Toast.makeText(context,
                    context.getString(R.string.animaldetailsactivity_without_event),
                    Toast.LENGTH_SHORT).show();
        } else {
            cur.moveToNext();
            String event_title = cur.getString(EVENT_TITLE_INDEX);
            String template = context.getString(R.string.animaldetailsactivity_with_event);

            Toast.makeText(context, String.format(template, event_title), Toast.LENGTH_LONG).show();
        }
        cur.close();
    }
}
