package com.flatlyapps.materialbudget.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.flatlyapps.materialbudget.R;

/**
 * Created by Paul on 05/07/2015.
 */

public class RecurListAdapter extends RecyclerView.Adapter<RecurListAdapter.ViewHolder> {

    private final Context context;

    public RecurListAdapter(Context context) {
        this.context = context;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public final FrameLayout rootView;

        public ViewHolder(FrameLayout rootView) {
            super(rootView);
            this.rootView = rootView;
        }
    }

    @Override
    public RecurListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        FrameLayout rootView = (FrameLayout) LayoutInflater.from(context)
                .inflate(R.layout.recur_item_layout, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView listTitle = (TextView) holder.rootView.findViewById(R.id.item_title_view);
        listTitle.setText("recur position: " + position);
        holder.rootView.setOnClickListener(new ItemClickListener(position));

    }

    @Override
    public int getItemCount() {
        return 100;
    }

    private class ItemClickListener implements View.OnClickListener {

        private final int position;

        ItemClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
        }
    }

}
