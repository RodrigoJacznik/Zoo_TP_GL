package com.globallogic.zoo.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.globallogic.zoo.R;
import com.globallogic.zoo.helpers.HttpConnectionHelper;
import com.globallogic.zoo.helpers.SharedPreferencesHelper;


public class LoginActivity extends BaseActivity implements TextWatcher {

    private EditText pass;
    private EditText user;
    private Button signin;
    private TextView error;
    private ProgressBar load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (SharedPreferencesHelper.isUserLogin(this)) {
            Intent intent = WelcomeActivity.getIntent(LoginActivity.this);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_login);
        bindViews();
        setUpActionBar();

        signin.setEnabled(false);
        pass.addTextChangedListener(this);
        user.addTextChangedListener(this);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoginTask().execute();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menumain_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        signin.setEnabled(! user.getText().toString().isEmpty() && ! pass.getText().toString().isEmpty());
    }

    @Override
    protected void setUpActionBar() {
        super.setUpActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
    }

    private void bindViews() {
        signin = (Button) findViewById(R.id.mainactivity_siging);
        pass = (EditText) findViewById(R.id.mainactivity_pass);
        user = (EditText) findViewById(R.id.mainactivity_user);
        error = (TextView) findViewById(R.id.mainactivity_error);
        load = (ProgressBar) findViewById(R.id.mainactivity_load);
    }

    private void login(String userInput, String passInput) {
        Intent intent = WelcomeActivity.getIntent(LoginActivity.this);
        SharedPreferencesHelper.setUserNameAndPass(this, userInput, passInput);
        startActivity(intent);
        finish();
    }

    private class LoginTask extends AsyncTask<Void, Void, Boolean> {

        String passInput;
        String userInput;

        @Override
        protected Boolean doInBackground(Void... params) {
            return HttpConnectionHelper.login(LoginActivity.this, userInput, passInput);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            passInput = pass.getText().toString();
            userInput = user.getText().toString();
            load.setVisibility(View.VISIBLE);
            signin.setEnabled(false);
            error.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            load.setVisibility(View.INVISIBLE);
            if (aBoolean) {
                login(userInput, passInput);
            } else {
                error.setVisibility(View.VISIBLE);
                signin.setEnabled(true);
            }
        }
    }
}
