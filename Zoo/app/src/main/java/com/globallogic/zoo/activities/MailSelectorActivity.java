package com.globallogic.zoo.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.globallogic.zoo.R;

import java.util.ArrayList;

public class MailSelectorActivity extends ActionBarActivity {
    public static final String EMAILS = "EMAILS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_selector);

        ListView listView = (ListView) findViewById(R.id.mailselectoractivity_emails);
        ArrayList<String> emails = getIntent().getStringArrayListExtra(EMAILS);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, emails);
        listView.setAdapter(adapter);

    }
}
