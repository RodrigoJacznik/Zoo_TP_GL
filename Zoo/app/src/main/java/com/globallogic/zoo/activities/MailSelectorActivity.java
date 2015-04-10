package com.globallogic.zoo.activities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.globallogic.zoo.R;
import com.globallogic.zoo.custom.views.ShareDialog;

import java.util.ArrayList;
import java.util.List;

public class MailSelectorActivity extends ActionBarActivity {
    public static final String EMAILS = "EMAILS";
    private List<String> emails = new ArrayList<>();
    ListView listView;

    private static final String[] CONTACT_PROJECTION = new String[] {
            ContactsContract.RawContacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_selector);

        listView = (ListView) findViewById(R.id.mailselectoractivity_emails);

        FetchMailTask fetchMailTask = new FetchMailTask();
        fetchMailTask.execute();
    }

    private void setUpListView() {
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, emails);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra(ShareDialog.EMAIL, adapter.getItem(position));
                setResult(ShareDialog.RESULT_OK, intent);
                finish();
            }
        });
        listView.setAdapter(adapter);
    }


    private List<String> getContactsEmails(Context context) {
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

    private List<String> matchContactMail(Cursor cur, ContentResolver cr) {
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

    class FetchMailTask extends AsyncTask<Void, Void, List<String>> {
        ProgressBar progressBar;

        @Override
        protected List<String> doInBackground(Void... params) {
            return getContactsEmails(MailSelectorActivity.this);
        }

        @Override
        protected void onPreExecute() {
            progressBar = (ProgressBar)
                    findViewById(R.id.mailselectoractivity_progressbar);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(List<String> emails) {
            MailSelectorActivity.this.emails = emails;
            setUpListView();
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}


