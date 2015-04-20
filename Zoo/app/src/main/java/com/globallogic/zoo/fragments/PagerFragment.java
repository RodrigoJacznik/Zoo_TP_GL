package com.globallogic.zoo.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globallogic.zoo.R;
import com.globallogic.zoo.adapters.PagerAdapter;

/**
 * Created by GL on 20/04/2015.
 */
public class PagerFragment extends Fragment {

    private ViewPager viewPager;

    public PagerFragment() {}

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = (ViewPager) view.findViewById(R.id.welcomeactivity_view_pager);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_pager, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PagerAdapter pagerAdapter = new PagerAdapter(getFragmentManager());
        viewPager.setAdapter(pagerAdapter);
    }
}
