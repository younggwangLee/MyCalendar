package com.lectopia.forward.mymovie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView tvDirector, tvGenre, tvActors, tvReleased, tvPlot;
    Button btnSearch;
    EditText etTitle;
    ImageView ivPoster;
    String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDirector = (TextView)findViewById(R.id.tvDirector);
        tvGenre = (TextView)findViewById(R.id.tvGenre);
        tvActors = (TextView)findViewById(R.id.tvActor);
        tvReleased = (TextView)findViewById(R.id.tvReleased);
        tvPlot = (TextView)findViewById(R.id.tvPlot);
        etTitle = (EditText)findViewById(R.id.etTitle);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        ivPoster = (ImageView) findViewById(R.id.ivPoster);

        btnSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(etTitle.getText() != null && !etTitle.getText().toString().isEmpty()){
                    MySearchTask mySearchTask = new MySearchTask();
                    mySearchTask.execute();
                }else{
                    Toast.makeText(getApplicationContext(), "타이틀을 입력하세요,", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    class MySearchTask extends AsyncTask<String, Void, JSONObject> {
        String apiKey="bc00b359";
        String str;
        String receiveMsg;

        @Override
        protected JSONObject doInBackground(String... values) {
            try {
                String param = etTitle.getText().toString();
                URL url = new URL("http://www.omdbapi.com/?apikey="+apiKey+"&t="+param);

                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

                if(conn.getResponseCode() == conn.HTTP_OK){
                    InputStreamReader isr = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader br = new BufferedReader(isr);
                    StringBuffer sb = new StringBuffer();
                    while((str = br.readLine())!= null){
                        sb.append(str);
                    }
                    receiveMsg = sb.toString();
                    try {
                        JSONObject movieInfoObject = new JSONObject(receiveMsg);
                        return movieInfoObject;
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    Log.i("msg : ", receiveMsg);
                    br.close();
                }else{
                    Log.i("통신 요청 결과",conn.getResponseCode() + "에러");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(JSONObject movieObject) {
            try {
                tvDirector.setText("감독 : " + movieObject.getString("Director"));
                tvReleased.setText("개봉일 : " + movieObject.getString("Released"));
                tvGenre.setText("장르 : " + movieObject.getString("Genre"));
                tvActors.setText("주연배우 : " + movieObject.getString("Actors"));
                tvPlot.setText("줄거리 : "+ movieObject.getString("Plot"));

                ImageSearch imageSearch = new ImageSearch();
                imageSearch.execute(movieObject.getString("Poster"));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
    class ImageSearch extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.connect();
                connection.setConnectTimeout(10000);
                connection.setRequestMethod("GET");
                InputStream is = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                return bitmap;
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Bitmap image) {
            ivPoster.setImageBitmap(image);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}