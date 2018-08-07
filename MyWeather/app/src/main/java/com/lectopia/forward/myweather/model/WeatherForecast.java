package com.lectopia.forward.myweather.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherForecast {
    public final List<Forecast> forecastList = new ArrayList<>(); // 한지역의 json데이터가 40개의 json객체로 구성

    public WeatherForecast(JSONObject json) throws JSONException{ // 전달 받은
        JSONArray array = json.getJSONArray("list");
        //array값을 list로 이동
        int len = array.length();
        for(int i = 0 ; i < len; ++i) {
            JSONObject jsonObj = array.getJSONObject(i);
            Forecast forecast = new Forecast(jsonObj);
            forecastList.add(forecast);
        }
    }


}
