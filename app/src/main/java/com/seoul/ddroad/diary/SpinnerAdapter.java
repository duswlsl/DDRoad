package com.seoul.ddroad.diary;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seoul.ddroad.R;

import java.util.ArrayList;

public class SpinnerAdapter  extends ArrayAdapter<String> {
    int groupid;
    Activity context;
    ArrayList<String> list;
    LayoutInflater inflater;
    public SpinnerAdapter(Activity context, int groupid, int id, ArrayList<String>
            list){
        super(context,id,list);
        this.list=list;
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupid=groupid;
    }

    public View getView(int position, View convertView, ViewGroup parent ){
        return initRow(position, convertView, parent);

    }

    public View getDropDownView(int position, View convertView, ViewGroup
            parent){
        return getView(position,convertView,parent);

    }

    public View initRow (int position, View convertView, ViewGroup
            parent){
        ViewHolder holder ;
        View row = convertView;
                if(row == null){
                    row =  this.inflater.inflate(this.groupid, parent, false);

                    holder = new ViewHolder();
                    holder.textView1 = (TextView) row.findViewById(R.id.weathertext);
                    holder.imageView1 = (ImageView)row.findViewById(R.id.weatherimageView);
                    holder.item_holder = row.findViewById(R.id.item_holder);
                    row.setTag(holder);
                }else{

                }
        return row;
    }

    private class ViewHolder {
        TextView textView1 = null;
        ImageView imageView1 = null;
        LinearLayout item_holder = null;
    }
}
