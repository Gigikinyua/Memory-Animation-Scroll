package com.jemimah.memory_timeline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.jemimah.memory_timeline.view.DrawView;

public class MainActivity extends AppCompatActivity {

        DrawView drawView;
//    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        initViews();
//
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//        drawView = new DrawView(this);
//        setContentView (drawView);
    }

//    private void initViews() {
//        toolbar = findViewById(R.id.toolbar);
//    }

    @Override
    public boolean onSupportNavigateUp() {
//        onBackPressed();
        return false;
    }

}