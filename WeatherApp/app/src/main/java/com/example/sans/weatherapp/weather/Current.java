package com.example.sans.weatherapp.weather;

import com.example.sans.weatherapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by sans on 15/7/16.
 */
public class Current {

    private String mIcon;
    private double mHumidity;
    private long mTime;
    private double mTemperature;
    private double mPrecipChance;
    private String mSummary;
    private String mTimezone;

    public int getPrecipChance() {
        double precipPercentage= mPrecipChance*100;
        return (int)Math.round(precipPercentage);
    }

    public void setPrecipChance(double precipChance) {
        mPrecipChance = precipChance;
    }




    public String getTimezone() {
        return mTimezone;
    }

    public void setTimezone(String timezone) {
        mTimezone = timezone;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }
    public int getIconId(){
        // clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, or partly-cloudy-night.
        return Forecast.getIconId(mIcon);
    }

    public double getHumidity() {
        return mHumidity;
    }

    public void setHumidity(double humidity) {
        mHumidity = humidity;
    }

    public long getTime() {
        return mTime;
    }
    public String getFormattedTime(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("hh:mm a");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(getTimezone()));
        Date date=new Date(getTime()*1000);
        String time=simpleDateFormat.format(date);
        return time;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public int getTemperature() {
        return (int) Math.round(mTemperature);
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }




    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }
}
