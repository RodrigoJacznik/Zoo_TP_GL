package com.globallogic.zoo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.globallogic.zoo.R;
import com.globallogic.zoo.models.Animal;


public class LoginActivity extends ActionBarActivity implements TextWatcher {
    private final static String PASS = "Android";
    private final static String USER = "GL";

    private EditText pass;
    private EditText user;
    private Button signin;
    private TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Animal.populateAnimals();
        bindViews();
        setUpActionBar();

        signin.setEnabled(false);
        pass.addTextChangedListener(this);
        user.addTextChangedListener(this);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passInput = pass.getText().toString();
                String userInput = user.getText().toString();

                if (passInput.equals(PASS) && userInput.equals(USER)) {
                    Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                    intent.putExtra(WelcomeActivity.USERK, userInput);
                    restartDefaultViewState();
                    startActivity(intent);
                    finish(); // Para que welcome no vuelva a loging
                } else {
                    error.setVisibility(View.VISIBLE);
                }
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

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
        actionBar.setIcon(R.drawable.ic_action_logo);
    }

    private void restartDefaultViewState() {
        pass.setText("");
        user.setText("");
        error.setVisibility(View.INVISIBLE);
    }
    private void bindViews() {
        signin = (Button) findViewById(R.id.mainactivity_siging);
        pass = (EditText) findViewById(R.id.mainactivity_pass);
        user = (EditText) findViewById(R.id.mainactivity_user);
        error = (TextView) findViewById(R.id.mainactivity_error);
    }
}
