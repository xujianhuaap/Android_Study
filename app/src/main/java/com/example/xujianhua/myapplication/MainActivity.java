package com.example.xujianhua.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import com.example.app.BookOuterClass;

import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.content);

        BookOuterClass.Book.Builder book = BookOuterClass.Book.newBuilder();
        book.setName("战争与和平");
        book.setId(001);
        book.setDesc("世界没有和平");
        BookOuterClass.Book b = book.build();
        textView.setText("");

//        AtomicInteger integer = new AtomicInteger(0);
//        for(int i =0;i<300;i++){
//
//            final int hash = integer.getAndAdd(0x61c88647*2);
//            String s = Integer.toBinaryString(hash);
//            if(s.length()>12){
//                Log.d("atom",""+ s.substring(s.length()-12,s.length()-8)+"\t"+s.substring(s.length()-8,s.length()-4)+"\t"+s.substring(s.length()-4,s.length()) );
//            }
//
//        }
//        for(int i =0;i<200;i++){
//
//            final int hash = integer.getAndAdd(0x61c88647*2);
//            Log.d("atom",""+(hash & 255) );
//        }
//        Integer[] arr = new Integer[20];
//        Log.d(MainActivity.class.getName(),"arr len:"+arr.length);
//        arr[0]=1;
//        for(Integer n :arr){
//            Log.d(MainActivity.class.getName(),""+(n == null));
//        }

    }
}
