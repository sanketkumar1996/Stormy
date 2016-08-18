package com.example.sans.weatherapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sans.weatherapp.R;
import com.example.sans.weatherapp.weather.Day;

/**
 * Created by sans on 16/7/16.
 */
public class DayAdapter extends BaseAdapter {
    public DayAdapter(Context context, Day[] days) {
        mContext = context;
        mDays = days;
    }

    private Context mContext;
    private Day[] mDays;
    @Override
    public int getCount() {
        return mDays.length;
    }

    @Override
    public Object getItem(int position) {
        return mDays[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.daily_list_item,null);
            holder=new ViewHolder();
            holder.iconImageView= (ImageView) convertView.findViewById(R.id.iconImageView);
            holder.temperatureLabel=(TextView)convertView.findViewById(R.id.temperatureLabel);
            holder.dayLabel=(TextView)convertView.findViewById(R.id.dayNameLabel);
            convertView.setTag(holder);
        }
        else{
            holder=(ViewHolder)convertView.getTag();
        }
        Day day=mDays[position];

        holder.iconImageView.setImageResource(day.getIconId());
        holder.temperatureLabel.setText(day.getTemperatureMax() + "");
        holder.dayLabel.setText(day.getDayOfTheWeek());



        return convertView;

    }
    private static class ViewHolder{
        ImageView iconImageView;
        TextView dayLabel;
        TextView  temperatureLabel;
    }
}
