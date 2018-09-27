package com.seoul.ddroad.setting;


import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.seoul.ddroad.MainActivity;
import com.seoul.ddroad.R;
import com.seoul.ddroad.diary.DetailActivity;
import com.seoul.ddroad.diary.SingerItem;
import com.seoul.ddroad.map.PolylineDialog;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class SettingFragment extends Fragment {
    private RecyclerView recyclerView;
    ArrayList<ListItem> list = new ArrayList<>();

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

        recyclerView = getView().findViewById(R.id.settingRecycler);

        //줄 구분선 만들기
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getActivity().getApplicationContext(), new LinearLayoutManager(getActivity()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        list.add(new ListItem("D-day설정", R.drawable.bichon1));
        list.add(new ListItem("출처보기", R.drawable.bichon2));
        ListAdapter adapter = new ListAdapter(getActivity(), list);
        recyclerView.setAdapter(adapter);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {

            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                //Toast.makeText(getActivity().getApplicationContext(), "클릭한 아이템의 이름은 " + list.get(position).getTitle(), Toast.LENGTH_SHORT).show();

                onCreateDialog(position);

                if (position == 0) {

                    //showDialog(DIALOG_TIME);
                    //Toast.makeText(getActivity().getApplicationContext(), list.get(position).getTitle(), Toast.LENGTH_SHORT).show();


                }

            }

            public Dialog onCreateDialog(int id)
            {
                if( id == 0)
                {
                    DatePickerDialog dpd = new DatePickerDialog
                            (getContext().getApplicationContext(), // 현재화면의 제어권자
                                    new DatePickerDialog.OnDateSetListener() {
                                        public void onDateSet(DatePicker view,
                                                              int year, int monthOfYear,int dayOfMonth) {
                                            Toast.makeText(getActivity().getApplicationContext(),
                                                    year+"년 "+(monthOfYear+1)+"월 "+dayOfMonth+"일 을 선택했습니다",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    , // 사용자가 날짜설정 후 다이얼로그 빠져나올때
                                    //    호출할 리스너 등록
                                    2015, 6, 21); // 기본값 연월일
                    return dpd;
                }
                return this.onCreateDialog(id);
            }
        });
    }


    public Dialog onCreateDialog() {
        DatePickerDialog dpd = new DatePickerDialog
                (getActivity().getApplicationContext(), // 현재화면의 제어권자
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view,
                                                  int year, int monthOfYear, int dayOfMonth) {
                                Toast.makeText(getActivity().getApplicationContext(),
                                        year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일 을 선택했습니다",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        , // 사용자가 날짜설정 후 다이얼로그 빠져나올때
                        //    호출할 리스너 등록
                        2015, 6, 21); // 기본값 연월일
        Log.d("날짜정보", "날짜:" + dpd);
        return dpd;
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
