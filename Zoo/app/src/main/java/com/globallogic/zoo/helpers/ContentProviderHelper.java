package com.globallogic.zoo.helpers;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.ContactsContract;

import com.globallogic.zoo.models.Schedule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rodrigo on 4/11/15.
 */
public abstract class ContentProviderHelper {

    private static final String[] CONTACT_PROJECTION = new String[]{
            ContactsContract.RawContacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
    };

    private static final int CONTACT_ID_INDEX = 0;
    private static final int CONTACT_DISPLAY_NAME_INDEX = 1;

    public static final String[] EVENT_PROYECTION = new String[]{
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
    };

    private static final int EVENT_ID_INDEX = 0;
    private static final int EVENT_TITLE_INDEX = 1;
    private static final int EVENT_DTSTART_INDEX = 2;
    private static final int EVENT_DTEND_INDEX = 3;

    public static List<String> getContactsEmails(Context context) {
        ContentResolver cr = context.getContentResolver();
        List<String> emails = new ArrayList<>();

        Uri uri = ContactsContract.Contacts.CONTENT_URI;

        Cursor cur = cr.query(uri, CONTACT_PROJECTION, null, null, null);
        if (cur != null && cur.getCount() > 1) {
            emails = matchContactMail(cur, cr);
            cur.close();
        }

        return emails;
    }

    private static List<String> matchContactMail(Cursor cur, ContentResolver cr) {
        List<String> emails = new ArrayList<>();

        while (cur.moveToNext()) {
            Cursor emailCur = cr.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + cur.getString(0),
                    null, null);
            int index = emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
            while (emailCur.moveToNext()) {
                emails.add(emailCur.getString(index));
            }
            emailCur.close();
        }

        return emails;
    }

    public static String checkUserHasAnEvent(Context context, Schedule schedule) {
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

        String start = String.valueOf(schedule.getInitialHour().getTime());
        String end = String.valueOf(schedule.getFinalHour().getTime());

        String[] selectionargs = new String[]{start, end, start, end};

        cur = cr.query(uri, EVENT_PROYECTION, selection, selectionargs, null);

        String eventName = null;
        if (cur.getCount() >= 1) {
            cur.moveToNext();
            eventName = cur.getString(EVENT_TITLE_INDEX);
        }
        cur.close();
        return eventName;
    }
}

