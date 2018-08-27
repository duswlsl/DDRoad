package com.seoul.ddroad.intro;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.seoul.ddroad.MainActivity;
import com.seoul.ddroad.R;

public class dustActivity extends AppCompatActivity {
    TextView tv;
    Button intro_bt_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dust);

        intro_bt_back = findViewById(R.id.bt_dustback);
        tv = findViewById(R.id.tv);

        //url 설정
        String url = "http://openapi.seoul.go.kr:8088/646a474675686b773132397a4b494264/json/ForecastWarningUltrafineParticleOfDustService/1/5/";


        // AsyncTask를 통해 HttpURLConnection
        NetworkTask networkTask = new NetworkTask(url, null);
        networkTask.execute();

        intro_bt_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        public NetworkTask(String url, ContentValues values) {

            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {

            String result; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
            tv.setText(s);
        }
    }
}
