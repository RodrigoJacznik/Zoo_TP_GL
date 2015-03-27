package com.globallogic.zoo.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.globallogic.zoo.R;
import com.globallogic.zoo.adapters.AnimalAdapter;


public class WelcomeActivity extends ActionBarActivity {
    final static String USERK = "USER";
    final static String IS_FEMM = "IS_FEMM";
    static final String ANIMAL = "ANIMAL";

    private Button signout;
    private TextView welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        bindViews();

        Intent intent = getIntent();
        Boolean isFemm = intent.getBooleanExtra(IS_FEMM, true);
        String name = intent.getStringExtra(USERK);

        String welcomeMessage = makeWelcomeMessage(isFemm, name);
        welcome.setText(welcomeMessage);

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.welcomeactivity_recycleview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        AnimalAdapter mAnimalAdapter = new AnimalAdapter(this);

        mRecyclerView.setAdapter(mAnimalAdapter);
    }

    private String makeWelcomeMessage(Boolean isFemm, String name) {
        Resources res = getResources();
        String sra = res.getString(R.string.welcomeactivity_sra);
        String sr = res.getString(R.string.welcomeactivity_sr);

        return String.format(res.getString(R.string.welcomeactivity_welcome),
                isFemm ? sra : sr, name);
    }

    private void bindViews() {
        signout = (Button) findViewById(R.id.welcomeactivity_signout);
        welcome = (TextView) findViewById(R.id.welcomeactivity_welcome);
    }
}
