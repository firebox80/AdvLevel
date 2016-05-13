package com.example.maxim.geolocation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static CharSequence MENU_ITEM_LOCATION = "Геолокация";
    private static CharSequence MENU_ITEM_GEOCODING_BY_COORD = "Геокодирование по координатам";
    private static CharSequence MENU_ITEM_GEOCODING_BY_NAME = "Геокодирование по месту";

    private static final int MENU_ITEM_LOCATION_ID = 1;
    private static final int MENU_ITEM_GEOCODING_BY_COORD_ID = 2;
    private static final int MENU_ITEM_GEOCODING_BY_NAME_ID = 3;

    private LocationManager locationManager;

    private String currentProvaderStr;

    private TextView tvInfo;
    private Location testLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvInfo = (TextView) findViewById(R.id.tvInfo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_ITEM_LOCATION_ID, 0, MENU_ITEM_LOCATION);
        menu.add(0, MENU_ITEM_GEOCODING_BY_COORD_ID, 0, MENU_ITEM_GEOCODING_BY_COORD);
        menu.add(0, MENU_ITEM_GEOCODING_BY_NAME_ID, 0, MENU_ITEM_GEOCODING_BY_NAME);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ITEM_LOCATION_ID:
                tvInfo.setText(MENU_ITEM_LOCATION);
                locationService();
                break;
            case MENU_ITEM_GEOCODING_BY_COORD_ID:
                tvInfo.setText(MENU_ITEM_GEOCODING_BY_COORD);
                geocoding(MENU_ITEM_GEOCODING_BY_COORD_ID);
                break;
            case MENU_ITEM_GEOCODING_BY_NAME_ID:
                tvInfo.setText(MENU_ITEM_GEOCODING_BY_NAME);
                geocoding(MENU_ITEM_GEOCODING_BY_NAME_ID);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void locationService() {

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        StringBuilder sb = new StringBuilder();
        String providerGPSStr = LocationManager.GPS_PROVIDER;
        String providerNetworkStr = LocationManager.NETWORK_PROVIDER;
        String providerPassiveStr = LocationManager.PASSIVE_PROVIDER;

        sb.append("\n providerGPSStr : \t" + providerGPSStr);
        sb.append("\n providerNetworkStr : \t" + providerNetworkStr);
        sb.append("\n providerPassiveStr : \t" + providerPassiveStr);
        sb.append("\n");


        boolean isGPSProviderEnabled = false;
        boolean isNeworkProviderEnabled = false;
        boolean isPassiveProviderEnabled = false;

        try {
            isGPSProviderEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            sb.append("\n isGPSProviderEnabled : \t" + isGPSProviderEnabled);
        } catch (Exception e) {
            sb.append("\n isGPSProviderEnabled : \t" + e.getMessage());
        }

        try {
            isNeworkProviderEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            sb.append("\n isNeworkProviderEnabled : \t" + isNeworkProviderEnabled);
        } catch (Exception e) {
            sb.append("\n isNeworkProviderEnabled : \t" + e.getMessage());
        }
        try {
            isPassiveProviderEnabled = locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);
            sb.append("\n isPassiveProviderEnabled : \t" + isPassiveProviderEnabled);
        } catch (Exception e) {
            sb.append("\n isPassiveProviderEnabled : \t" + e.getMessage());
        }

        if (!isGPSProviderEnabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        Toast.makeText(getApplicationContext(), MENU_ITEM_LOCATION, Toast.LENGTH_LONG).show();

        LocationProvider providerGPS = locationManager.getProvider(LocationManager.GPS_PROVIDER);
        LocationProvider providerNetwork = locationManager.getProvider(LocationManager.NETWORK_PROVIDER);
        LocationProvider providerPassive = locationManager.getProvider(LocationManager.PASSIVE_PROVIDER);

        LocationProvider testProvaider = providerNetwork;

        if (testProvaider != null) {
            processProvider(sb, LocationManager.NETWORK_PROVIDER, testProvaider);
        }

        Location lastKnowLocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location lastKnowLocationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Location lastKnowLocationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        testLocation = lastKnowLocationNetwork;
        if (testLocation != null) {
            processLocation(sb, LocationManager.NETWORK_PROVIDER, testLocation);
        }



        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                String mess = "latitude :" + location.getLatitude()+ " longitude : " +
                        location.getLongitude();
                Toast.makeText(getApplicationContext(), mess, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        currentProvaderStr = LocationManager.GPS_PROVIDER;
        locationManager.requestLocationUpdates(currentProvaderStr, 0, 2, locationListener);
    }

    private void processProvider(StringBuilder sb, String networkProvider, LocationProvider testProvider) {
//        		getAccuracy() 	API<9		-> 			1 = Criteria.ACCURACY_FINE
//									2 = Criteria.ACCURACY_COARSE
//		getAccuracy() 	API>=9		-> 			 1 = Criteria.ACCURACY_LOW для поверхности и высоты точночть > +/- 500 м.
//									2 = Criteria.ACCURACY_MEDIUM	для
//        поверхности и высоты точночть   +/- 100 - 500 м.
//									3 = Criteria.ACCURACY_HIGH		для
//                поверхности и высоты точночть < +/- 100 м.
//		getName()	  		  			->      LocationManager.GPS_PROVIDER
//										LocationManager.NETWORK_PROVIDER
//									        LocationManager.PASSIVE_PROVIDER
//		getPowerRequirement () ->   				1 = Criteria.POWER_LOW
//									2 = Criteria.POWER_MEDIUM
//									3 = Criteria.POWER_HIGH

        int accuracy = testProvider.getAccuracy();
        String name = testProvider.getName();
        int powerRequirement = testProvider.getPowerRequirement();
        boolean hasMonetaryCost = testProvider.hasMonetaryCost();
        boolean requiresCell = testProvider.requiresCell();
        boolean requiresNetwork = testProvider.requiresNetwork();
        boolean requiresSatellite = testProvider.requiresSatellite();
        boolean supportsAltitude = testProvider.supportsAltitude();
        boolean supportsBearing = testProvider.supportsBearing();
        boolean supportsSpeed = testProvider.supportsSpeed();

        sb.append("\n Test Provider:");
        sb.append("\n accuracy :\t" + accuracy);
        sb.append("\n name :\t" + name);
        sb.append("\n powerRequirement :\t" + powerRequirement);
        sb.append("\n hasMonetaryCost :\t" + hasMonetaryCost);
        sb.append("\n requiresCell :\t" + requiresCell);
        sb.append("\n requiresNetwork :\t" + requiresNetwork);
        sb.append("\n requiresSatellite :\t" + requiresSatellite);
        sb.append("\n supportsAltitude :\t" + supportsAltitude);
        sb.append("\n supportsBearing :\t" + supportsBearing);
        sb.append("\n supportsSpeed :\t" + supportsSpeed);
        sb.append("\n");
    }

    private void processLocation(StringBuilder sb, String type, Location testLocation) {
        boolean hasAccuracy = testLocation.hasAccuracy();
        boolean hasAltitude = testLocation.hasAltitude();
        boolean hasBearing = testLocation.hasBearing();
        boolean hasSpeed = testLocation.hasSpeed();
        float accuracy = testLocation.getAccuracy();
        double altitude = testLocation.getAltitude();
        float bearing = testLocation.getBearing();
        Bundle extras = testLocation.getExtras();
        Set<String> keySet = extras.keySet();
        double latitude = testLocation.getLatitude();
        double longitude = testLocation.getLongitude();
        String provider2 = testLocation.getProvider();
        float speed = testLocation.getSpeed();
        long time = testLocation.getTime();

        sb.append("\n Test Location:");
        sb.append("\n type :\t" + type);
        sb.append("\n hasAccuracy :\t" + hasAccuracy);
        sb.append("\n hasAltitude :\t" + hasAltitude);
        sb.append("\n hasBearing :\t" + hasBearing);
        sb.append("\n hasSpeed :\t" + hasSpeed);
        sb.append("\n accuracy :\t" + accuracy);
        sb.append("\n altitude :\t" + altitude);
        sb.append("\n bearing :\t" + bearing);
        sb.append("\n latitude :\t" + latitude);
        sb.append("\n longitude :\t" + longitude);
        sb.append("\n provider2 :\t" + provider2);
        sb.append("\n speed :\t" + speed);
        sb.append("\n time :\t" + time);
        sb.append("\n");
    }

//    private void geocoding(int mode){
//
//        Geocoder geocoder = new Geocoder(getApplicationContext());
//        List<Address> list =null;
//
//        switch (mode){
//            case MENU_ITEM_GEOCODING_BY_COORD_ID:
//                list = geocoder.getFromLocation("Одесса")
//                Toast.makeText(getApplicationContext(), MENU_ITEM_GEOCODING_BY_COORD, Toast.LENGTH_LONG).show();
//                break;;
//            case MENU_ITEM_GEOCODING_BY_NAME_ID:
//                Toast.makeText(getApplicationContext(), MENU_ITEM_GEOCODING_BY_NAME, Toast.LENGTH_LONG).show();
//                break;;
//
//        }
//    }

    private void geocoding(int mode) {

        double kyivLat = 50.4500259340476;  // Центр - Крещатик
        double kyivLon = 30.52333608269692;  // Центр - Крещатик

        Geocoder geocoder = new Geocoder(getApplicationContext());

        List<Address> list = null;

        try {
            switch (mode) {
                case MENU_ITEM_GEOCODING_BY_COORD_ID:
                    list = geocoder.getFromLocation(kyivLat, kyivLon, 5);
                    break;

                case MENU_ITEM_GEOCODING_BY_NAME_ID:
                    list = geocoder.getFromLocationName("Одесса", 5);
//				list = geocoder.getFromLocationName(locationName, maxResults, lowerLeftLatitude, lowerLeftLongitude,
//                    upperRightLatitude, upperRightLongitude);
                    break;
            }
        } catch (IOException e) {
            tvInfo.setText("Geocoding Error : " + e.getMessage());
        }


        for (int i = 0; i < list.size(); i++) {
            // получаем объект Address, представляющий конкретный адрес
            // в базе данных сервиса Geocoding
            StringBuilder sb = new StringBuilder();
            Address address = list.get(i);
//			address.clearLatitude();
//			address.clearLongitude();
            int describeContents = address.describeContents();
            String adminArea = address.getAdminArea();
            String countryCode = address.getCountryCode();
            String countryName = address.getCountryName();
            Bundle extras = address.getExtras();
            String featureName = address.getFeatureName();
            double latitude = address.getLatitude();
            Locale locale = address.getLocale();
            String locality = address.getLocality();
            double longitude = address.getLongitude();
            int maxAddressLineIndex = address.getMaxAddressLineIndex();

            String addressLines[] = new String[maxAddressLineIndex];
            for(int ind = 0; ind<maxAddressLineIndex; ind++)
                addressLines[ind] = address.getAddressLine(ind);

            String phone = address.getPhone();
            String postalCode = address.getPostalCode();
            String premises = address.getPremises();
            String subAdminArea = address.getSubAdminArea();
            String subLocality = address.getSubLocality();
            String subThoroughfare = address.getSubThoroughfare();
            String thoroughfare = address.getThoroughfare();
            String url = address.getUrl();

            sb.append("Address #" + i).
                    append("\nadminArea : " + adminArea).
                    append("\ncountryCode : " + countryCode).
                    append("\ncountryName : " + countryName).
                    append("\nextras : " + extras).
                    append("\nfeatureName : " + featureName).
                    append("\nlatitude : " + latitude).
                    append("\nlocale : " + locale).
                    append("\nlocality : " + locality).
                    append("\nlongitude : " + longitude).
                    append("\naddressLines : " + Arrays.toString(addressLines)).
                    append("\nphone : " + phone).
                    append("\npostalCode : " + postalCode).
                    append("\npremises : " + premises).
                    append("\nsubAdminArea : " + subAdminArea).
                    append("\nsubLocality : " + subLocality).
                    append("\nsubThoroughfare : " + subThoroughfare).
                    append("\nthoroughfare : " + thoroughfare).
                    append("\nurl : " + url).
                    append("\n\n===============================\n\n");
            tvInfo.append(sb.toString());
        }
    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
