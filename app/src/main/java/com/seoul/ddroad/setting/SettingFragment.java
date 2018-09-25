package com.seoul.ddroad.setting;


import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.seoul.ddroad.R;
import com.seoul.ddroad.diary.DetailActivity;
import com.seoul.ddroad.diary.SingerItem;

import java.util.ArrayList;


public class SettingFragment extends Fragment {
    private RecyclerView recyclerView;
    ArrayList<ListItem> list = new ArrayList<>();
    public void onCreate( Bundle savedInstanceState) {
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
                new DividerItemDecoration(getActivity().getApplicationContext(),new LinearLayoutManager(getActivity()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        list.add(new ListItem("D-day설정",R.drawable.bichon1));
        list.add(new ListItem("출처보기",R.drawable.bichon2));

        ListAdapter adapter = new ListAdapter(getActivity(),list);
        recyclerView.setAdapter(adapter);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener(){

            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Toast.makeText(getActivity().getApplicationContext(), "클릭한 아이템의 이름은 " + list.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });




        }


    }
