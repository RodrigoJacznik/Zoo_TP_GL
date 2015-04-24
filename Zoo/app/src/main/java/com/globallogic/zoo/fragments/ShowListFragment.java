package com.globallogic.zoo.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globallogic.zoo.R;
import com.globallogic.zoo.adapters.ShowAdapter;
import com.globallogic.zoo.data.ShowRepository;
import com.globallogic.zoo.models.Show;
import com.globallogic.zoo.network.API;

import java.util.List;


public class ShowListFragment extends Fragment implements
        API.OnRequestListListener<Show>,
        ShowAdapter.OnShowClickListener {

    public static final String TAG = "ShowListFragment";

    public interface OnShowClickListener {
        public void onShowClick(long showId);
    }

    private ShowAdapter showAdapter;
    private OnShowClickListener onShowClickListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onShowClickListener = (OnShowClickListener) activity;
        } catch (ClassCastException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showAdapter = new ShowAdapter(getActivity());
        showAdapter.setOnShowClickListener(this);
        ShowRepository repository = new ShowRepository(getActivity(), this);
        repository.getAllShows(null);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_show_list, container, false);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.fragment_show_list_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(showAdapter);

        return v;
    }

    @Override
    public void onFail(int code) {

    }

    @Override
    public void onSuccess(List<Show> shows) {
        if (!shows.isEmpty()) {
            showAdapter.setShows(shows);
        }
    }

    @Override
    public void onShowClick(long showId) {
        onShowClickListener.onShowClick(showId);
    }
}
