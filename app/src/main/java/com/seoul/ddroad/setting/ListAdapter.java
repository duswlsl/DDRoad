package com.seoul.ddroad.setting;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.seoul.ddroad.R;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    Context context;
    ArrayList<ListItem> singModels = new ArrayList<>();

    public ListAdapter(Context context, ArrayList<ListItem> singModels) {
        this.context = context;
        this.singModels = singModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.setting_item
                ,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListItem singModel = singModels.get(position);

        holder.title.setText(singModel.getTitle());
        holder.imageview.setImageResource(singModel.getResId());
    }

    @Override
    public int getItemCount() {
        return singModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView imageview;
        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.settingTextView);
            imageview = itemView.findViewById(R.id.settingImageView);
        }
    }
}

