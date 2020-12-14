package com.example.homeworkcorrectteacher.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Adapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.homeworkcorrectteacher.ConversationListActivity;
import com.example.homeworkcorrectteacher.IP;
import com.example.homeworkcorrectteacher.MyViewPager;
import com.example.homeworkcorrectteacher.R;
import com.example.homeworkcorrectteacher.ScrollableGridView;
import com.example.homeworkcorrectteacher.adapter.HomeworkAdapter;
import com.example.homeworkcorrectteacher.entity.Homework;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RewardFragment extends Fragment implements View.OnClickListener {
    private ImageView background;
    private MyViewPager viewPager;
    private ArrayList<View> pageview;
    private ImageView conversation;//消息列表
    private TextView englishText;
    private TextView mathText;
    // 滚动条图片
    private ImageView scrollbar;
    // 滚动条初始偏移量
    private int offset = 0;
    // 当前页编号
    private int currIndex = 0;
    // 滚动条宽度
    private int bmpW;
    //一倍滚动量
    private int one;
    private OkHttpClient okHttpClient;
    private ScrollableGridView gvHomework;
    private View math;
    private View english;
    private HomeworkAdapter homeworkAdapter;

    public RewardFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reward, container, false);
        viewPager = view.findViewById(R.id.viewpager);
        okHttpClient = new OkHttpClient();
        conversation = view.findViewById(R.id.iv_ring);
        conversation.setOnClickListener(this);
        //查找布局文件用LayoutInflater.inflate
        english = inflater.inflate(R.layout.view_english, null);
        math = inflater.inflate(R.layout.view_math, null);
        englishText = view.findViewById(R.id.english);
        mathText = view.findViewById(R.id.math);
        scrollbar = view.findViewById(R.id.scroll);
        englishText.setOnClickListener(this);
        mathText.setOnClickListener(this);

        pageview =new ArrayList<View>();
        //添加想要切换的界面
        pageview.add(math);
        pageview.add(english);
        //数据适配器
        PagerAdapter mPagerAdapter = new PagerAdapter(){

            @Override
            //获取当前窗体界面数
            public int getCount() {
                // TODO Auto-generated method stub
                return pageview.size();
            }

            @Override
            //判断是否由对象生成界面
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0==arg1;
            }
            //使从ViewGroup中移出当前View
            public void destroyItem(View arg0, int arg1, Object arg2) {
                ((ViewPager) arg0).removeView(pageview.get(arg1));
            }

            //返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
            public Object instantiateItem(View arg0, int arg1){
                ((ViewPager)arg0).addView(pageview.get(arg1));
                return pageview.get(arg1);
            }
        };
        //绑定适配器
        viewPager.setAdapter(mPagerAdapter);
        //设置viewPager的初始界面为第一个界面
        viewPager.setCurrentItem(0);
        gvHomework = math.findViewById(R.id.math_grid);
        gvHomework.setHorizontalSpacing(25);
        gvHomework.setVerticalSpacing(25);
        getHomeworkOfSpecificSubject("math");
        //添加切换界面的监听器

        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        // 获取滚动条的宽度
        //bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.scrollbar).getWidth();
        //为了获取屏幕宽度，新建一个DisplayMetrics对象
        DisplayMetrics displayMetrics = new DisplayMetrics();
        //将当前窗口的一些信息放在DisplayMetrics类中
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //得到屏幕的宽度
        int screenW = displayMetrics.widthPixels;
        //计算出滚动条初始的偏移量
        offset = (screenW / 2 - bmpW) / 2;
        //计算出切换一个界面时，滚动条的位移量
        one = offset * 2 + bmpW;
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        //将滚动条的初始位置设置成与左边界间隔一个offset
        scrollbar.setImageMatrix(matrix);
        return view;
    }
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    /**
                     * TranslateAnimation的四个属性分别为
                     * float fromXDelta 动画开始的点离当前View X坐标上的差值
                     * float toXDelta 动画结束的点离当前View X坐标上的差值
                     * float fromYDelta 动画开始的点离当前View Y坐标上的差值
                     * float toYDelta 动画开始的点离当前View Y坐标上的差值
                     **/
                    animation = new TranslateAnimation(one, 0, 0, 0);
                    break;
                case 1:
                    animation = new TranslateAnimation(offset, one, 0, 0);
                    gvHomework = english.findViewById(R.id.english_grid);
                    gvHomework.setHorizontalSpacing(20);
                    gvHomework.setVerticalSpacing(20);
                    getHomeworkOfSpecificSubject("english");
                    break;
            }
            //arg0为切换到的页的编码
            currIndex = arg0;
            // 将此属性设置为true可以使得图片停在动画结束时的位置
            animation.setFillAfter(true);
            //动画持续时间，单位为毫秒
            animation.setDuration(200);
            //滚动条开始动画
            scrollbar.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.math:
                //点击"视频“时切换到第一页
                viewPager.setCurrentItem(0);
                gvHomework = math.findViewById(R.id.math_grid);
                gvHomework.setHorizontalSpacing(15);
                gvHomework.setVerticalSpacing(15);
                getHomeworkOfSpecificSubject("math");
                break;
            case R.id.english:
                //点击“音乐”时切换的第二页
                viewPager.setCurrentItem(1);
                gvHomework = english.findViewById(R.id.english_grid);
                gvHomework.setHorizontalSpacing(15);
                gvHomework.setVerticalSpacing(15);
                getHomeworkOfSpecificSubject("english");
                break;
            case R.id.iv_ring://消息
                //跳转到会话列表页面
                Intent intent = new Intent(getContext(), ConversationListActivity.class);
                startActivity(intent);
                break;
        }
    }
    public void getHomeworkOfSpecificSubject(String subject){
        final Request request = new Request.Builder().url(IP.CONSTANT+"GetHomeworkListServlet?tag=waitCorrect&homeworkType="+subject).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败时回调的方法
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String homeworkListJson = response.body().string();
                ArrayList<Homework> homeworkList = new Gson().fromJson(homeworkListJson,new TypeToken<ArrayList<Homework>>(){}.getType());
                homeworkAdapter = new HomeworkAdapter(getContext(),homeworkList, R.layout.homework_list_item);
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        gvHomework.setAdapter(homeworkAdapter);
                    }
                });
            }
        });
    }
}