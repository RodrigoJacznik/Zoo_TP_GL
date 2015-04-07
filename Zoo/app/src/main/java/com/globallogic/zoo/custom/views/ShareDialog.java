package com.globallogic.zoo.custom.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.globallogic.zoo.R;
import com.globallogic.zoo.activities.MailSelectorActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rodrigo on 4/5/15.
 */
public class ShareDialog extends DialogFragment {

    private static final String[] CONTACT_PROYECTION = new String[] {
            ContactsContract.RawContacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
    };

    public interface NoticeShareDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String[] email);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    private NoticeShareDialogListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (NoticeShareDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View rootView = inflater.inflate(R.layout.dialog_mail, null);

        ImageButton contacts = (ImageButton) rootView.findViewById(R.id.dialog_mail_contacts);
        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> emails = (ArrayList<String>) getContacts(context);
                Intent intent = new Intent(context, MailSelectorActivity.class);
                intent.putStringArrayListExtra(MailSelectorActivity.EMAILS, emails);
                startActivity(intent);
            }
        });

        builder.setView(rootView)
                .setTitle(getString(R.string.sharedialog_title));

        builder.setPositiveButton(getString(R.string.sharedialog_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                EditText editText = (EditText) rootView.findViewById(R.id.dialog_mail_email);
                String[] emailTo = new String[]{editText.getText().toString()};
                listener.onDialogPositiveClick(ShareDialog.this, emailTo);
            }
        });
        builder.setNegativeButton(getString(R.string.sharedialog_cancel),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onDialogNegativeClick(ShareDialog.this);
            }
        });

        return builder.create();
    }

    private List<String> getContacts(Context context) {
        List<String> emails = new ArrayList<>();

        Cursor cur;
        ContentResolver cr = context.getContentResolver();
        Uri uri = ContactsContract.Contacts.CONTENT_URI;

        cur = cr.query(uri, CONTACT_PROYECTION, null, null, null);
        if (cur != null && cur.getCount() > 1) {
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
            cur.close();
        }
        return emails;
    }
}
