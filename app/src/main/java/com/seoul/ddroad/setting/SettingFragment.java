package com.seoul.ddroad.setting;


import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.seoul.ddroad.MainActivity;
import com.seoul.ddroad.R;
import com.seoul.ddroad.diary.DetailActivity;
import com.seoul.ddroad.diary.SingerItem;
import com.seoul.ddroad.intro.DustFragment;
import com.seoul.ddroad.map.PolylineDialog;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class SettingFragment extends Fragment {
    private RecyclerView recyclerView;
    private TextView mDisplayDate;
    private TextView mDisplayDogname;
    private Button btn_CertainDog;
    private EditText edt_dogname;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private String strDogName;
    ArrayList<ListItem> list = new ArrayList<>();


    Bundle args = new Bundle(1);
    String input;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        strDogName = "";
        input = "";
        recyclerView = getView().findViewById(R.id.settingRecycler);
        mDisplayDate = getView().findViewById(R.id.text_dog_date);
        //줄 구분선 만들기
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getActivity().getApplicationContext(), new LinearLayoutManager(getActivity()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        list.add(new ListItem("펫 이름설정", R.drawable.bichon1));
        list.add(new ListItem("D-day설정", R.drawable.bichon1));
        list.add(new ListItem("출처보기", R.drawable.bichon2));
        ListAdapter adapter = new ListAdapter(getActivity(), list);
        recyclerView.setAdapter(adapter);


        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                //Toast.makeText(getActivity().getApplicationContext(), "클릭한 아이템의 이름은 " + list.get(position).getTitle(), Toast.LENGTH_SHORT).show();

                //0.강아지 이름, 1.강아지 데려온 날짜, 3.출처보기
                switch (position) {
                    case 0:
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                        View mView = getLayoutInflater().inflate(R.layout.dialog_mydog, null);


                        mBuilder.setView(mView);
                        final AlertDialog dialog = mBuilder.create();
                        dialog.show();

                        final String STRTAG_1 ="SETTING";
                        btn_CertainDog = mView.findViewById(R.id.btn_certaindog);
                        edt_dogname = mView.findViewById(R.id.edit_dog_name);
                        mDisplayDogname = mView.findViewById(R.id.text_dog_name);


                        btn_CertainDog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                input = edt_dogname.getText().toString();

//                                if(getFragmentManager().findFragmentByTag(STRTAG_1) == null)
//                                {
//                                    SettingFragment fragment = new SettingFragment();
//                                    args.putString("MYDOG", input);
//                                    fragment.setArguments(args);
//                                }
//
//                                mDisplayDogname.setText(input.toString());
//
//                                //정보를 너머겨줘야한다 어디로 더스트 프라그먼트로
//                                Log.d("bundle", args.toString());

                                //dialog.dismiss();
                            }
                        });
                        break;
                    case 1:

                        Calendar cal = Calendar.getInstance();
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);

                        //Toast.makeText(getActivity().getApplicationContext(), list.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                        DatePickerDialog dialogPicker = new DatePickerDialog(getContext(),
                                android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener,
                                year,
                                month,
                                day);

                        dialogPicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialogPicker.show();
                        break;
                    case 2:
                        //AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                        //View mView = getLayoutInflater().inflate(R.layout.dialog_mydog, null);


                        //mBuilder.setView(mView);
                        //AlertDialog dialog = mBuilder.create();
                        //dialog.show();
                        break;
                }
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int nYear = year;
                int nMonth = month;
                int nDayofMonth = dayOfMonth;

                int daysss;
                String strDaysss;
                //확인눌러 데이타  다일로그 받어서 넣어주기
                month = month + 1;
                Log.d("Setting", "onDateSet data: " + year + "/" + month + "/" + dayOfMonth);
                //값을 더스트 프라그먼트의 화면에 넣어줘야한다

                daysss = countDday(nYear, nMonth, nDayofMonth);

                strDaysss = "" + daysss;
                Toast.makeText(getActivity().getApplicationContext(), strDaysss.toString(), Toast.LENGTH_SHORT).show();
                //mDisplayDate.setText(strDaysss.toString());
            }

            public int countDday(int myear, int mmonth, int mday) {
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
                    //캘린더
                    Calendar todaCal = Calendar.getInstance(); //현재날짜
                    Calendar ddayCal = Calendar.getInstance(); //설정날짜

                    mmonth -= 1;

                    ddayCal.set(myear, mmonth, mday);
                    ddayCal.set(myear, mmonth, mday);// D-day의 날짜를 입력 셋팅해준다
                    Log.e("테스트", simpleDateFormat.format(todaCal.getTime()) + "");
                    Log.e("테스트", simpleDateFormat.format(ddayCal.getTime()) + "");

                    long today = todaCal.getTimeInMillis() / 86400000; //(24 * 60 * 60 * 1000) 24시간 60분 60초 * (ms초->초 변환 1000)
                    long dday = ddayCal.getTimeInMillis() / 86400000;
                    long count = (dday - today) * -1; // 오늘 날짜에서 dday 빼준다
                    return (int) count; // 결과값 반환해준다


                } catch (Exception e) {
                    e.printStackTrace();
                    return -1;
                }
            }
        };
    }
//
//    void onPetState() {
//        //다이얼로그
//        PolylineDialog dialog = new PolylineDialog();
//        Bundle args = new Bundle();
//        dialog.setArguments(args);
//        dialog.setTargetFragment(this, 2);
//        dialog.show(getActivity().getSupportFragmentManager(), "tag");
//    }
}