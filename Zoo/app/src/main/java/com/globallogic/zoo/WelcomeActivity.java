package com.globallogic.zoo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;


public class WelcomeActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Intent intent = getIntent();
        Boolean isFemm = intent.getBooleanExtra(MainActivity.IS_FEMM, true);
        String name = intent.getStringExtra(MainActivity.USERK);

        TextView tv = (TextView) findViewById(R.id.welcomeactivity_welcome);
        tv.setText("Bienvenido " + (isFemm ? "sra. " : "Sr. ") + name);
    }

}
