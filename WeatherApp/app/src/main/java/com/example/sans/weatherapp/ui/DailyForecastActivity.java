package com.example.sans.weatherapp.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sans.weatherapp.R;
import com.example.sans.weatherapp.ui.adapter.DayAdapter;
import com.example.sans.weatherapp.weather.Day;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DailyForecastActivity extends ListActivity{
    @BindView(android.R.id.list)ListView mListView;
    @BindView(android.R.id.empty) TextView mTextView;

    private Day[] mDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);
        ButterKnife.bind(this);

        Intent intent=getIntent();
        Parcelable[] parcelables=intent.getParcelableArrayExtra(MainActivity.DAILY_FORECAST);
        mDays= Arrays.copyOf(parcelables,parcelables.length,Day[].class);
        DayAdapter adapter=new DayAdapter(DailyForecastActivity.this, mDays);

    }
}
