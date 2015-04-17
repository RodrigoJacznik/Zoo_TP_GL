package com.globallogic.zoo.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.globallogic.zoo.R;
import com.globallogic.zoo.custom.views.ShareDialog;
import com.globallogic.zoo.helpers.ContentProviderHelper;

import java.util.ArrayList;
import java.util.List;

public class MailSelectorActivity extends BaseActivity {
    public static final String EMAILS = "EMAILS";

    private List<String> emails = new ArrayList<>();

    private ListView emailsView;
    private ProgressBar progressBar;
    private TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_selector);

        bindViews();

        new FetchMailTask().execute();
    }

    private void bindViews() {
        emailsView = (ListView) findViewById(R.id.mailselectoractivity_emails);
        progressBar = (ProgressBar)
                findViewById(R.id.mailselectoractivity_progressbar);
        error = (TextView) findViewById(R.id.mailselectoractivity_error);
    }

    private void setUpListView() {
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, emails);

        emailsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra(ShareDialog.EMAIL, adapter.getItem(position));
                setResult(ShareDialog.RESULT_OK, intent);
                finish();
            }
        });
        emailsView.setAdapter(adapter);
    }

    final private class FetchMailTask extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... params) {
            return ContentProviderHelper.getContactsEmails(MailSelectorActivity.this);
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(List<String> fetchEmails) {
            progressBar.setVisibility(View.INVISIBLE);
            if (!fetchEmails.isEmpty()) {
                emails = fetchEmails;
            } else {
                error.setVisibility(View.VISIBLE);
            }
            setUpListView();
        }
    }
}


