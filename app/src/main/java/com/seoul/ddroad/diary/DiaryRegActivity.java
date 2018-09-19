package com.seoul.ddroad.diary;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.seoul.ddroad.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static java.sql.DriverManager.println;

/**
 * Created by guitarhyo on 2018-08-15.
 */
public class DiaryRegActivity extends AppCompatActivity{

    private Spinner spinner;
    private String mImgStr="";
    private TextView weatherDate;
    private Date mCurrentDate; //전역 현재날짜 선언
    private Calendar now;
    private String dateStr = "";
    private String timeStr = "";
    private String weatherDateStr = "";
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private FloatingActionButton fab;
    private String imgDirStr = "";
    private SqlLiteDao sqlLiteImgDao;
    @Override
    public void onCreate( Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diaryreg);

        sqlLiteImgDao = new SqlLiteDao(DiaryRegActivity.this);
        spinner =(Spinner)findViewById(R.id.weatherSpinner);

        now = Calendar.getInstance();
        mCurrentDate = now.getTime();  //현재 날짜를 가져온다
        weatherDateStr = getDateFormat("yyyy-MM-dd HH:mm",mCurrentDate);
        weatherDate = (TextView)findViewById(R.id.weatherDate);
        weatherDate.setText(weatherDateStr);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new MyAdapter(this);
        recyclerView.setAdapter(myAdapter);

        fab = findViewById(R.id.diaryImgFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pix.start(DiaryRegActivity.this, 100, 1);//선택 개수 정할수있음 일단 1개
            }
        });



        Button btnDate  = (Button)findViewById(R.id.regBtnDate);
        btnDate.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                new android.app.DatePickerDialog(
                        DiaryRegActivity.this,
                        new android.app.DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                if(timeStr == ""){
                                    timeStr = getDateFormat("HH:mm",mCurrentDate);
                                }

                                dateStr = ""+year;
                                int monthf = month+1;//1적게 들어오기때문에 더해야함
                                if(monthf > 9){
                                    dateStr += "-"+monthf;
                                }else{
                                    dateStr += "-0"+monthf;
                                }

                                if(dayOfMonth > 9){
                                    dateStr += "-"+dayOfMonth;
                                }else{
                                    dateStr += "-0"+dayOfMonth;
                                }

                                weatherDate.setText(dateStr+" "+timeStr);
                                weatherDateStr=dateStr+" "+timeStr;

                                Log.d("Orignal", weatherDateStr);
                            }
                        },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });

        Button btnTime  = (Button)findViewById(R.id.regBtnTime);
        btnTime.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

        new android.app.TimePickerDialog(
                DiaryRegActivity.this,
                new android.app.TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        if(dateStr == ""){
                            dateStr = getDateFormat("yyyy-MM-dd",mCurrentDate);
                        }
                        if(hour > 9){
                            timeStr = ""+hour;
                        }else{
                            timeStr = "0"+hour;
                        }

                        if(minute > 9){
                            timeStr += ":"+minute;
                        }else{
                            timeStr += ":0"+minute;
                        }
                        weatherDate.setText(dateStr+" "+timeStr);
                        weatherDateStr=dateStr+" "+timeStr;
                        Log.d("Original", weatherDateStr);
                    }
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
               true //24여부
        ).show();
            }
        });


        //액션바 사용
        ActionBar ab = getSupportActionBar() ;
        ab.setTitle("등록하기");

        //메뉴바에 '<' 버튼이 생긴다.(두개는 항상 같이다닌다)
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        // 출처: http://ande226.tistory.com/141 [안디스토리]


        //날씨 이미지 스피너
        String[] arr = getResources().getStringArray(R.array.weather_item_array);
        ArrayList<String> list = new ArrayList<String>();
        for (int i=0; i < arr.length ; i++){
            list.add(arr[i]);
        }

        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this, R.layout.weather_spinner_item,list);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

               // Toast.makeText(DiaryRegActivity.this,"선택된 아이템 : "+spinner.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
               // Toast.makeText(DiaryRegActivity.this,"선택된 아이템 : "+position,Toast.LENGTH_SHORT).show();
                if(position == 1 ){
                    mImgStr = "@drawable/bichon1";
                }else if(position == 2){
                    mImgStr = "@drawable/bichon2";
                }else if(position == 3){
                    mImgStr = "@drawable/bichon3";
                }else if(position == 4){
                    mImgStr = "@drawable/bichon4";
                }else if(position == 5){
                    mImgStr = "@drawable/bichon5";
                }else{
                    mImgStr = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    /**
     * Action Bar에 메뉴를 생성한다.
     * @param menu
     * @return
     */

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu2, menu);
        return true;
    }

    /**
     * 메뉴 아이템을 클릭했을 때 발생되는 이벤트...
     * @param item
     * @return
     */

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if( id == R.id.regPost ){//글 등록 누르면?


            final EditText diaryTitle=(EditText)findViewById(R.id.diaryTitle);
            final EditText diaryContent=(EditText)findViewById(R.id.diaryContent);


                Object[] params = { diaryTitle.getText(), diaryContent.getText(),mImgStr,weatherDateStr};
                sqlLiteImgDao.insertDiary(params);


            //이미지 들어갈 다이어리 번호 가져와서 파일 만들구 디비에 넣기
            int diaryId  = sqlLiteImgDao.getLastDiaryId();
            String imgDir = imgFIleWrite();
            if(imgDir != null && imgDir != ""){
                Toast.makeText(getApplicationContext(),
                        imgDir, Toast.LENGTH_SHORT)
                        .show();
                sqlLiteImgDao.insertDiaryImg(diaryId,imgDir);
            }


            Toast.makeText(getApplicationContext(),
                    "등록 되었습니다.", Toast.LENGTH_SHORT)
                    .show();

            /*Intent intent = new Intent(
                    getApplicationContext(), // 현재 화면의 제어권자
                    DiaryActivity.class); // 다음 넘어갈 클래스 지정
            startActivity(intent); // 다음 화면으로 넘어간다*/
            onBackPressed();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }



    public String imgFIleWrite(){
        String returnDirStr = "";
        FileOutputStream outStream = null;

        if(imgDirStr != null && imgDirStr != "") {
            try {
                File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "ddroad");
                Toast.makeText(getApplicationContext(),
                        mediaStorageDir.getPath(), Toast.LENGTH_LONG).show();
                if (!mediaStorageDir.exists()) { //폴더 있는지 확인하고 없으면 만든다
                    if (!mediaStorageDir.mkdirs()) {
                        Log.d("ddroad", "failed to create directory");

                    }
                }

                returnDirStr = String.format(mediaStorageDir.getPath() + "/ddroad%d.png",
                        System.currentTimeMillis());

                File mediaFile;
                Log.d("ddroad", returnDirStr);
                mediaFile = new File(returnDirStr);
                outStream = new FileOutputStream(mediaFile);

                //카메라에서 가져온 경로로 파일 만듬
                File tmpFile = new File(imgDirStr);
                byte[] data = convertFileToByteArray(tmpFile);

                outStream.write(data);
                outStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                returnDirStr = "";
            } finally {
            }
        }

         return returnDirStr;
    }
    //카메라
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.e("val", "requestCode ->  " + requestCode+"  resultCode "+resultCode);
        switch (requestCode) {
            case (100): {
                if (resultCode == Activity.RESULT_OK) {
                    ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
                    myAdapter.addImage(returnValue);

                    for (String s : returnValue) {
                        imgDirStr = s;
                        Log.e("val", " ->  " + s);

                    }
                }
            }
            break;
        }
    }

    //카메라
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(DiaryRegActivity.this, 100, 1);
                } else {
                    Toast.makeText(DiaryRegActivity.this, "권한 설정을 해야 카메라 기능을 사용할 수있습니다.", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    public byte[] convertFileToByteArray(File f)
    {
        byte[] byteArray = null;
        try
        {
            InputStream inputStream = new FileInputStream(f);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024*8];
            int bytesRead =0;

            while ((bytesRead = inputStream.read(b)) != -1)
            {
                bos.write(b, 0, bytesRead);
            }

            byteArray = bos.toByteArray();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return byteArray;
    }


    private String getDateFormat(String format,Date date){//입력 Date를 날짜를  포팻 형태로 String 출력

        if(format == null || format ==""){
            format  = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        return dateFormat.format(date);
    }
}