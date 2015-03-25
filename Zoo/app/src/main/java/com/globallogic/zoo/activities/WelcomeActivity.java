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
    static final String ANIMAL = "ANIMAL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button signout = (Button) findViewById(R.id.welcomeactivity_signout);
        TextView tv = (TextView) findViewById(R.id.welcomeactivity_welcome);

        Resources res = getResources();

        Intent intent = getIntent();
        Boolean isFemm = intent.getBooleanExtra(MainActivity.IS_FEMM, true);
        String name = intent.getStringExtra(MainActivity.USERK);

        String sra = res.getString(R.string.welcomeactivity_sra);
        String sr = res.getString(R.string.welcomeactivity_sr);

        String welcome = String.format(res.getString(R.string.welcomeactivity_welcome),
                isFemm ? sra : sr, name);
        tv.setText(welcome);

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
}
