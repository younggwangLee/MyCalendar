package com.lectopia.forward.myweather.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Forecast {
    public final String dt_txt;
    public final Main main;
    public final Weather weather;

    public Forecast(JSONObject json) throws JSONException{
        dt_txt = json.getString("dt_txt");
        main = new Main(json.getJSONObject("main"));
        weather = new Weather(json.getJSONArray("weather"));
    }

    public class Main {
        public final String temp;
        public final String humidity;
        public Main(JSONObject json)throws JSONException{
            temp = json.getString("temp");
            humidity = json.getString("humidity");
        }
    }

    public class Weather{
        public final String main;
        public final String description;
        public final String icon;

        public Weather(JSONArray jsonArray)throws JSONException{
            main = jsonArray.getJSONObject(0).getString("main");
            description = jsonArray.getJSONObject(0).getString("description");
            icon = jsonArray.getJSONObject(0).getString("icon");
        }
    }
}
