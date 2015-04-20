package com.globallogic.zoo.fragments;

import android.app.Fragment;
import android.os.Bundle;
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


public class ShowListFragment extends Fragment implements API.OnRequestListListener<Show> {

    public static final String TAG = "ShowListFragment";

    private ShowAdapter showAdapter;

    public ShowListFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        showAdapter = new ShowAdapter(getActivity());
        ShowRepository repository = new ShowRepository(getActivity(), this);
        repository.getAllShows(null);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
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
        if (! shows.isEmpty()) {
            showAdapter.setShows(shows);
        }
    }
}
