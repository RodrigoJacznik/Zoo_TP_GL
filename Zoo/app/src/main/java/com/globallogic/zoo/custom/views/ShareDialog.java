package com.globallogic.zoo.custom.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.globallogic.zoo.R;
import com.globallogic.zoo.activities.MailSelectorActivity;

/**
 * Created by rodrigo on 4/5/15.
 */
public class ShareDialog extends DialogFragment {

    public static final String TAG = "SHARE_DIALOG";

    private static final int PICK_EMAIL_REQUEST = 0;
    public static final int RESULT_OK = 0;
    public static final String EMAIL = "EMAIL";

    private EditText email;

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
        email = (EditText) rootView.findViewById(R.id.dialog_mail_email);
        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MailSelectorActivity.class);
                startActivityForResult(intent, 0);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_EMAIL_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    email.setText(data.getStringExtra(EMAIL));
                }
            }
        }
    }
}
