package com.seoul.ddroad.map;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class RestAPI {
    private static String TAG = RestAPI.class.getSimpleName();

    // 네이버 API 키값
    String clientId = "5diZho3PLVF6IvUkDN0a";
    String clientSecret = "Wie3JCCLC5";

    URL url;
    String apiURL;
    String inputLine;
    private ArrayList<Data> cafeList, hospitalList, hotelList, salonList, trailList;
    private Data data;
    private String arr[];
    private String str;

    public RestAPI() {
    }


    public void getinfo(String type) {
        cafeList = new ArrayList<Data>();
        hotelList = new ArrayList<Data>();
        hospitalList = new ArrayList<Data>();
        salonList = new ArrayList<Data>();
        trailList = new ArrayList<Data>();
        Log.d("RestAPI", "start" + type);

        try {

            String text = "";
            if (type.equals("cafe")) {
                text = URLEncoder.encode("애견카페", "UTF-8");
            } else if (type.equals("hospital")) {
                text = URLEncoder.encode("서울동물병원", "UTF-8");
            } else if (type.equals("salon")) {
                text = URLEncoder.encode("서울애견미용실", "UTF-8");
            } else if (type.equals("trail")) {
                text = URLEncoder.encode("서울공원", "UTF-8");
            } else if (type.equals("hotel")) {
                text = URLEncoder.encode("서울애견호텔", "UTF-8");
            }
            for (int i = 0; i < 10; i++) {
                int start = (100 * i) + 1;
                // 오픈API를 이용하기 위한 URL
                apiURL = "https://openapi.naver.com/v1/search/local?query=" + text + "&display=100&start=" + start + "&";
                url = new URL(apiURL);

                // URL연결
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                // 정보를 얻어올 수 있는 GET메소드
                con.setRequestMethod("GET");

                // 연결을 위한 키 아이디와 비밀번호
                con.setRequestProperty("X-Naver-Client-Id", clientId);
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret);

                int responseCode = con.getResponseCode();
                BufferedReader br;

                if (responseCode == 200) { // 정상 호출
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {  // 에러 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    Log.d(TAG, br.readLine());
                }

                // 원하는 정보를 모두 받아온다. 하지만 가장 최신의 하나것만 사용하기 때문에 break;를 사용
                while ((inputLine = br.readLine()) != null) {
                    if (inputLine.contains("title")) {
                        data = new Data();
                        arr = inputLine.split("\"", 5);
                        str = arr[3].replace("<b>", "").replace("</b>", "").replace("&amp;", "&");
                        data.setTitle(str);
                    } else if (inputLine.contains("link")) {
                        arr = inputLine.split("\"", 5);
                        data.setLink(arr[3]);
                    } else if (inputLine.contains("description")) {
                        arr = inputLine.split("\"");
                        str = arr[3].replace("<b>", "").replace("</b>", "");
                        data.setDetail(str);
                    } else if (inputLine.contains("telephone")) {
                        arr = inputLine.split("\"");
                        data.setTel(arr[3]);
                    } else if (inputLine.contains("mapx")) {
                        arr = inputLine.split("\"", 5);
                        String[] arr2 = br.readLine().split("\"", 5);
                        convertToLatlng(Double.parseDouble(arr[3]), Double.parseDouble(arr2[3]));
                    } else if (inputLine.contains("address")) {
                        arr = inputLine.split("\"", 5);
                        data.setAddress(arr[3]);

                        switch (type) {
                            case "cafe":
                                cafeList.add(data);
                                break;
                            case "hospital":
                                hospitalList.add(data);
                                break;
                            case "hotel":
                                hotelList.add(data);
                                break;
                            case "salon":
                                salonList.add(data);
                                break;
                            case "trail":
                                trailList.add(data);
                                break;
                        }
                    }
                }
                br.close();
                if (i == 9)
                    saveDataSet(type);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void convertToLatlng(double x, double y) {
        GeoTransPoint mapxy = new GeoTransPoint(x, y);
        GeoTransPoint latlng = GeoTrans.convert(GeoTrans.KATEC, GeoTrans.GEO, mapxy);
        double lat = latlng.getY();
        double lng = latlng.getX();
        data.setLatitude(lat);
        data.setLongitude(lng);
    }

    public void saveDataSet(String type) {
        switch (type) {
            case "cafe":
                DataSet.cafeList = cafeList;
                break;
            case "hospital":
                DataSet.hospitalList = hospitalList;
                break;
            case "hotel":
                DataSet.hotelList = hotelList;
                break;
            case "salon":
                DataSet.salonList = salonList;
                break;
            case "trail":
                DataSet.trailList = trailList;
                break;
        }
        Log.d(TAG, "fin" + type);
    }

}