package com.example.maxim.googlemap;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    Activity activity;

    private static final String MENU_ITEM_INFO = "Информация";
    private static final String MENU_ITEM_MAP_TYPE = "Тип карты";
    private static final String MENU_ITEM_AREA = "Указать область";
    private static final String MENU_ITEM_GROUNGOVERLAY = "Ground Overlay";
    private static final String MENU_ITEM_CLEAR = "Очистить";

    private static final int MENU_ITEM_INFO_ID = 1;
    private static final int MENU_ITEM_MAP_TYPE_ID = 2;
    private static final int MENU_ITEM_AREA_ID = 3;
    private static final int MENU_ITEM_GROUNGOVERLAY_ID = 4;
    private static final int MENU_ITEM_CLEAR_ID = 5;

    private static FragmentManager fragmentManager;
    private static MapFragment mf;
    private static GoogleMap map;

    private static MapDialog mapDialog;

    private GroundOverlay myPosition;
    private GroundOverlay plane;

    double kyivLat = 50.4500259340476;  // Центр - Крещатик
    double kyivLon = 30.52333608269692;  // Центр - Крещатик
    double moscowLat = 55.75411978563285;  // Центр - Красная Площадь
    double moscowLon = 37.62041185051203;  // Центр - Красная Площадь


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        fragmentManager = getFragmentManager();
        activity = this;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        menu.add(0, MENU_ITEM_INFO_ID, 0, MENU_ITEM_INFO);
        menu.add(0, MENU_ITEM_MAP_TYPE_ID, 0, MENU_ITEM_MAP_TYPE);
        menu.add(0, MENU_ITEM_AREA_ID, 0, MENU_ITEM_AREA);
        menu.add(0, MENU_ITEM_GROUNGOVERLAY_ID, 0, MENU_ITEM_GROUNGOVERLAY);
        menu.add(0, MENU_ITEM_CLEAR_ID, 0, MENU_ITEM_CLEAR);

        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        switch (item.getItemId()) {

//            case MENU_ITEM_INFO_ID:
//                processInfoItem();
//                break;

            case MENU_ITEM_MAP_TYPE_ID:
                processMapTypeItem();
                break;

            case MENU_ITEM_AREA_ID:
                processMapAreaItem();
                break;

            case MENU_ITEM_GROUNGOVERLAY_ID:
                processGroundOverlayItem();
                break;

            case MENU_ITEM_CLEAR_ID:
                processClearMapItem();
                break;
        }

        return super.onMenuItemSelected(featureId, item);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.setOnMapClickListener(this);
        map.setOnMapLongClickListener(this);

        LatLng kyiv = new LatLng(kyivLat, kyivLon);
        map.addMarker(new MarkerOptions().position(kyiv).title("Marker in Kyiv"));
        map.moveCamera(CameraUpdateFactory.newLatLng(kyiv));
    }

//    private void processInfoItem() {
//
//        CameraPosition cp = map.getCameraPosition(); // получение настроек камеры карты (см. п. 9)
//        int mapType = map.getMapType();        // получение настроек камеры карты (см. п. 3)
//        float maxZoomLevel = map.getMaxZoomLevel();     // получение максимального значения масштаба
//        float minZoomLevel = map.getMinZoomLevel();     // получение наименьшего значения масштаба
//        Location location = map.getMyLocation();    // получение gps-координат нахождения телефона /требует разрешения использования GPS/
//        Projection projection = map.getProjection();    // работа с тоображением проекции карты на экран
//        UiSettings uiSettings = map.getUiSettings();    // содержит информацию о способах воздействия на карту (жесты, прием gps-координат и т.д.)

//        StringBuilder sb = new StringBuilder();
//        sb.append( cp.toString() + "/n");
//        sb.append( mapType + "/n");
//        sb.append( maxZoomLevel + "/n");
//        sb.append( minZoomLevel + "/n");
//        sb.append( location.toString() + "/n");

//        map.isBuildingsEnabled();    // определение включен ли 3D-режим отображения зданий на карте
//        map.isIndoorEnabled();      // определение включен ли режим карт внутри зданий (http://maps.google.com/help/maps/indoormaps/)
//        map.isMyLocationEnabled();   // определяет могут ли карты использовать gps для опрежедения местоположения устройтва
//        map.isTrafficEnabled();     // ???
//    }

    private void processMapTypeItem() {
        mapDialog = new MapDialog();

        Bundle b = new Bundle();
        b.putInt(MapDialog.DIALOG_MODE_KEY, MapDialog.DIALOG_MODE_MAP_TYPE);
        mapDialog.setArguments(b);

        mapDialog.show(fragmentManager, "Диалог");
    }

    private void processMapAreaItem() {
        mapDialog = new MapDialog();

        Bundle b = new Bundle();
        b.putInt(MapDialog.DIALOG_MODE_KEY, MapDialog.DIALOG_MODE_MAP_AREA);
        mapDialog.setArguments(b);

        mapDialog.show(fragmentManager, "Диалог");
    }

    private void processGroundOverlayItem() {
    }

    private void processClearMapItem() {
        map.clear();
    }

    @Override
    public void onMapClick(LatLng latLng) {}

    @Override
    public void onMapLongClick(LatLng latLng) {}

    public static class MapDialog extends DialogFragment {

        static final int DIALOG_MODE_MAP_TYPE = 1;
        static final int DIALOG_MODE_MAP_AREA = 2;
        static final int DIALOG_MODE_OVERLAY_MODE = 3;

        static final String DIALOG_MODE_KEY = "DIALOG_MODE_KEY";
//        static final String DIALOG_MODE_MAP_TYPE_KEY = "MAP_TYPE_KEY";
//        static final String DIALOG_MODE_MAP_AREA_KEY = "MAP_AREA_KEY";
//        static final String DIALOG_MODE_OVERLAY_MODE_KEY = "OVERLAY_MODE_KEY";


        int currentDialogMode;

        public MapDialog(){
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View v = null;

            Bundle b = getArguments();
            currentDialogMode = b.getInt(DIALOG_MODE_KEY);

            switch (currentDialogMode) {
                case DIALOG_MODE_MAP_TYPE:
                    v = getMapTypeListContent();
                    break;
                case DIALOG_MODE_MAP_AREA:
                    v = getMapAreaListContent();
                    break;
                case DIALOG_MODE_OVERLAY_MODE:
                    v = getOverlayDirectionContent();
                    break;
            }
            return v;
        }

        private View getMapTypeListContent() {

            ListView lv = new ListView(getActivity());

            final String mapTypeKeys[] = { "Стандартная карта",
                    "Гибридная карта",
                    "Политическая карта",
                    "Спутник",
                    "Нет" };
            final int mapTypeValues[] = { GoogleMap.MAP_TYPE_NORMAL,
                    GoogleMap.MAP_TYPE_HYBRID,
                    GoogleMap.MAP_TYPE_TERRAIN,
                    GoogleMap.MAP_TYPE_SATELLITE,
                    GoogleMap.MAP_TYPE_NONE };

            ArrayAdapter<String> ad = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1,
                    mapTypeKeys);

            lv.setAdapter(ad);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                    map.setMapType(mapTypeValues[arg2]);

                    mapDialog.dismiss();

                    Toast.makeText(getActivity(), mapTypeKeys[arg2], Toast.LENGTH_LONG).show();
                }
            });
            return lv;
        }

        private View getMapAreaListContent(){

            ListView lv = new ListView(getActivity());

            final String mapAreaKeys[] = { "Круг", "Прямоугольник", "Полилиния" };

            ArrayAdapter<String> ad = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mapAreaKeys);

            lv.setAdapter(ad);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                    switch (arg2) {
                        case 0: // Круг

                            CircleOptions circleOptions = new CircleOptions()
                                    .center(new LatLng(50.5f, 30.3f))
                                    .radius(150000)   //set radius in meters
                                    .fillColor(Color.RED)
                                    .strokeColor(Color.BLACK)
                                    .strokeWidth(5)
                                    .zIndex(.8f);

                            map.addCircle(circleOptions);

                            break;
                        case 1: // Прямоугольник

                            PolygonOptions polygonOptions = new PolygonOptions()
                                    .add(new LatLng(55.0, 36.0))
                                    .add(new LatLng(57.0, 36.0))
                                    .add(new LatLng(57.0, 38.0))
                                    .add(new LatLng(55.0, 38.0));

                            polygonOptions.strokeColor(Color.GREEN);
                            polygonOptions.strokeWidth(10);

                            map.addPolygon(polygonOptions);

                            break;
                        case 2: // Полилиния

                            PolylineOptions polylineOptions = new PolylineOptions()
                                    .add(new LatLng(57.0, 30.0))
                                    .add(new LatLng(56.0, 30.0))
                                    .add(new LatLng(57.0, 31.0))
                                    .add(new LatLng(56.0, 32.0))
                                    .add(new LatLng(54.0, 32.0));

                            polylineOptions.geodesic(true);

                            // Get back the mutable Polyline
                            Polyline polyline = map.addPolyline(polylineOptions);
                            break;

                    }

                    mapDialog.dismiss();

                    Toast.makeText(getActivity(), mapAreaKeys[arg2], Toast.LENGTH_LONG).show();
                }
            });
            return lv;
        }

        private View getOverlayDirectionContent() {
            return null;
        }
    }
}