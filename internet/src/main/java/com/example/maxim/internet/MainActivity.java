package com.example.maxim.internet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import models.Region;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener {

    private Spinner sp;
    private EditText et;
    private RadioGroup rg;
    private RadioButton rb1;
    private RadioButton rb2;
    private Button btn;
    private ListView lv;
    private ArrayAdapter<String> adapterSp;
    private ArrayAdapter<String> adapterLv;

    String[] region = { "Киевская", "Львовская", "Одесская" };
    String[] names = { "Иван", "Марья", "Петр", "Антон", "Даша", "Борис",
            "Костя", "Игорь", "Анна", "Денис", "Андрей" };
    private ArrayList<Region> regionList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String response =null;
                InputStream is = null;
                try {
                    String myurl = API.getInfo();
                    URL url = new URL("http://petitions-test.dev.gns-it.com/api/get-info");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    // Starts the query
                    conn.connect();
                    int responseCode = conn.getResponseCode();

                    if (responseCode != 200) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Problem", Toast.LENGTH_LONG).show();
                            }
                        });
                        return;
                    }
                    is = conn.getInputStream();

                    InputStreamReader isr = new InputStreamReader(is);
                    final StringBuilder sb = new StringBuilder();
                    BufferedReader br = new BufferedReader(isr);
                    String read = null;

                    try {
                        read = br.readLine();

                        while (read != null) {
                            //System.out.println(read);
                            sb.append(read);
                            read = br.readLine();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        isr.close();
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, sb.toString(), Toast.LENGTH_LONG).show();
                        }
                    });

                    JSONObject jsonResponse = new JSONObject(sb.toString());
                    int serverResponseCode = jsonResponse.optInt("code");
                    JSONArray jsonRejions = jsonResponse.optJSONArray("regions");

                    for (int i=0; i<jsonRejions.length(); i++){
                        JSONObject region = jsonRejions.optJSONObject(i);

                        String id = region.optString("id");
                        String name_ua = region.optString("name_ua");
                        String name_ru = region.optString("name_ru");
                        String name_en = region.optString("name_en");
                        JSONObject council = region.optJSONObject("council");

                        Region regionJS = new Region(id, name_ua, name_ru, name_en);
                        regionList.add(regionJS);
                    }
                }
                catch(final Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();

        adapterSp = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, region);
        adapterLv = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, names);

        sp = (Spinner) findViewById(R.id.spinner);
        sp.setAdapter(adapterSp);
        sp.setPrompt("Title");

        et = (EditText) findViewById(R.id.editText);
        rg = (RadioGroup) findViewById(R.id.radioGroup);
        rb1 = (RadioButton) findViewById(R.id.radioButton);
        rb2 = (RadioButton) findViewById(R.id.radioButton2);
        btn = (Button) findViewById(R.id.button);

        lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(adapterLv);

        sp.setOnItemSelectedListener(this);
        rg.setOnCheckedChangeListener(this);
        btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case -1:
//                tvInfo.setText("");
                break;
            case R.id.radioButton:
//                rb1.setText("Подумайте ещё раз");
                break;
            case R.id.radioButton2:
//                rb2.setText("Отличный выбор!");
                break;
        }

    }
}
