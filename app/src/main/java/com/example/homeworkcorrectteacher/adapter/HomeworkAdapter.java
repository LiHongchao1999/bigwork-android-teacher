package com.example.homeworkcorrectteacher.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.homeworkcorrectteacher.HomeworkCorrectActivity;
import com.example.homeworkcorrectteacher.IP;
import com.example.homeworkcorrectteacher.R;
import com.example.homeworkcorrectteacher.entity.Homework;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;


public class HomeworkAdapter extends BaseAdapter {

    private OkHttpClient okHttpClient;
    private Context context;
    private List<Homework> dataSource = new ArrayList<>();
    private int id;

    public HomeworkAdapter(Context context, List<Homework> dataSource, int id) {
        this.context = context;
        this.dataSource = dataSource;
        this.id = id;
        this.okHttpClient = new OkHttpClient();
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
            viewHolder.imgHomework = convertView.findViewById(R.id.img_homework);
            viewHolder.tvSubmitTime = convertView.findViewById(R.id.tv_submit_time);
            viewHolder.btnCheckDetail = convertView.findViewById(R.id.checkdetail);
            viewHolder.deadlineTime = convertView.findViewById(R.id.transparentPrompt);
            convertView.setTag(viewHolder);
        }else {
            viewHolder =(ViewHolder) convertView.getTag();
        }
        //Log.e("12312",dataSource.get(position).getHomework_image().toString());
        Glide.with(context).load(IP.CONSTANT+"images/"+dataSource.get(position).getHomework_image().get(0)).into(viewHolder.imgHomework);
        viewHolder.tvSubmitTime.setText(dataSource.get(position).getSubmitTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(dataSource.get(position).getDeadline());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long current = System.currentTimeMillis();
        long minutes = (date.getTime()-current)/60000;
        int day = (int)minutes/60/24;
        int hour = (int)(minutes/60-day*24);
        int minute = (int)(minutes%60);
        viewHolder.deadlineTime.setText("还有"+day+"天"+hour+"小时"+minute+"分钟截止");
        viewHolder.btnCheckDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HomeworkCorrectActivity.class);
                intent.putExtra("homework",dataSource.get(position));
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    private class ViewHolder{
        public Button btnCheckDetail;
        private ImageView imgHomework;
        private TextView deadlineTime;
        private TextView tvSubmitTime;
    }
}
