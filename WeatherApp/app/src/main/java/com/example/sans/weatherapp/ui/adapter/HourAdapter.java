package com.example.sans.weatherapp.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sans on 24/7/16.
 */
public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder> {

    @Override
    public HourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(HourViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class HourViewHolder extends RecyclerView.ViewHolder{

        public HourViewHolder(View itemView) {
            super(itemView);
        }
    }
}
