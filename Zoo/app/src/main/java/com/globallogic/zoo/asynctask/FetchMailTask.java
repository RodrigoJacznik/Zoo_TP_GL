package com.globallogic.zoo.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.globallogic.zoo.helpers.ContentProviderHelper;

import java.util.List;

/**
 * Created by rodrigo on 4/11/15.
 */
public class FetchMailTask extends AsyncTask<Void, Void, List<String>> {

    OnAsyncTaskListener<List<String>> onAsyncTaskListener;

    public FetchMailTask(OnAsyncTaskListener onAsyncTaskListener) {
        this.onAsyncTaskListener = onAsyncTaskListener;
    }

    @Override
    protected List<String> doInBackground(Void... params) {
        return ContentProviderHelper.getContactsEmails((Context) onAsyncTaskListener);
    }

    @Override
    protected void onPreExecute() {
        onAsyncTaskListener.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<String> emails) {
        onAsyncTaskListener.onPostExecute(emails);
    }
}