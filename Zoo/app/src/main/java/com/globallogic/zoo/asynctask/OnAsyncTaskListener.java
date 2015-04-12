package com.globallogic.zoo.asynctask;

/**
 * Created by rodrigo on 4/11/15.
 */
public interface OnAsyncTaskListener<R> {

    public void onPreExecute();
    public void onPostExecute(R r);
}
