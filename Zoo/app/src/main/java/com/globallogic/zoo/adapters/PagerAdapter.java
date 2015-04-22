package com.globallogic.zoo.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.globallogic.zoo.fragments.AnimalListFragment;
import com.globallogic.zoo.fragments.ShowListFragment;


public class PagerAdapter extends FragmentStatePagerAdapter {
    private static int NUM_ITEMS = 2;

    public PagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }


    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AnimalListFragment();
            case 1:
                return new ShowListFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Animales";
            case 1:
                return "Shows";
            default:
                return null;
        }
    }
}
