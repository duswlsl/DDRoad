package com.seoul.ddroad.map;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seoul.ddroad.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DataDialog extends DialogFragment {
    Data data;

    @BindView(R.id.tv_title)
    TextView tv_title;

    public DataDialog() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_data, container, false);
        ButterKnife.bind(this, view);

        data = (Data) getArguments().getSerializable("data");
        setInfo();
        return view;
    }

    private void setInfo() {
        tv_title.setText(data.getTitle());
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(800, 800);
    }

    @OnClick(R.id.btn_cancel)
    public void clickX() {
        dismiss();
    }

}
