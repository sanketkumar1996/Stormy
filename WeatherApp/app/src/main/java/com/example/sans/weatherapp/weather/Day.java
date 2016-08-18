package com.example.sans.weatherapp.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by sans on 16/7/16.
 */
public class Day implements Parcelable {
    private long mTime;
    private double mTemperatureMax;
    private String mIcon;
    private String mSummary;
    private String mTimezone;

    public String getTimezone() {
        return mTimezone;
    }

    public void setTimezone(String timezone) {
        mTimezone = timezone;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public int getTemperatureMax() {
        return (int) Math.round(mTemperatureMax);
    }

    public void setTemperatureMax(double temperatureMax) {
        mTemperatureMax = temperatureMax;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String timezone) {
        mSummary = timezone;
    }

    public int getIconId() {
        return Forecast.getIconId(mIcon);

    }

    public String getDayOfTheWeek() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(mTimezone));
        Date date = new Date(mTime * 1000);
        String time = simpleDateFormat.format(date);
        return time;
    }

    @Override
    public int describeContents() {
        return 0;//not used
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) { //wraps up the data
        dest.writeLong(mTime);
        dest.writeString(mSummary);
        dest.writeDouble(mTemperatureMax);
        dest.writeString(mIcon);
        dest.writeString(mTimezone);
    }

    private Day(Parcel in) {
        mTime = in.readLong();
        mSummary = in.readString();
        mTemperatureMax = in.readDouble();
        mIcon = in.readString();
        mTimezone = in.readString();
    }

    public static final Creator<Day> CREATOR = new Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel source) {
            return new Day(source);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };
}
