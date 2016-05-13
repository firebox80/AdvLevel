package com.example.maxim.quicksearch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Animation.AnimationListener {

    static final String SEARCH_START = "SEARCH_START";
    private final String SEARCH_END = "SEARCH_END";
    final static String SEARCH_RESULT = "package com.example.maxim.quicksearch";
    private final String LIST_FILE = "listDataFile";
    private final String FILE_QUANTITY = "FILE_QUANTITY";
    private final String FILE_FOUND = "FILE_FOUND";
    private String findFile = "NOT_FOUND";

    private Animation animation;

    private EditText etNameFile;
    private Button btnFind;
    private Button btnShow;
    private TextView tvQuantityFiles;
    private TextView tvName;
    private ImageView ivProgressBar;

    private Intent intentService;

    private IntentFilter filterReceiverFind;
    private ReceiverTextView receiverFind;

    private boolean isFind;
    private boolean isStop;

    private int quantity;

    private ArrayList<Data> listDataFile = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filterReceiverFind = new IntentFilter(SEARCH_RESULT);
        receiverFind = new ReceiverTextView();
        registerReceiver(receiverFind, filterReceiverFind);

        intentService = new Intent(this, ServiceFind.class);

        animation = AnimationUtils.loadAnimation(this, R.anim.myanimation);

        etNameFile = (EditText) findViewById(R.id.editText);
        ivProgressBar = (ImageView) findViewById(R.id.imageView);
        tvQuantityFiles = (TextView) findViewById(R.id.tvQuantity);
        tvName = (TextView) findViewById(R.id.tvName);
        btnFind = (Button) findViewById(R.id.button);
        btnShow = (Button) findViewById(R.id.button2);

        if(ServiceFind.isServise){
            btnFind.setText("Stop");
            ivProgressBar.startAnimation(animation);
            isFind = true;
        }
        btnFind.setOnClickListener(this);
        btnShow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                if(isFind){
                    isFind = false;
                    btnFind.setText("Find");
                    ivProgressBar.clearAnimation();
                    ServiceFind.isStop = false;
                    stopService(intentService);
                } else if(!etNameFile.getText().toString().equals("")) {
                    isFind = true;
                    btnFind.setText("Stop");
                    ivProgressBar.startAnimation(animation);
                    ServiceFind.isStop = true;
                    String nameFile = etNameFile.getText().toString();
                    intentService.putExtra(SEARCH_START, nameFile);
                    startService(intentService);
                    btnShow.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.button2:
                Intent i = new Intent(this, NotifiedActivity.class);
                i.putExtra(LIST_FILE, listDataFile);
                startActivity(i);
                break;
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {
        Toast.makeText(this, "Нажата кнопка Cancel", Toast.LENGTH_LONG).show();
        ivProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAnimationEnd(Animation animation) {
//        ivProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        ServiceFind.isActivity = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        ServiceFind.isActivity = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(receiverFind);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public class ReceiverTextView extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            isStop = intent.getBooleanExtra(SEARCH_END, true);
            quantity = intent.getIntExtra(FILE_QUANTITY, 0);
            findFile = intent.getStringExtra(FILE_FOUND);

            tvQuantityFiles.setText(String.valueOf(quantity));
            tvName.setText(String.valueOf(findFile));

            if(!isStop){
                isFind = false;
                btnFind.setText("Find");
                ivProgressBar.clearAnimation();

                listDataFile = (ArrayList<Data>) intent.getSerializableExtra(LIST_FILE);
                if(listDataFile.size()!=0) {
                    btnShow.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}