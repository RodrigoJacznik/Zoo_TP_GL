package com.globallogic.zoo.asynctask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.LongSparseArray;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.globallogic.zoo.R;
import com.globallogic.zoo.helpers.HttpConnectionHelper;

/**
 * Created by rodrigo on 4/12/15.
 */
public class FetchImgTask extends AsyncTask<String, Void, Void> {

    private static LongSparseArray<Bitmap> cache = new LongSparseArray<>();

    private ImageView imageView;
    private Bitmap drawable;
    private ProgressBar load;
    private Context context;

    private long id;

    public FetchImgTask(ImageView imageView, Context context) {
        super();
        this.imageView = imageView;
        this.context = context;
    }

    public FetchImgTask(ImageView imageView, long id, ProgressBar progressBar, Context context) {
        this(imageView, context);
        this.id = id;
        this.load = progressBar;
    }

    @Override
    protected Void doInBackground(String... url) {
        if (drawable == null) {
            drawable = HttpConnectionHelper.fetchImg(context, url[0]);
            cache.append(id, drawable);
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        load.setVisibility(View.VISIBLE);
        drawable = cache.get(id);
        if (drawable != null) {
            this.cancel(true);
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        completeViewSetup();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        completeViewSetup();
    }

    private void completeViewSetup() {
        load.setVisibility(View.INVISIBLE);
        Animation fadeIn = AnimationUtils.loadAnimation(context, R.anim.fadein);
        imageView.startAnimation(fadeIn);
        imageView.setImageBitmap(drawable);
        imageView.setAdjustViewBounds(true);
    }
}
