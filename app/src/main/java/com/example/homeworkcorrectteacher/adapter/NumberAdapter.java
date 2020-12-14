package com.example.homeworkcorrectteacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.homeworkcorrectteacher.R;

import java.util.List;

public class NumberAdapter extends BaseAdapter {
    private Context context;
    private List<Integer> nums;
    private int layoutRes;
    public NumberAdapter(Context context, List<Integer> nums,int layoutRes){
        this.context = context;
        this.nums = nums;
        this.layoutRes = layoutRes;
    }
    @Override
    public int getCount() {
        return nums.size();
    }

    @Override
    public Object getItem(int position) {
        return nums.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder();
        if(view==null){
            view = LayoutInflater.from(context).inflate(layoutRes,null);
            viewHolder = new ViewHolder();
            viewHolder.numText = view.findViewById(R.id.number);
            view.setTag(viewHolder);
        }else {
            viewHolder =(ViewHolder) view.getTag();
        }
        viewHolder.numText.setText(nums.get(i)+"");
        return view;
    }
    private class ViewHolder{
        TextView numText;
    }
}
