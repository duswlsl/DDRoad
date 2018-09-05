package com.seoul.ddroad.diary;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.seoul.ddroad.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static com.seoul.ddroad.FontsOverride.setDefaultFont;
import static java.sql.DriverManager.println;


/**
 * Created by guitarhyo on 2018-08-15.
 */
public class DiaryActivity extends AppCompatActivity {

    private CaldroidFragment caldroidFragment;
    private Date mCurrentDate;
    private String diaryTableName = "diary";
    private String diaryDatabaseName = "diary.db";

    SQLiteDatabase database;  // database를 다루기 위한 SQLiteDatabase 객체 생성


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCurrentDate = Calendar.getInstance().getTime();
        Date currentDate = Calendar.getInstance().getTime();
        setContentView(R.layout.activity_diary);

        //액션바 사용
        ActionBar ab = getSupportActionBar() ;
        ab.setTitle("캘린더") ;

        //메뉴바에 '<' 버튼이 생긴다.(두개는 항상 같이다닌다)
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        // 출처: http://ande226.tistory.com/141 [안디스토리]

        //날짜포멧 변환
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

        initDBTable();//액티비티 생성시 디비를 생성

        //달력 생성자 생성
        caldroidFragment = new CaldroidFragment();



        // If Activity is created after rotation
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }
        // If activity is created from fresh
        else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

            caldroidFragment.setArguments(args);
        }

        caldroidFragment.setSelectedDate(currentDate);
        //setCustomResourceForDates();

        // Attach to the activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                caldroidFragment.clearSelectedDates();
                caldroidFragment.setSelectedDate(date);
                caldroidFragment.refreshView();
                mCurrentDate = date;



                ListView listView = (ListView) findViewById(R.id.listView);

                SingerAdapter adapter =  selectAdapterList(); //함수 추가해서 간단하게 보이게함

                listView.setAdapter(adapter);

                //refreshList(date)리스트리셋
            }

            @Override
            public void onChangeMonth(int month, int year) {

            }

            @Override
            public void onLongClickDate(Date date, View view) {
                Toast.makeText(getApplicationContext(),
                        "길게", Toast.LENGTH_SHORT)
                        .show();
                if(database != null){
                    Random randomGenerator = new Random();
                    int randomInteger = randomGenerator.nextInt(100); //0 ~ 99 사이의 int를 랜덤으로 생성
                    String title = "title"+randomInteger;
                    String content = "content"+randomInteger;

                    String sql = "insert into diary(title, content) values(?, ?)";
                    Object[] params = { title, content};
                    database.execSQL(sql, params);
                    println("데이터 추가함.");
                }
            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
                    Toast.makeText(getApplicationContext(),
                            "Caldroid view is created", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        };
        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);

/*
final TextView textView = (TextView) findViewById(R.id.emptyInfo);
final Button customizeButton = (Button) findViewById(R.id.customize_button);
// Customize the calendar
        customizeButton.setOnClickListener(new View.OnClickListener() {
  @Override
            public void onClick(View v) {
                if (undo) {
                    customizeButton.setText(getString(R.string.customize));
                    textView.setText("");
                    // Reset calendar
                    caldroidFragment.clearDisableDates();
                    caldroidFragment.clearSelectedDates();
                    caldroidFragment.setMinDate(null);
                    caldroidFragment.setMaxDate(null);
                    caldroidFragment.setShowNavigationArrows(true);
                    caldroidFragment.setEnableSwipe(true);
                    caldroidFragment.refreshView();
                    undo = false;
                    return;
                }

                // Else
                undo = true;
                customizeButton.setText(getString(R.string.undo));
                Calendar cal = Calendar.getInstance();

                // Min date is last 7 days
                cal.add(Calendar.DATE, -7);
                Date minDate = cal.getTime();

                // Max date is next 7 days
                cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 14);
                Date maxDate = cal.getTime();

                // Set selected dates
                // From Date
                cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 2);
                Date fromDate = cal.getTime();

                // To Date
                cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 3);
                Date toDate = cal.getTime();

                // Set disabled dates
                ArrayList<Date> disabledDates = new ArrayList<Date>();
                for (int i = 5; i < 8; i++) {
                    cal = Calendar.getInstance();
                    cal.add(Calendar.DATE, i);
                    disabledDates.add(cal.getTime());
                }
                // Customize
                caldroidFragment.setMinDate(minDate);
                caldroidFragment.setMaxDate(maxDate);
                caldroidFragment.setDisableDates(disabledDates);
                caldroidFragment.setSelectedDates(fromDate, toDate);
                caldroidFragment.setShowNavigationArrows(false);
                caldroidFragment.setEnableSwipe(false);

                caldroidFragment.refreshView();

                // Move to date
                // cal = Calendar.getInstance();
                // cal.add(Calendar.MONTH, 12);
                // caldroidFragment.moveToDate(cal.getTime());

                String text = "Today: " + formatter.format(new Date()) + "\n";
                text += "Min Date: " + formatter.format(minDate) + "\n";
                text += "Max Date: " + formatter.format(maxDate) + "\n";
                text += "Select From Date: " + formatter.format(fromDate)
                        + "\n";
                text += "Select To Date: " + formatter.format(toDate) + "\n";
                for (Date date : disabledDates) {
                    text += "Disabled Date: " + formatter.format(date) + "\n";
                }

                textView.setText(text);
            }
 });*/


    }


    //달력에 색깔 입히기
    private void setCustomResourceForDates() {
        Calendar cal = Calendar.getInstance();

        // Min date is last 7 days
        cal.add(Calendar.DATE, -7);
        Date blueDate = cal.getTime();

        // Max date is next 7 days
        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);
        Date greenDate = cal.getTime();

        if (caldroidFragment != null) {
            ColorDrawable blue = new ColorDrawable(getResources().getColor(R.color.blue));
            ColorDrawable green = new ColorDrawable(Color.GREEN);
            caldroidFragment.setBackgroundDrawableForDate(blue, blueDate);
            caldroidFragment.setBackgroundDrawableForDate(green, greenDate);
            caldroidFragment.setTextColorForDate(R.color.white, blueDate);
            caldroidFragment.setTextColorForDate(R.color.white, greenDate);
        }
    }

    class SingerAdapter extends BaseAdapter {
        ArrayList<SingerItem> items = new ArrayList<SingerItem>();


        @Override
        public int getCount() {
            return items.size();    //리스트 뷰가 몇개 있는지
        }

        public void addItem(SingerItem item){
            items.add(item);
        }

        @Override
        public Object getItem(int position) {  //몇번째 데이터를 달라
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {  //Id값 넘겨달라
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SingerItemView view = new SingerItemView(getApplicationContext());

            SingerItem item = items.get(position);
            view.setMobile(item.getMobile());
            view.setImage(item.getResId());
            return view;
        }
    }
    private SingerAdapter selectAdapterList() {//이부분에 SQL을 넣어도되고~

        List<HashMap<String,Object>> diaryList = selectDiaryData();

        SingerAdapter adapter = new SingerAdapter();

        for(int i=0; i< diaryList.size();i++){
            adapter.addItem(new SingerItem("댕댕이와 산책한 날", R.drawable.dog1));
        }

        return adapter;
    }

    /**
     * Action Bar에 메뉴를 생성한다.
     * @param menu
     * @return
     */

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return true;
    }

    /**
     * 메뉴 아이템을 클릭했을 때 발생되는 이벤트...
     * @param item
     * @return
     */

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if( id == R.id.newPost ){

            Toast.makeText(DiaryActivity.this, "새 글 등록 버튼을 클릭했습니다.", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public void initDBTable(){
        // 액티비티가 켜지면 데이터베이스 생성
        database = openOrCreateDatabase(diaryDatabaseName,MODE_PRIVATE,null);  //없으면 생성하고 있으면 그대로
        if(database != null){
            String sql = "CREATE  TABLE IF NOT EXISTS " + diaryTableName + "(diaryId integer PRIMARY KEY autoincrement, title text, content text)"; //테이블 확인후 생성
            database.execSQL(sql);

            println("테이블 생성됨.");
        }else{
            println("먼저 데이터베이스를 오픈하세요.");
        }
    }

    public List<HashMap<String,Object>> selectDiaryData(){   // 항상 DB문을 쓸때는 예외처리(try-catch)를 해야한다. 이름으로 값을 찾는것
        println("selectData() 호출됨.");
        List<HashMap<String,Object>> diaryList = new ArrayList<HashMap<String,Object>>();// 리스트로 받기위함 선언을 한다
        HashMap<String,Object> diaryObj = null; //MAP형태로 저장하기위한 객채 선언

        if(database !=null){
            String sql = "select diaryId, title, content from " + diaryTableName;
            Cursor cursor = database.rawQuery(sql, null);   // select 사용시 사용(sql문, where조건 줬을 때 넣는 값)
            println("조회된 데이터 개수 : " + cursor.getCount());   // db에 저장된 행 개수를 읽어온다

            for(int i=0; i< cursor.getColumnCount();i++){
                cursor.moveToNext();    // 첫번째에서 다음 레코드가 없을때까지 읽음
                int diaryId = cursor.getInt(0);   // 첫번째 속성
                String title = cursor.getString(1); // 두번째 속성
                String content = cursor.getString(2);    // 세번째 속성

                diaryObj = new HashMap<String,Object>(); //데이터를 넣기 위해 생성자 생성
                diaryObj.put("diaryId",diaryId);
                diaryObj.put("title",title);
                diaryObj.put("content",content);

                diaryList.add(diaryObj);
            }
            cursor.close();

        }
        return diaryList;//최종 데이터를 리턴 한다
    }

}