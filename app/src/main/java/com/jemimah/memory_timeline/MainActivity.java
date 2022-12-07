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

        dataModels.add(new DataModel("Apple Pie", "Android 1.0", "1", "September 23, 2008"));
        dataModels.add(new DataModel("Banana Bread", "Android 1.1", "2", "February 9, 2009"));
        dataModels.add(new DataModel("Cupcake", "Android 1.5", "3", "April 27, 2009"));
        dataModels.add(new DataModel("Cupcake", "Android 1.5", "3", "April 27, 2009"));
        dataModels.add(new DataModel("Cupcake", "Android 1.5", "3", "April 27, 2009"));

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