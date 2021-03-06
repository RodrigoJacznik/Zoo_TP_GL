package com.globallogic.zoo.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.globallogic.zoo.R;
import com.globallogic.zoo.helpers.AnimalHelper;

/**
 * Created by GL on 01/04/2015.
 */
public class ActionModeCallback implements ActionMode.Callback {

    private AnimalAdapter animalAdapter;
    private ActionMode actionMode;
    private Context context;

    public ActionModeCallback(AnimalAdapter animalAdapter, Context context) {
        this.animalAdapter = animalAdapter;
        this.context = context;
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        MenuInflater inflater = actionMode.getMenuInflater();
        inflater.inflate(R.menu.animal_list_context_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cm_welcome_delete:
                animalAdapter.remove();
                actionMode.finish();
                return true;
            case R.id.cm_welcome_share:
                Intent shareIntent = AnimalHelper.getShareAnimalIntent(animalAdapter.getItem());
                context.startActivity(shareIntent);
                actionMode.finish();
                return true;
            default:
                return false;
        }
    }

    public void finishActionMode() {
        actionMode.finish();
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        animalAdapter.setViewHoldersPressedState(false);
        this.actionMode = null;
    }

    public void setActionMode(ActionMode actionMode) {
        this.actionMode = actionMode;
    }

    public ActionMode getActionMode() {
        return actionMode;
    }

    public void setShareIconInvisible(Boolean bool) {
        Menu menu = actionMode.getMenu();
        MenuItem share = menu.findItem(R.id.cm_welcome_share);
        share.setVisible(bool);
    }
}

