package com.example.maxim.sms;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

public class ScrollingActivity extends AppCompatActivity {
    private static final CharSequence GET_PHONE_INFO = "GET_PHONE_INFO";
    private static final CharSequence SET_PHONE_LISTENER = "SET_PHONE_LISTENER";
    private static final CharSequence MAKE_CALL = "MAKE_CALL";
    private static final CharSequence SEND_SMS = "SEND_SMS";
    private static final CharSequence RECEIVE_SMS = "RECEIVE_SMS";
    private static final CharSequence READ_SMS = "READ_SMS";
    private static final CharSequence GET_CONTACTS = "GET_CONTACTS";
    private static final CharSequence GET_ACCOUNTS = "GET_ACCOUNTS";

    private static final int MENU_ITEM_GET_PHONE_INFO = 1;
    private static final int MENU_ITEM_SET_PHONE_LISTENER = 2;
    private static final int MENU_ITEM_MAKE_CALL = 3;
    private static final int MENU_ITEM_SEND_SMS = 4;
    private static final int MENU_ITEM_RECEIVE_SMS = 5;
    private static final int MENU_ITEM_READ_SMS = 6;
    private static final int MENU_ITEM_GET_CONTACTS = 7;
    private static final int MENU_ITEM_GET_ACCOUNTS = 8;

    private TelephonyManager tm;
    private SmsManager smsManager;

    private BroadcastReceiver smsSentReceiver;
    private BroadcastReceiver smsDeliveredReceiver;
    private BroadcastReceiver smsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        smsManager = SmsManager.getDefault();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_ITEM_GET_PHONE_INFO, 0, GET_PHONE_INFO);
        menu.add(0, MENU_ITEM_SET_PHONE_LISTENER, 0, SET_PHONE_LISTENER);
        menu.add(0, MENU_ITEM_MAKE_CALL, 0, MAKE_CALL);
        menu.add(0, MENU_ITEM_SEND_SMS, 0, SEND_SMS);
        menu.add(0, MENU_ITEM_RECEIVE_SMS, 0, RECEIVE_SMS);
        menu.add(0, MENU_ITEM_READ_SMS, 0, READ_SMS);
        menu.add(0, MENU_ITEM_GET_CONTACTS, 0, GET_CONTACTS);
        menu.add(0, MENU_ITEM_GET_ACCOUNTS, 0, GET_ACCOUNTS);

        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case MENU_ITEM_GET_PHONE_INFO:
                getPhoneInfo();
                break;
            case MENU_ITEM_SET_PHONE_LISTENER:
                setSetPhoneListener();
                break;
            case MENU_ITEM_MAKE_CALL:
                makeCall();
                break;
            case MENU_ITEM_SEND_SMS:
                sendSMS();
                break;
            case MENU_ITEM_RECEIVE_SMS:
                break;
            case MENU_ITEM_READ_SMS:
                break;
            case MENU_ITEM_GET_CONTACTS:
                break;
            case MENU_ITEM_GET_ACCOUNTS:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        smsSentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS has been sent", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic Failure", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No Service", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio Off", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };
        smsDeliveredReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch(getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS Delivered", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        smsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                if(bundle != null){
                    Objects[] pdus = (Objects[]) bundle.get("pdus");
                    for (Object pdu : pdus){
                        SmsMessage smsMessage;

                        if (Build.VERSION.SDK_INT >= 19) { //KITKAT
                            SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                            smsMessage = msgs[0];
                        } else {
                            smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                            StringBuilder sb = new StringBuilder();
                            boolean isEmail = smsMessage.isEmail();
                            sb.append("\nIs Email: " + isEmail);
                            boolean isStatusReportMessage = smsMessage.isStatusReportMessage();
                            sb.append("\nIs Status Report Message: " + isStatusReportMessage);
                            String displayMessageBody = smsMessage.getDisplayMessageBody();
                            sb.append("\nDisplay MessageBody: " + displayMessageBody);
                            String displayOriginatingAddress = smsMessage.getDisplayOriginatingAddress();
                            sb.append("\nDisplay Originating: " + displayOriginatingAddress);
                            String messageBody = smsMessage.getMessageBody();
                            sb.append("\nMessage Body: " + messageBody);
                            String serviceCenterAddress = smsMessage.getServiceCenterAddress();
                            sb.append("\nService Center Address: " + serviceCenterAddress);
                            int status = smsMessage.getStatus();
                            sb.append("\nStatus: " + status);
                            // также существуют еще очень много get-методов, возвращающие более специализированную информацию
                            Toast.makeText(context, "SMS Received message :\n" + sb, Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        };
        registerReceiver(smsSentReceiver, new IntentFilter("SMS_SENT"));
        registerReceiver(smsDeliveredReceiver, new IntentFilter("SMS_DELIVERED"));
        registerReceiver(smsReceiver, new IntentFilter("SMS_RECEIVER"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(smsSentReceiver);
        unregisterReceiver(smsDeliveredReceiver);
        unregisterReceiver(smsReceiver);
    }

    private void sendSMS(){
        try {
            PendingIntent piSend = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), 0);
            PendingIntent piDelivered = PendingIntent.getBroadcast(this, 0, new Intent("SMS_Delivered"), 0);

            smsManager.sendTextMessage("+380662259442", null, "HELLO", piSend, piDelivered);
//            +380674607775
//            ArrayList<String> smsBodyParts = smsManager.divideMessage(smsMessage);
//            ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
//            ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<PendingIntent>();


//            smsManager.sendMultimediaMessage("+380674607775", null, smsBodyParts, sentPendingIntents, deliveredPendingIntents);


        } catch (IllegalArgumentException e){
            Log.d("TELEPHONY & SMS", "Проблема с отправкой SMS : " + e.getMessage());
        }
    }

    private void receiveSms(){

    }

    private void setSetPhoneListener(){
        PhoneStateListener psl = new MyPhoneStateListener();
        tm.listen(psl, PhoneStateListener.LISTEN_CELL_LOCATION | PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    private void getPhoneInfo() {

        StringBuilder sb = new StringBuilder("Информация о моем телефоне:\n\n");
//              ===== Тип телефона =====================================
        int phoneType = tm.getPhoneType();
        sb.append("> тип телефона: ");
        switch (phoneType) {
            case TelephonyManager.PHONE_TYPE_CDMA:
                sb.append("CDMA\n");
                break;
            case TelephonyManager.PHONE_TYPE_GSM:
                sb.append("GSM\n");
                break;
            case TelephonyManager.PHONE_TYPE_SIP:
                sb.append("SIP\n");
                break;
            case TelephonyManager.PHONE_TYPE_NONE:
                sb.append("Не телефон\n");
                break;
        }
//              ===== Тип связи, предоставляемой базовой станцией =====================================

        int networkType = tm.getNetworkType();
        sb.append("> тип связи: ");
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_CDMA:
                sb.append("CDMA\n");
                break;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                sb.append("EDGE\n");
                break;
            case TelephonyManager.NETWORK_TYPE_GPRS:
                sb.append("GPRS\n");
                break;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                sb.append("HSPA\n");
                break;
            case TelephonyManager.NETWORK_TYPE_LTE:
                sb.append("LTE\n");
                break;
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                sb.append("Неизвестно\n");
                break;
        }
//              ===== Параметры используемой базовой станции =====================================

        CellLocation cellLocation = tm.getCellLocation();
//              http://developer.android.com/reference/android/telephony/CellLocation.html
        Class<? extends CellLocation> class1 = cellLocation.getClass();
        sb.append("> тип базовой станции : ");
        if (class1.equals(GsmCellLocation.class)) {
            sb.append("GSM\n");
            GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
            int cid = gsmCellLocation.getCid();     // Идентификатор базовой станции
            int lac = gsmCellLocation.getLac();     // LAC (Location Area Code).
            // Территориальная единица, в пределах которой в данный момент находится абонент мобильного устройства;
            int psc = gsmCellLocation.getPsc();     // PSC (Primary Scrambling Code).
            // Код базовой станции для сетей UMTS
            sb.append("   Cid=" + cid + "\n");
            sb.append("   lac=" + lac + "\n");
            sb.append("   psc=" + psc + "\n");
        } else if (class1.equals(GsmCellLocation.class)) {
            sb.append("CDMA\n");
            CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) cellLocation;
            int baseStationId = cdmaCellLocation.getBaseStationId();        // идентификатор базовой станции;
            int baseStationLatitude = cdmaCellLocation.getBaseStationLatitude();    // географическая широта станции
            int baseStationLongitude = cdmaCellLocation.getBaseStationLongitude();  // географическая долгота станции
            int networkId = cdmaCellLocation.getNetworkId();                // идентификатор сети CDMA;
            int systemId = cdmaCellLocation.getSystemId();                          // системный идентификатор сети CDMA;
            sb.append("   baseStationId=" + baseStationId + "\n");
            sb.append("   baseStationLatitude=" + baseStationLatitude + "\n");
            sb.append("   baseStationLongitude=" + baseStationLongitude + "\n");
            sb.append("   networkId=" + networkId + "\n");
            sb.append("   systemId=" + systemId + "\n");
        }
//              ===== Состояние вызова телефона =====================================

        int callState = tm.getCallState();
        sb.append("> состояние телефона: ");
        switch (callState) {
            case TelephonyManager.CALL_STATE_IDLE:
                sb.append("телефон не активен\n");
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                sb.append("производится попытка вызова\n");
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                sb.append("телефон в состоянии соединения с абонентом\n");
                break;
        }
//              ===== тип состояния передачи данных сотовой связи =====================================

        int dataActivity = tm.getDataActivity();
        sb.append("> передача данных: ");
        switch (dataActivity) {
            case TelephonyManager.DATA_ACTIVITY_IN:
                sb.append("данные поступают\n");
                break;
            case TelephonyManager.DATA_ACTIVITY_INOUT:
                sb.append("данные поступают и выходят\n");
                break;
            case TelephonyManager.DATA_ACTIVITY_OUT:
                sb.append("данные выходят\n");
                break;
            case TelephonyManager.DATA_ACTIVITY_DORMANT:

                // Физическое устройство активно, но не может передать данные

                sb.append("передача не возможна\n");
                break;
            case TelephonyManager.DATA_ACTIVITY_NONE:
                sb.append("нет траффика\n");
                break;
        }
//              ===== Состояние канала связи =====================================

        int dataState = tm.getDataState();

        sb.append("> состояние соединения: ");
        switch (dataState) {
            case TelephonyManager.DATA_DISCONNECTED:
                sb.append("соединение прекратилось\n");
                break;
            case TelephonyManager.DATA_CONNECTING:
                sb.append("происходит подключение\n");
                break;
            case  TelephonyManager.DATA_CONNECTED:
                sb.append("произошло подключение\n");
                break;
            case TelephonyManager.DATA_SUSPENDED:
                sb.append("соединение прервано\n");
                break;
        }
        String deviceId = tm.getDeviceId();

        // IMEI

        sb.append("> DeviceId : " + deviceId + "\n");
        String deviceSoftwareVersion = tm.getDeviceSoftwareVersion();
        sb.append("> DeviceSoftwareVersion : " + deviceSoftwareVersion + "\n");
        List<NeighboringCellInfo> neighboringCellInfo = tm.getNeighboringCellInfo();
        sb.append("> NeighboringCellInfo : " + neighboringCellInfo + "\n");
        String networkCountryIso = tm.getNetworkCountryIso();
        sb.append("> NetworkCountryIso : " + networkCountryIso + "\n"); // http://userpage.chemie.fu-berlin.de/diverse/doc/ISO_3166.html
        String networkOperator = tm.getNetworkOperator();
        sb.append("> NetworkOperator : " + networkOperator + "\n");     // MCC+MNC  => http://en.wikipedia.org/wiki/Mobile_Network_Code

        // Украина/Life=255+06

        String simCountryIso = tm.getSimCountryIso();
        sb.append("> SimCountryIso : " + simCountryIso + "\n");
        String simOperator = tm.getSimOperator();
        sb.append("> SimOperator : " + simOperator + "\n");
        String simOperatorName = tm.getSimOperatorName();
        sb.append("> SimOperatorName : " + simOperatorName + "\n");
        String simSerialNumber = tm.getSimSerialNumber();
        sb.append("> SimSerialNumber : " + simSerialNumber + "\n");

//              ===== Соcтояние сим-карты ===================================

        int simState = tm.getSimState();
        sb.append("> SimState : ");
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                sb.append("SIM-карта отсутствует" + "\n");
                break;
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                sb.append("SIM-карта заблокирована" + "\n");
                break;
            case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                sb.append("требуется PIN-код" + "\n");
                break;
            case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                sb.append("требуется PUK-код" + "\n");
                break;
            case TelephonyManager.SIM_STATE_READY:
                sb.append("SIM-карта в рабочем состоянии" + "\n");
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                sb.append("SIM-карта неизвестна" + "\n");
                break;
        }

//              =================================

        boolean networkRoaming = tm.isNetworkRoaming();
        sb.append("> NetworkRoaming? : " + networkRoaming + "\n");
        boolean hasIccCard = tm.hasIccCard();
        sb.append("> hasIccCard? : " + hasIccCard + "\n");
        TextView tv = (TextView) findViewById(R.id.tv_info);
        tv.setText(sb.toString());
    }

    private void makeCall(){

        Intent intent = new Intent(Intent.ACTION_CALL_BUTTON);
        startActivity(intent);

//        String phoneNum = "0123456789";
//        Uri uri = Uri.parse("tel:" + phoneNum);
//        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
//        startActivity(intent);

//        String phoneNum = "0123456789";
//        Uri uri = Uri.parse("tel:" + phoneNum);
//        Intent intent = new Intent(Intent.ACTION_CALL, uri);
//        startActivity(intent);

    }

    private class MyPhoneStateListener extends PhoneStateListener {

        @Override
        public void onCellLocationChanged(CellLocation location) {
            super.onCellLocationChanged(location);
            Class<? extends CellLocation> class1 = location.getClass();
            StringBuilder sb = new StringBuilder("> onCellLocationChanged : ");
            if(class1.equals(GsmCellLocation.class)){
                sb.append("GSM\n");
                GsmCellLocation gsmCellLocation = (GsmCellLocation) location;
                int cid = gsmCellLocation.getCid();    // Идентификатор базовой станции
                int lac = gsmCellLocation.getLac();    // LAC (Location Area Code).

                // Территориальная единица, в пределах которой в данный момент находится абонент мобильного устройства;

                int psc = gsmCellLocation.getPsc();    // PSC (Primary Scrambling Code).

                // Код базовой станции для сетей UMTS

                sb.append("   Cid=" + cid + "\n");
                sb.append("   lac=" + lac + "\n");
                sb.append("   psc=" + psc + "\n");

            } else if(class1.equals(GsmCellLocation.class)){

                sb.append("CDMA\n");
                CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) location;
                int baseStationId = cdmaCellLocation.getBaseStationId();              // идентификатор базовой станции;
                int baseStationLatitude = cdmaCellLocation.getBaseStationLatitude();  // географическая широта станции
                int baseStationLongitude = cdmaCellLocation.getBaseStationLongitude();// географическая долгота станции
                int networkId = cdmaCellLocation.getNetworkId();                      // идентификатор сети CDMA;
                int systemId = cdmaCellLocation.getSystemId();                        // системный идентификатор сети CDMA;

                sb.append("   baseStationId=" + baseStationId + "\n");
                sb.append("   baseStationLatitude=" + baseStationLatitude + "\n");
                sb.append("   baseStationLongitude=" + baseStationLongitude + "\n");
                sb.append("   networkId=" + networkId + "\n");
                sb.append("   systemId=" + systemId + "\n");
            }
            Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {

            super.onSignalStrengthsChanged(signalStrength);

            StringBuilder sb = new StringBuilder("> onSignalStrengthsChanged : \n");
            int cdmaDbm = signalStrength.getCdmaDbm();
            sb.append("CDMA (Dbm) : " + cdmaDbm + "\n");
            int cdmaEcio = signalStrength.getCdmaEcio();
            sb.append("CDMA (Ecio) : " + cdmaEcio + "\n");
            int evdoDbm = signalStrength.getEvdoDbm();
            sb.append("EVDO (Dbm) : " + evdoDbm + "\n");
            int evdoSnr = signalStrength.getEvdoSnr();
            sb.append("EVDO (Snr) : " + evdoSnr + "\n");
            int gsmBitErrorRate = signalStrength.getGsmBitErrorRate();
            sb.append("gsmBitErrorRate : " + gsmBitErrorRate + "\n");
            int gsmSignalStrength = signalStrength.getGsmSignalStrength();
            sb.append("Signalstrengnt (Dbm) : " + gsmSignalStrength + "\n");

            Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
