package com.globallogic.zoo.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.globallogic.zoo.R;
import com.globallogic.zoo.utils.ThemeUtils;

public class SettingsActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {
    public final static String THEME = "THEME";

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.onActivityCreateSetTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        spinner = (Spinner) findViewById(R.id.settingsactivity_spinner);
        spinner.setSelection(ThemeUtils.getsTheme(), true);
        spinner.setOnItemSelectedListener(this);

        Button dummy = (Button) findViewById(R.id.settingsactivity_dummybutton);
        dummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this,
                        getResources().getString(R.string.settingsactivity_toast_msg),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String option = (String) parent.getItemAtPosition(position);
        if (option.equals("Extra")) {
            ThemeUtils.changeToTheme(this, ThemeUtils.THEME_EXTRA);
        } else {
            ThemeUtils.changeToTheme(this, ThemeUtils.THEME_DEFAULT);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}