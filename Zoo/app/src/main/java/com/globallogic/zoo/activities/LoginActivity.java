package com.globallogic.zoo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.globallogic.zoo.R;
import com.globallogic.zoo.helpers.SharedPreferencesHelper;
import com.globallogic.zoo.helpers.ZooDatabaseHelper;
import com.globallogic.zoo.network.API;


public class LoginActivity extends BaseActivity implements TextWatcher, API.OnLoginRequestListener {

    private EditText pass;
    private EditText user;
    private Button signin;
    private TextView error;
    private ProgressBar load;
    private String passInput;
    private String userInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (SharedPreferencesHelper.isUserLogin(this)) {
            Intent intent = WelcomeActivity.getWelcomeIntent(LoginActivity.this);
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
                login();
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        signin.setEnabled(!user.getText().toString().isEmpty() && !pass.getText().toString().isEmpty());
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

    private void successlogin(String userInput, String passInput) {
        Intent intent = WelcomeActivity.getWelcomeIntent(LoginActivity.this);
        SharedPreferencesHelper.setUserNameAndPass(this, userInput, passInput);
        startActivity(intent);
        finish();
    }

    private void login() {
        passInput = pass.getText().toString();
        userInput = user.getText().toString();
        load.setVisibility(View.VISIBLE);
        signin.setEnabled(false);
        error.setVisibility(View.INVISIBLE);
        API.login(this, this, userInput, passInput);
    }

    @Override
    public void onSuccess() {
        load.setVisibility(View.INVISIBLE);
        ZooDatabaseHelper db = new ZooDatabaseHelper(LoginActivity.this);
        db.insertOrUpdateUser(userInput);
        successlogin(userInput, passInput);
    }

    @Override
    public void onFail(int code) {
        error.setVisibility(View.VISIBLE);
        signin.setEnabled(true);
    }
}
