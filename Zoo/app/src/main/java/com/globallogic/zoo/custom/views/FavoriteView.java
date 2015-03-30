package com.globallogic.zoo.custom.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.globallogic.zoo.R;
import com.globallogic.zoo.custom.views.callbacks.FavoriteViewCallback;

import java.util.Random;

/**
 * Created by GL on 25/03/2015.
 */
public class FavoriteView extends LinearLayout {

    private TextView favorite;
    private ImageView star;

    private FavoriteViewCallback callback;

    private boolean displayText;
    private boolean favoriteState;
    private String like;
    private String dontLike;
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

        favorite = (TextView) findViewById(R.id.favactivity_text);
        star = (ImageView) findViewById(R.id.favactivity_img);
        star.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setFavoriteState(!isFavoriteState());
                callback.callbackCall(favoriteState, actualBackgroundColor);
            }
        });

        if (like == null) {
            like = getResources().getString(R.string.favactivity_like);
        }
        if (dontLike == null) {
            dontLike = getResources().getString(R.string.favactivity_dontlike);
        }

        favorite.setVisibility(displayText ? VISIBLE : GONE);
    }

    private void setupAttributes(AttributeSet attrs) {

        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.FavoriteView, 0, 0);

        try {
            displayText = a.getBoolean(R.styleable.FavoriteView_displayText, true);
            like = a.getString(R.styleable.FavoriteView_is_favorite_text);
            dontLike = a.getString(R.styleable.FavoriteView_is_not_favorite_text);
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
            actualBackgroundColor = getResources().getColor(android.R.color.transparent);
            star.setImageResource(R.drawable.state_list_start_off);
            favorite.setText(dontLike);
        } else {
            actualBackgroundColor = randomPastelColor();
            star.setImageResource(R.drawable.state_list_star_on);
            favorite.setText(like);
        }
    }

    private int randomPastelColor() {
        return Color.rgb(randomColor(), randomColor(), randomColor());
    }

    private int randomColor() {
        Random rnd = new Random();
        return rnd.nextInt(127) + 127;
    }

    public boolean isFavoriteState() {
        return favoriteState;
    }

    public void setCallback(FavoriteViewCallback callback) {
        this.callback = callback;
    }
}