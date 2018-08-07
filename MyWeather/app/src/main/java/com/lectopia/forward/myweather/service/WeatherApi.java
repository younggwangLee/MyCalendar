package com.lectopia.forward.myweather.service;

import android.content.Context;
import android.util.Log;

import com.lectopia.forward.myweather.model.WeatherForecast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherApi {
    //bb4186a5b540d14f1f0ea95def1c23d2
    //https://api.openweathermap.org/data/2.5/forecast?&appid=bb4186a5b540d14f1f0ea95def1c23d2&units=metric&q=seoul
    static final String apiId = "bb4186a5b540d14f1f0ea95def1c23d2";
    public static final String URL_STR="https://api.openweathermap.org/data/2.5/forecast"
            + "?appid="+ apiId
            + "&units=metric"
            +"&q=";
    public static WeatherForecast getWeather(Context context,String location) throws IOException, JSONException{
        URL url = new URL(URL_STR+(location == null ? "seoul" : location));
        HttpURLConnection conn = null;

        StringBuilder sb = new StringBuilder();
        try{
            // Connection -> create Json -> return weatherForecast
            conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int responseCode = conn.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                InputStreamReader isr = new InputStreamReader(conn.getInputStream());
                BufferedReader bfr = new BufferedReader(isr);
                String str = null;
                while((str = bfr.readLine())!= null){
                    sb.append(str);
                }
            }else{
                Log.e("WeatherApi error", "URL="+URL_STR+location);
            }

        }catch (Exception e){
            Log.e("WeatherApi error", "URL="+URL_STR+location);
        }finally {
            conn.disconnect();
        }
        return new WeatherForecast(new JSONObject(sb.toString()));
    }
}
