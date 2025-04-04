package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.helpers.ImageHelperActivity;
import com.example.myapplication.image.FlowerClassificationActivity;
import com.example.myapplication.image.ImageClassificationActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void imageSelection(View view){
        Intent intent =new Intent(this, ImageClassificationActivity.class);
        startActivity(intent);
    }
    public void flowerSelection(View view){
        Intent intent =new Intent(this, FlowerClassificationActivity.class);
        startActivity(intent);
    }

}
