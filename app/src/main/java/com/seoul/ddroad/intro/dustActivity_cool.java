package com.seoul.ddroad.intro;

import android.graphics.Typeface;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.seoul.ddroad.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class dustActivity_cool extends AppCompatActivity {
    private String inputDate;
    private String inputTime;
    private String inputNx;
    private String inputNy;
    private String location;
    private Double temperature;
    private TextView text_temperature;
    private TextView text_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        inputDate =""; inputTime =""; inputNx = ""; inputNy = ""; location = "용산구"; temperature = 0.0;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dust_cool);

        text_temperature = (TextView)findViewById(R.id.text_temperature);
        text_location = (TextView)findViewById(R.id.text_location);

        text_location.setText(location);


        //폰트설정
        fontChange();

        //날짜시간설정
        setDateTime();

        //임의로 주소입력
        setNxNy(location);

        //온도API
        setTempApi(inputDate,inputTime,inputNx,inputNy);


    }

    public void fontChange(){
        TextView text_finddust = (TextView)findViewById(R.id.text_finedust);
        TextView text_dog_date = (TextView)findViewById(R.id.text_dog_date);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"BMJUA_ttf.ttf");

        text_temperature.setTypeface(typeface);
        text_finddust.setTypeface(typeface);
        text_dog_date.setTypeface(typeface);
        text_location.setTypeface(typeface);
    }

    public void setDateTime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        inputDate = dateFormat.format(c.getTime());

        SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
        String formattedHour = hourFormat.format(c.getTime());

        SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");
        String formattedMinute = minuteFormat.format(c.getTime());


        int hour = Integer.parseInt(formattedHour);

        //40분간격 업데이트 if문처리
        if(Integer.parseInt(formattedMinute)<41){
            if(hour/10<1){
                inputTime = "0"+(hour-1)+"59";
            }else{
                inputTime = (hour-1)+"59";
            }
        }else{
            inputTime = formattedHour+formattedMinute;
        }

    }

    public void setNxNy(String location){

        if(location.equals("서울시")){
            inputNx="60";
            inputNy="127";
        }else if(location.equals("종로구")){
            inputNx="60";
            inputNy="127";
        }else if(location.equals("중구")){
            inputNx="60";
            inputNy="127";
        }else if(location.equals("용산구")){
            inputNx="60";
            inputNy="126";
        }else if(location.equals("성동구")){
            inputNx="61";
            inputNy="127";
        }else if(location.equals("광진구")){
            inputNx="62";
            inputNy="126";
        }else if(location.equals("동대문구")){
            inputNx="61";
            inputNy="127";
        }else if(location.equals("중랑구")){
            inputNx="62";
            inputNy="128";
        }else if(location.equals("성북구")){
            inputNx="61";
            inputNy="127";
        }else if(location.equals("강북구")){
            inputNx="61";
            inputNy="128";
        }else if(location.equals("도봉구")){
            inputNx="61";
            inputNy="129";
        }else if(location.equals("노원구")){
            inputNx="61";
            inputNy="129";
        }else if(location.equals("은평구")){
            inputNx="59";
            inputNy="127";
        }else if(location.equals("서대문구")){
            inputNx="59";
            inputNy="127";
        }else if(location.equals("마포구")){
            inputNx="59";
            inputNy="127";
        }else if(location.equals("양천구")){
            inputNx="58";
            inputNy="126";
        }else if(location.equals("강서구")){
            inputNx="58";
            inputNy="126";
        }else if(location.equals("구로구")){
            inputNx="58";
            inputNy="125";
        }else if(location.equals("금천구")){
            inputNx="59";
            inputNy="124";
        }else if(location.equals("영등포구")){
            inputNx="58";
            inputNy="126";
        }else if(location.equals("동작구")){
            inputNx="59";
            inputNy="125";
        }else if(location.equals("관악구")){
            inputNx="59";
            inputNy="125";
        }else if(location.equals("서초구")){
            inputNx="61";
            inputNy="125";
        }else if(location.equals("강남구")){
            inputNx="61";
            inputNy="126";
        }else if(location.equals("송파구")){
            inputNx="62";
            inputNy="126";
        }else if(location.equals("강동구")){
            inputNx="62";
            inputNy="126";
        }

    }


    public void setTempApi(String inputDate, String inputTime, String inputNx, String inputNy){


        String searchUrl = "http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2/ForecastGrib?";
        String serviceKey = "ServiceKey=6HQWWR6iibX4NvJpYaP%2BP%2Blivy9AiBISgqmHA%2FxT4vsJKPwxr%2BRIMG%2BNFDhz3ZWkSJHoyDp3cTjzF2dfuXG84w%3D%3D";
        String baseDate = "&base_date="+inputDate;
        String baseTime = "&base_time="+inputTime;
        String nx = "&nx="+inputNx;
        String ny = "&ny="+inputNy;
        String lastText = "&pageNo=1&numOfRows=10&_type=json";
        String requestUrl = searchUrl+serviceKey+baseDate+baseTime+nx+ny+lastText;

        getJSON(requestUrl);
    }

    public void getJSON(final String requestUrl) {

        if ( requestUrl == null) return;

        Thread thread = new Thread(new Runnable() {

            public void run() {

                String result;

                try {

                    Log.e("tag",requestUrl);
                    URL url = new URL(requestUrl);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                    httpURLConnection.setReadTimeout(3000);
                    httpURLConnection.setConnectTimeout(3000);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setUseCaches(false);
                    httpURLConnection.connect();


                    int responseStatusCode = httpURLConnection.getResponseCode();

                    InputStream inputStream;
                    if (responseStatusCode == HttpURLConnection.HTTP_OK) {

                        inputStream = httpURLConnection.getInputStream();
                    } else {
                        inputStream = httpURLConnection.getErrorStream();

                    }


                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    StringBuilder sb = new StringBuilder();
                    String line;


                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }

                    bufferedReader.close();
                    httpURLConnection.disconnect();

                    result = sb.toString().trim();


                } catch (Exception e) {
                    result = e.toString();
                }



                temperature = jsonParser(result);
                text_temperature.setText(String.valueOf(temperature)+"도");
            }

        });
        thread.start();
    }



    public Double jsonParser(String jsonString){
        double myFloatValue=0.0;

        if (jsonString == null ) return 0.0;

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject res = jsonObject.getJSONObject("response");
            JSONObject body = res.getJSONObject("body");
            JSONObject items = body.getJSONObject("items");
            JSONArray item = items.getJSONArray("item");
            JSONObject jsonObject2 = new JSONObject(item.get(5).toString());
            myFloatValue = BigDecimal.valueOf(jsonObject2.getDouble("obsrValue")).doubleValue();

            return myFloatValue;
        } catch (JSONException e) {

            Log.d("TAG", e.toString() );
        }

        return myFloatValue;
    }

}
