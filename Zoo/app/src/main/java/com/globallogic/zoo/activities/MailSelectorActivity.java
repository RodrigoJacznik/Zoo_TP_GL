package com.globallogic.zoo.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.globallogic.zoo.R;
import com.globallogic.zoo.custom.views.ShareDialog;

import java.util.ArrayList;

public class MailSelectorActivity extends ActionBarActivity {
    public static final String EMAILS = "EMAILS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_selector);

        ListView listView = (ListView) findViewById(R.id.mailselectoractivity_emails);
        ArrayList<String> emails = getIntent().getStringArrayListExtra(EMAILS);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
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
}
