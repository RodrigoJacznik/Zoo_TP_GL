package com.globallogic.zoo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.globallogic.zoo.R;
import com.globallogic.zoo.helpers.ZooDatabaseHelper;
import com.globallogic.zoo.models.Show;

import java.util.List;

/**
 * Created by GL on 16/04/2015.
 */
public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ViewHolder> {

    private List<Show> shows;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.show_row_name);
        }

        public void load(Show show) {
            name.setText(show.getName());
        }
    }

    public ShowAdapter(Context context) {
        ZooDatabaseHelper db = new ZooDatabaseHelper(context);
        shows = db.getShows();
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
}
