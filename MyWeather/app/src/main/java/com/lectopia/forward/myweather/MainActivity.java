package com.lectopia.forward.myweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.lectopia.forward.myweather.model.Forecast;
import com.lectopia.forward.myweather.model.WeatherForecast;
import com.lectopia.forward.myweather.service.ImageViewTask;
import com.lectopia.forward.myweather.service.WeatherApi;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public TextView tvView;
    public LinearLayout forecast_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("simpleWather", "Mainactivity#onCreate called");

        tvView = (TextView) findViewById(R.id.tvView);
        forecast_layout = (LinearLayout)findViewById(R.id.forecast_layout);

        //thread
        MyWeatherTask myTask = new MyWeatherTask(this);
        myTask.execute("Seoul");
    }

    class MyWeatherTask extends AsyncTask<String, Void, WeatherForecast>{
        private final Context context;
        public MyWeatherTask(Context context){
            this.context = context;
        }
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected WeatherForecast doInBackground(String... location) {
            //HttpURLConnection
            try{////////////////////////////여기수정
                return WeatherApi.getWeather(this.context, location[0]);
            }catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(WeatherForecast weather) {
            super.onPostExecute(weather);
            tvView.setText("SEOUL");
            for(Forecast forecast : weather.forecastList){
                View row = View.inflate(MainActivity.this, R.layout.forecast_row, null);

                TextView tvDate = (TextView) row.findViewById(R.id.tvDate);
                TextView tvTemp = (TextView) row.findViewById(R.id.tvTemp);
                TextView tvDesc = (TextView) row.findViewById(R.id.tvDesc);
                ImageView imageView = (ImageView) row.findViewById(R.id.imageView);
                imageView.setTag(forecast.weather.icon);

                SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd(E) HH");

                Date date1 = null;
                String date2 = null;
                try {
                    date1 = dateFormat1.parse(forecast.dt_txt);
                    date2 = dateFormat2.format(date1);
                }catch (Exception e){
                    e.printStackTrace();
                }

                tvDate.setText(date2);
                //tvDate.setText(forecast.dt_txt);
                tvTemp.setText(forecast.main.temp);//유니코드 2103
                tvDesc.setText(forecast.weather.description);
                Log.d("SimpleWeather", forecast.weather.icon);
                //image - 별도의 스레드가 필요
                ImageViewTask imageTask = new ImageViewTask(MainActivity.this);
                imageTask.execute(imageView);

                forecast_layout.addView(row);
            }
        }
    }
}
