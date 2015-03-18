package com.globallogic.zoo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;


public class MainActivity extends ActionBarActivity {
    private final static String PASS = "Android";
    private final static String USER = "GL";

    final static String USERK = "USER";
    final static String IS_FEMM = "IS_FEMM";

    private EditText pass;
    private EditText user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.mainactivity_siging).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass = ((EditText) findViewById(R.id.mainactivity_pass));
                user = ((EditText) findViewById(R.id.mainactivity_user));

                String passInput = pass.getText().toString();
                String userInput = user.getText().toString();

                int index = ((RadioGroup) findViewById(R.id.mainactivity_radiogroup)).
                        getCheckedRadioButtonId();

                if (passInput.equals(PASS) && userInput.equals(USER)) {
                    Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                    intent.putExtra(MainActivity.USERK, userInput);
                    intent.putExtra(MainActivity.IS_FEMM, index == R.id.mainactivity_femenino);
                    startActivity(intent);
                }
                else {
                    findViewById(R.id.mainactivity_error).setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
