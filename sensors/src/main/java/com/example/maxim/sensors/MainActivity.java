package com.example.maxim.sensors;

import android.app.Activity;
import android.app.DialogFragment;
//import android.support.v4.app.DialogFragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener{

    private static final CharSequence MENU_ITEM_GENERAL_INFO = "Все сенсоры";
    private static final CharSequence MENU_ITEM_CHOOSE_SENSOR = "Выбрать сенсор";
    private static final int MENU_ITEM_GENERAL_INFO_ID = 1;
    private static final int MENU_ITEM_CHOOSE_SENSOR_ID = 2;
    private static FragmentManager fragmentManager;
    private static SensorDialog sensorDialog;
    private static SensorManager sensorManager;

    private TextView tvInfo;

    private GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvInfo = (TextView) findViewById(R.id.tv_info);
        fragmentManager = getFragmentManager();

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);


        mDetector = new GestureDetectorCompat(this,this);
        // Set the gesture detector as the double tap
        // listener.
        mDetector.setOnDoubleTapListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_ITEM_GENERAL_INFO_ID, 0, MENU_ITEM_GENERAL_INFO);
        menu.add(0, MENU_ITEM_CHOOSE_SENSOR_ID, 0, MENU_ITEM_CHOOSE_SENSOR);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ITEM_GENERAL_INFO_ID:
                tvInfo.setText("");
                getSensorsGeneralInfo();
                break;
            case MENU_ITEM_CHOOSE_SENSOR_ID:
                tvInfo.setText("");
                sensorDialog = new SensorDialog();
                sensorDialog.show(fragmentManager, "dialog");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onMenuItemSelected(int featureId, MenuItem item) {
//
//        switch (item.getItemId()) {
//            case MENU_ITEM_GENERAL_INFO_ID:
//                tvInfo.setText("");
//                getSensorsGeneralInfo();
//                break;
//            case MENU_ITEM_CHOOSE_SENSOR_ID:
//                tvInfo.setText("");
//                sensorDialog = new SensorDialog();
//                sensorDialog.show(fragmentManager, "dialog");
//                break;
//        }
//        return super.onMenuItemSelected(featureId, item);
//    }

    private void getSensorsGeneralInfo() {
        Toast.makeText(this, "Sensors General Info", Toast.LENGTH_LONG).show();

        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

        for (Sensor sensor : sensors) {
            int fifoMaxEventCount = sensor.getFifoMaxEventCount();
            int fifoReservedEventCount = sensor.getFifoReservedEventCount();
            float maximumRange = sensor.getMaximumRange();
            int minDelay = sensor.getMinDelay();
            String name = sensor.getName();
            float power = sensor.getPower();
            float resolution = sensor.getResolution();
            int type = sensor.getType();
            String vendor = sensor.getVendor();
            int version = sensor.getVersion();
            StringBuilder sb = new StringBuilder("\n");
            sb.append("fifoMaxEventCount : \t" + fifoMaxEventCount + "\n");
            sb.append("fifoReservedEventCount : \t" + fifoReservedEventCount + "\n");
            sb.append("maximumRange : \t" + maximumRange + "\n");
            sb.append("minDelay : \t" + minDelay + "\n");
            sb.append("name : \t" + name + "\n");
            sb.append("power : \t" + power + " mA" + "\n");
            sb.append("resolution : \t" + resolution + "\n");
            sb.append("type : \t" + type + "\n");
            sb.append("vendor : \t" + vendor + "\n");
            sb.append("version : \t" + version + "\n");
            tvInfo.append(sb.toString());
        }
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }
    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }
    @Override
    public void onShowPress(MotionEvent e) {

    }
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }
    @Override
    public void onLongPress(MotionEvent e) {

    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public static class SensorDialog extends DialogFragment {
        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            getDialog().setTitle("Сенсоры:");
            ListView sensorDialogRoot = new ListView(getActivity());
            List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
            String[] sensorNames = new String[sensors.size()];

            for (int i = 0; i < sensors.size(); i++)
                sensorNames[i] = sensors.get(i).getName();

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getActivity(),
                    android.R.layout.simple_list_item_1,
                    sensorNames);

            sensorDialogRoot.setAdapter(adapter);
            sensorDialogRoot.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    sensorDialog.dismiss();
                    Toast.makeText(getActivity(),
                            ((TextView) arg1).getText().toString(),
                            Toast.LENGTH_LONG)
                            .show();
                }
            });
            return sensorDialogRoot;
        }
    }
}