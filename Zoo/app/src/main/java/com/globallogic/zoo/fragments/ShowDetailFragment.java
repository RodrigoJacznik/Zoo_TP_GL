package com.globallogic.zoo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.globallogic.zoo.R;
import com.globallogic.zoo.data.ShowRepository;
import com.globallogic.zoo.models.Show;
import com.globallogic.zoo.network.API;
import com.koushikdutta.ion.Ion;

public class ShowDetailFragment extends Fragment implements API.OnRequestObjectListener<Show> {

    public static final String SHOW_ID = "SHOW_ID";
    public static final String TAG = "ShowDetailFragment";

    private Show show;
    private TextView title;
    private TextView description;
    private ImageView image;

    public static ShowDetailFragment newInstance(long showId) {
        ShowDetailFragment fragment = new ShowDetailFragment();
        Bundle args = new Bundle();
        args.putLong(SHOW_ID, showId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long showId = getArguments().getLong(SHOW_ID);

        ShowRepository repository = new ShowRepository(getActivity(), this);
        repository.getShow(showId, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_show_detail, container, false);
        title = (TextView) v.findViewById(R.id.fragmentshowdetail_title);
        description = (TextView) v.findViewById(R.id.fragmentshowdetail_description);
        image = (ImageView) v.findViewById(R.id.fragmentshowdetail_image);
        return v;
    }

    private void fillShowData(Show show) {
        title.setText(show.getName());
        description.setText(show.getDescription());
        Ion.with(image).load(show.getImageUrl());
    }

    @Override
    public void onSuccess(Show show) {
        this.show = show;
        fillShowData(show);
    }

    @Override
    public void onFail(int code) {

    }
}
