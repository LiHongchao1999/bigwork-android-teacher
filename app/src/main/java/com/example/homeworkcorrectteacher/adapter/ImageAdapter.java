package com.example.homeworkcorrectteacher.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.homeworkcorrectteacher.HomeworkCorrectActivity;
import com.example.homeworkcorrectteacher.IP;
import com.example.homeworkcorrectteacher.R;
import com.example.homeworkcorrectteacher.entity.Homework;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private List<String> dataSource = new ArrayList<>();
    private int id;

    public ImageAdapter(Context context, List<String> dataSource, int id) {
        this.context = context;
        this.dataSource = dataSource;
        this.id = id;
    }

    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(id, null);
            viewHolder = new ViewHolder();
            viewHolder.imgHomework = convertView.findViewById(R.id.img_homework111);
            convertView.setTag(viewHolder);
        }else {
            viewHolder =(ViewHolder) convertView.getTag();
        }
        Log.e("123",IP.CONSTANT+"images/"+dataSource.get(position));
        final ObjectAnimator anim = ObjectAnimator.ofInt(viewHolder.imgHomework, "ImageLevel", 0,10000);
        anim.setDuration(3000);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(ObjectAnimator.INFINITE);
        anim.start();
        if(dataSource.get(position)!=null){
            Glide.with(context).load(IP.CONSTANT+"images/"+dataSource.get(position))
                    .placeholder(R.drawable.rotate_loading)
                    .error(R.drawable.fail)
                    .into(viewHolder.imgHomework);
        }else{
            viewHolder.imgHomework.setImageDrawable(context.getResources().getDrawable(R.drawable.wait));
        }
        return convertView;
    }
    private class ViewHolder{
        ImageView imgHomework;
    }
}
