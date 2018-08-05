package com.lectopia.forward.myimage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    int index = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView imageView = (ImageView)findViewById(R.id.imageView);
        final int[] imageArray = {R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4, R.drawable.a5, R.drawable.a6, R.drawable.a7, R.drawable.a8};


        imageView.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent event) {
               if(event.getAction() == MotionEvent.ACTION_DOWN) {
                   int x = (int)event.getX();
                   int imgLocation = (int)imageView.getWidth()/2;
                   if( x > imgLocation) {
                       if(index != imageArray.length -1){
                           ++index;
                       }else{
                           index = 0;
                       }
                   }else if(x <= imgLocation){
                       if(index != 0){
                           --index;
                       }else{
                           index = imageArray.length -1;
                       }
                   }
                   imageView.setImageResource(imageArray[index]);
               }
               return true;
            }
        });
    }
}

