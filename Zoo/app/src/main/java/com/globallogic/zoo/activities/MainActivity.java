package com.globallogic.zoo.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.globallogic.zoo.R;


public class MainActivity extends ActionBarActivity implements TextWatcher {
    private final static String PASS = "Android";
    private final static String USER = "GL";

    final static String USERK = "USER";
    final static String IS_FEMM = "IS_FEMM";

    private EditText pass;
    private EditText user;
    private Button signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signin = (Button) findViewById(R.id.mainactivity_siging);
        pass = (EditText) findViewById(R.id.mainactivity_pass);
        user = (EditText) findViewById(R.id.mainactivity_user);

        signin.setEnabled(false);
        pass.addTextChangedListener(this);
        user.addTextChangedListener(this);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passInput = pass.getText().toString();
                String userInput = user.getText().toString();

                int index = ((RadioGroup) findViewById(R.id.mainactivity_radiogroup)).
                        getCheckedRadioButtonId();

                if (passInput.equals(PASS) && userInput.equals(USER)) {
                    Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                    intent.putExtra(MainActivity.USERK, userInput);
                    intent.putExtra(MainActivity.IS_FEMM, index == R.id.mainactivity_femenino);
                    pass.setText("");
                    user.setText("");
                    startActivity(intent);
                } else {
                    findViewById(R.id.mainactivity_error).setVisibility(View.VISIBLE);
                }
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
        signin.setEnabled(! user.getText().toString().isEmpty() && ! pass.getText().toString().isEmpty());
    }
}
