package com.globallogic.zoo.custom.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.globallogic.zoo.R;

/**
 * Created by GL on 25/03/2015.
 */
public class FavView extends LinearLayout implements View.OnClickListener{

    private boolean displayText;

    private TextView tvFavorite;
    private ImageView ivStar;

    private boolean favoriteState;
    private String meGusta;
    private String noMeGusta;

    public FavView(Context context) {
        super(context);
        init();
    }

    public FavView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupAttributes(attrs);
        init();
    }

    public FavView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupAttributes(attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.activity_fav, this);
        this.setOnClickListener(this);

        tvFavorite = (TextView) findViewById(R.id.favactivity_text);
        ivStar = (ImageView) findViewById(R.id.favactivity_img);

        meGusta = getResources().getString(R.string.favactivity_megusta);
        noMeGusta = getResources().getString(R.string.favactivity_nomegusta);

        if (displayText) {
            tvFavorite.setVisibility(VISIBLE);
        } else {
            tvFavorite.setVisibility(INVISIBLE);
        }
    }

    private void setupAttributes(AttributeSet attrs) {

        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.FavView, 0, 0);

        try {
            displayText = a.getBoolean(R.styleable.FavView_displayText, true);
        } finally {
            a.recycle();
        }
    }

    public boolean isFavoriteState() {
        return favoriteState;
    }

    public void setFavoriteState(boolean favoriteState) {
        this.favoriteState = favoriteState;
        if (!favoriteState) {
            ivStar.setImageResource(android.R.drawable.star_off);
            tvFavorite.setText(meGusta);
        } else {
            ivStar.setImageResource(android.R.drawable.star_on);
            tvFavorite.setText(noMeGusta);
        }
    }

    @Override
    public void onClick(View v) {
        this.setFavoriteState(!isFavoriteState());
    }

}