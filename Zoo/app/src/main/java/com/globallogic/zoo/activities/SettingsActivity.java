package com.globallogic.zoo.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.globallogic.zoo.R;
import com.globallogic.zoo.helpers.ThemeHelper;

public class SettingsActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {
    public final static String THEME = "THEME";

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeHelper.onActivityCreateSetTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        spinner = (Spinner) findViewById(R.id.settingsactivity_spinner);
        spinner.setSelection(ThemeHelper.getsTheme(), true);
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
            ThemeHelper.changeToTheme(this, ThemeHelper.THEME_EXTRA);
        } else {
            ThemeHelper.changeToTheme(this, ThemeHelper.THEME_DEFAULT);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}