package com.example.homeworkcorrectteacher.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.homeworkcorrectteacher.CorrectingHomeworkDetailActivity;
import com.example.homeworkcorrectteacher.IP;
import com.example.homeworkcorrectteacher.R;
import com.example.homeworkcorrectteacher.entity.Homework;

import java.util.List;

public class CorrectingHomeworkAdapter extends BaseAdapter {
    private Context mContext;
    private List<Homework> homeworks;
    private int itemLayoutRes;
    public CorrectingHomeworkAdapter(Context mContext, List<Homework> homeworks, int msg_list_item) {
        this.mContext = mContext;
        this.homeworks = homeworks;
        this.itemLayoutRes = msg_list_item;
    }

    @Override
    public int getCount() {
        if (homeworks!=null){
            return homeworks.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return homeworks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(itemLayoutRes,null);
            viewHolder = new ViewHolder();
            viewHolder.time = convertView.findViewById(R.id.question_time);
            viewHolder.img = convertView.findViewById(R.id.question_img_first);
            viewHolder.continueBtn = convertView.findViewById(R.id.continuebtn);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.time.setText(homeworks.get(position).getSubmitTime());
        Glide.with(convertView).load(IP.CONSTANT+"images/"+homeworks.get(position).getHomework_image().get(0)).into(viewHolder.img);
        viewHolder.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CorrectingHomeworkDetailActivity.class);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }
    private class ViewHolder{
        TextView time;
        ImageView img;
        Button continueBtn;
    }
}
