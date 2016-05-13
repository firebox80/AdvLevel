package com.example.maxim.petition;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import models.Categories;
import models.City;
import models.District;
import models.Petition;
import models.Region;
import models.Type;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView lv;
    Adapter ad;
    ArrayList<Petition> listPetition = new ArrayList<>();
    ArrayList<Type> listType = new ArrayList<>();
    static ArrayList<Categories> listCategories = new ArrayList<>();
    ArrayList<City> listCity =new ArrayList<>();
    ArrayList<District> listDistrict = new ArrayList<>();
    ArrayList<Region> listRegion =new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView)findViewById(R.id.listView);
        ad = new Adapter(this, R.layout.item_custom, listPetition);
        lv.setAdapter(ad);
        lv.setOnItemClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){

            case R.id.action_city:
                Toast.makeText(getApplicationContext(), "action_city", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_district:
                Toast.makeText(getApplicationContext(), "action_district", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_region:
                Toast.makeText(getApplicationContext(), "action_region", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
