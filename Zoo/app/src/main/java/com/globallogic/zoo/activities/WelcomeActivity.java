package com.globallogic.zoo.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.globallogic.zoo.R;
import com.globallogic.zoo.adapters.AnimalAdapter;
import com.globallogic.zoo.adapters.callbacks.AnimalAdapterCallback;
import com.globallogic.zoo.models.Animal;


public class WelcomeActivity extends ActionBarActivity implements AnimalAdapterCallback {
    final static String USERK = "USER";
    static final String ANIMAL = "ANIMAL";

    private Button signout;
    private TextView welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        bindViews();

        Intent intent = getIntent();
        String name = intent.getStringExtra(USERK);

        welcome.setText(makeWelcomeMessage(name));

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.welcomeactivity_recycleview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        AnimalAdapter mAnimalAdapter = new AnimalAdapter(this, this);

        mRecyclerView.setAdapter(mAnimalAdapter);
    }

    private String makeWelcomeMessage(String name) {
        return String.format(getResources().getString(R.string.welcomeactivity_welcome), name);
    }

    private void bindViews() {
        signout = (Button) findViewById(R.id.welcomeactivity_signout);
        welcome = (TextView) findViewById(R.id.welcomeactivity_welcome);
    }

    @Override
    public void onClick(Animal animal) {
        Intent intent = new Intent(this, AnimalDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(AnimalDetailsActivity.ANIMAL, animal);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
