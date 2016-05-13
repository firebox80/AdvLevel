package com.example.maxim.internet;

public class API {

    private static final String K_BASE_URL  =            "http://petitions-test.dev.gns-it.com";

    private static final String API_GET_INFO =           "/api/get-info";

    private static final String API_GET_CITY_LIST =      "/api/get-city-list";

    private static final String API_GET_DISTRICT_LIST =  "/api/get-district-list";

    public static String getInfo() {
        String param1 = "_format=json";
        String urlStr = K_BASE_URL +
                        API_GET_INFO +
                        "?" +
                        param1;
        return urlStr;
//    http://petitions-test.dev.gns-it.com/api/get-info?_format=json
    }




    public static String getCityList() {




        String urlStr = K_BASE_URL +

                        API_GET_CITY_LIST;




        return urlStr;

//    http://petitions-test.dev.gns-it.com/api/get-city-list

    }




    public static String getDistrictList() {




        String urlStr = K_BASE_URL +

                        API_GET_DISTRICT_LIST;




        return urlStr;

//    http://petitions-test.dev.gns-it.com/api/get-district-list

    }

}