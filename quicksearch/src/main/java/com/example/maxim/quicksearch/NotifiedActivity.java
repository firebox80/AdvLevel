package com.example.maxim.quicksearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class NotifiedActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private final String LIST_FILE = "listDataFile";
    private ListView lv;
    private AdapterData ad;
    private ArrayList<Data> currentData = new ArrayList<Data>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notified);

        Intent i = getIntent();
        currentData = (ArrayList<Data>) i.getSerializableExtra(LIST_FILE);

        lv = (ListView) findViewById(R.id.listView);
        ad = new AdapterData(this, R.layout.item_layout_custom, currentData);
        lv.setAdapter(ad);
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
