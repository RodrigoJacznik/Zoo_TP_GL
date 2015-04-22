package com.globallogic.zoo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.globallogic.zoo.R;
import com.globallogic.zoo.helpers.ZooDatabaseHelper;
import com.globallogic.zoo.models.Show;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GL on 16/04/2015.
 */
public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ViewHolder> {

    private List<Show> shows = new ArrayList<>();
    private OnShowClickListener onShowClickListener;

    public interface OnShowClickListener {
        public void onShowClick(long showId);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;
        public ImageView image;
        public long showId;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.show_row_name);
            image = (ImageView) itemView.findViewById(R.id.show_row_image);
            itemView.setOnClickListener(this);
        }

        public void load(Show show) {
            name.setText(show.getName());
            Ion.with(image)
                    .placeholder(R.drawable.android)
                    .error(android.R.drawable.ic_dialog_alert)
                    .load(show.getImageUrl());
            showId = show.getId();
        }

        @Override
        public void onClick(View v) {
            Log.d("ShowAdapter", "click");
            onShowClickListener.onShowClick(this.showId);
        }
    }

    public ShowAdapter(Context context) {
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.load(shows.get(i));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return shows.size();
    }

    public void setShows(List<Show> shows) {
        this.shows = shows;
        notifyDataSetChanged();
    }
}
