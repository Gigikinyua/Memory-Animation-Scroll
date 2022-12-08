package com.jemimah.memory_timeline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.jemimah.memory_timeline.adapter.CustomAdapter;
import com.jemimah.memory_timeline.model.DataModel;
import com.jemimah.memory_timeline.view.DrawView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DrawView drawView;
    Toolbar toolbar;
    LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<DataModel> dataModels = new ArrayList<>();

        dataModels.add(new DataModel(1, "Android 1.0", "23 January 2022"));
        dataModels.add(new DataModel(2, "Android 1.1", "15 March 2022"));
        dataModels.add(new DataModel(3, "Android 1.5", "6 July 2022"));
        dataModels.add(new DataModel(4, "Android 1.5", "15 September 2022"));
        dataModels.add(new DataModel(5, "Android 1.5", "1 November 2022"));

        CustomAdapter adapter = new CustomAdapter(dataModels, getApplicationContext());

        drawView = new DrawView(this, adapter, container);
        container.addView(drawView);

    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        container = findViewById(R.id.viewer);
    }

    @Override
    public boolean onSupportNavigateUp() {
//        onBackPressed();
        return false;
    }

}