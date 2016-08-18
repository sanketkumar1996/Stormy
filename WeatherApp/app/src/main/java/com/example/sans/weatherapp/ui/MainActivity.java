package com.example.sans.weatherapp.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sans.weatherapp.R;
import com.example.sans.weatherapp.ui.adapter.HourlyForecastActivity;
import com.example.sans.weatherapp.weather.Current;
import com.example.sans.weatherapp.weather.Day;
import com.example.sans.weatherapp.weather.Forecast;
import com.example.sans.weatherapp.weather.Hour;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final String TAG= MainActivity.class.getSimpleName();
    //public static final String DAILY_FORECAST="DAILY_FORECAST";

    public static final String DAILY_FORECAST= "Daily_Forecast";
    private static final String HOURLY_FORECAST= "Hourly_Forecast";
    @BindView(R.id.timeLabel) TextView mTimeLabel;
    @BindView(R.id.temperatureLabel) TextView mTemperatureLabel;
    @BindView(R.id.humidityValue) TextView mHumidityValue;
    @BindView(R.id.precipValue) TextView mPrecipValue;
    @BindView(R.id.summaryLabel) TextView mSummaryLabel;
    @BindView(R.id.locationLabel)TextView mLocationLabel;
    @BindView(R.id.iconImageView) ImageView mIconImageView;
    @BindView(R.id.refreshImageView)ImageView mRefreshImageView;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;
    @BindView(R.id.dailyButton) Button mDailyButton;
    @BindView(R.id.hourlyButton) Button mHourlyButton;

    private Forecast mForecast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        final double latitude=9.9312;
        final double longitude=76.2673;
        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast(latitude, longitude);
                toggleRefresh();

            }
        });
        mDailyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,DailyForecastActivity.class);
                intent.putExtra(DAILY_FORECAST, mForecast.getDayForecast());
                startActivity(intent);
            }
        });

        mHourlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), HourlyForecastActivity.class);
                intent.putExtra(HOURLY_FORECAST, mForecast.getHourlyForecast());
                startActivity(intent);
            }
        });





        getForecast(latitude, longitude);



    }

    private void toggleRefresh() {
        if(mProgressBar.getVisibility()==View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        }
        else{
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }

    private void getForecast(double latitude, double longitude) {
        String apiKey="7c7c9b4825a293ec086f04142a50b370";
        String forecastUrl="https://api.forecast.io/forecast/"+apiKey+"/"+latitude+","+longitude;
        if(networkIsAvailable()) {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(forecastUrl).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                toggleRefresh();
                            }
                        });

                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mForecast = parseForecastDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });


                        } else {
                            alertUserAboutError();
                        }

                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });

        }
        else{
            Toast.makeText(getApplicationContext(), R.string.network_message, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateDisplay() {
        mTemperatureLabel.setText(mForecast.getCurrent().getTemperature()+"");
        mSummaryLabel.setText(mForecast.getCurrent().getSummary());
        mHumidityValue.setText(mForecast.getCurrent().getHumidity()+"");
        mTimeLabel.setText("At " + mForecast.getCurrent().getFormattedTime() + " it will be");
        mPrecipValue.setText(mForecast.getCurrent().getPrecipChance() + " %");
        Drawable drawable=getResources().getDrawable(mForecast.getCurrent().getIconId());
        mLocationLabel.setText(mForecast.getCurrent().getTimezone());
        mIconImageView.setImageDrawable(drawable);

       // Toast.makeText(getApplicationContext(),R.string.error_message,Toast.LENGTH_SHORT).show();

    }
    private Forecast parseForecastDetails(String jsonData)throws JSONException{
        Forecast forecast=new Forecast();

        forecast.setCurrent(getForercastInfo(jsonData));
        forecast.setDayForecast(getDailyInfo(jsonData));
        forecast.setHourlyForecast(getHourlInfo(jsonData));

        return forecast;
    }

    private Hour[] getHourlInfo(String jsonData) throws JSONException{
        JSONObject forecast=new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject hourly=forecast.getJSONObject("hourly");
        JSONArray data=hourly.getJSONArray("data");

        Hour[] hours=new Hour[data.length()];
        for(int i=0;i<data.length();i++){
            JSONObject jsonObject=data.getJSONObject(i);
            Hour hour=new Hour();
            hour.setTime(jsonObject.getLong("time"));
            hour.setIcon(jsonObject.getString("icon"));
            hour.setTemperature(jsonObject.getDouble("temperature"));
            hour.setSummary(jsonObject.getString("summary"));
            hour.setTimezone(timezone);
            hours[i]=hour;
        }
        return hours;


    }

    private Day[] getDailyInfo(String jsonData) throws JSONException{
        JSONObject forecast=new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject hourly=forecast.getJSONObject("daily");
        JSONArray data=hourly.getJSONArray("data");
        Day[] days=new Day[data.length()];
        for(int i=0;i<data.length();i++){
            JSONObject jsonObject=data.getJSONObject(i);
            Day day=new Day();
            day.setIcon(jsonObject.getString("icon"));
            day.setTime(jsonObject.getLong("time"));
            day.setTemperatureMax(jsonObject.getDouble("temperatureMax"));
            day.setSummary(jsonObject.getString("summary"));
            day.setTimezone(timezone);

            days[i]=day;
        }

        return days;

    }

    private Current getForercastInfo(String jsonData)throws JSONException{

        JSONObject forecast=new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        Log.v(TAG, "LOCATION FROM JSON:" + timezone);
        JSONObject currenWeather=forecast.getJSONObject("currently");

        Current currently=new Current();
        currently.setHumidity(currenWeather.getDouble("humidity"));
        currently.setIcon(currenWeather.getString("icon"));
        currently.setPrecipChance(currenWeather.getDouble("precipProbability"));
        Log.v(TAG, String.valueOf(currently.getPrecipChance()));
        currently.setSummary(currenWeather.getString("summary"));
        currently.setTemperature(currenWeather.getDouble("temperature"));
        currently.setTime(currenWeather.getLong("time"));
        currently.setTimezone(timezone);

        Log.v(TAG, currently.getFormattedTime());


        return currently;

    }


    private boolean networkIsAvailable() {
        ConnectivityManager manager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=manager.getActiveNetworkInfo();
        boolean networkAvailable=false;
        if(networkInfo!=null && networkInfo.isConnected()){
            networkAvailable=true;
        }
        return networkAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog=new AlertDialogFragment();
        dialog.show(getFragmentManager(),"errorDialog");
    }




}
