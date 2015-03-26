package com.globallogic.zoo.custom.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.globallogic.zoo.R;

import java.util.Random;

/**
 * Created by GL on 25/03/2015.
 */
public class FavoriteView extends LinearLayout implements View.OnClickListener {

    private TextView tvFavorite;
    private ImageView ivStar;
    private View rootView;

    private FavoriteViewCallback callback;

    private boolean displayText;
    private boolean favoriteState;
    private String meGusta;
    private String noMeGusta;
    private int actualBackgroundColor;

    public FavoriteView(Context context) {
        super(context);
        init();
    }

    public FavoriteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupAttributes(attrs);
        init();
    }

    public FavoriteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupAttributes(attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.activity_favorite, this);
        this.setOnClickListener(this);

        tvFavorite = (TextView) findViewById(R.id.favactivity_text);
        ivStar = (ImageView) findViewById(R.id.favactivity_img);
        rootView = tvFavorite.getRootView();

        if (meGusta == null) {
            meGusta = getResources().getString(R.string.favactivity_megusta);
        }
        if (noMeGusta == null) {
            noMeGusta = getResources().getString(R.string.favactivity_nomegusta);
        }

        if (displayText) {
            tvFavorite.setVisibility(VISIBLE);
        } else {
            tvFavorite.setVisibility(INVISIBLE);
        }
    }

    private void setupAttributes(AttributeSet attrs) {

        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.FavoriteView, 0, 0);

        try {
            displayText = a.getBoolean(R.styleable.FavoriteView_displayText, true);
            meGusta = a.getString(R.styleable.FavoriteView_is_favorite_text);
            noMeGusta = a.getString(R.styleable.FavoriteView_is_not_favorite_text);
        } finally {
            a.recycle();
        }
    }

    public void setFavoriteState(boolean favoriteState) {
        this.favoriteState = favoriteState;
        changeViewState();
    }

    private void changeViewState() {
        if (!favoriteState) {
            rootView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            ivStar.setImageResource(android.R.drawable.star_off);
            tvFavorite.setText(noMeGusta);
        } else {
            actualBackgroundColor = randomColor();
            rootView.setBackgroundColor(actualBackgroundColor);
            ivStar.setImageResource(android.R.drawable.star_on);
            tvFavorite.setText(meGusta);
        }
    }

    private int randomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    @Override
    public void onClick(View v) {
        this.setFavoriteState(!isFavoriteState());
        callback.callbackCall(favoriteState, actualBackgroundColor);
        Log.d("fav_view", String.valueOf(actualBackgroundColor));
    }


    public boolean isFavoriteState() {
        return favoriteState;
    }

    public void setCallback(FavoriteViewCallback callback) {
        this.callback = callback;
    }
}