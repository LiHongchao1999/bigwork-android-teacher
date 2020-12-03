package com.example.homeworkcorrectteacher;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.homeworkcorrectteacher.fragment.MessageFragment;
import com.example.homeworkcorrectteacher.fragment.MyFragment;
import com.example.homeworkcorrectteacher.fragment.RewardFragment;

public class MainActivity extends AppCompatActivity {
    private Fragment currentFragment = new Fragment(); //当前fragment
    private RewardFragment rewardFragment; //悬赏页面
    private MessageFragment messageFragment; //消息页面
    private MyFragment myFragment; //个人页面
    
    private ImageView mainImg;
    private ImageView messageImg;
    private ImageView mineImg;
    
    private TextView mainText;
    private TextView messageText;
    private TextView mineText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取控件
        getViews();
        //获取fragment对象
        rewardFragment = new RewardFragment();
        messageFragment = new MessageFragment();
        myFragment = new MyFragment();
        //设置当前页
        changeTeb(rewardFragment);
        currentFragment = rewardFragment;
        mainImg.setImageResource(R.drawable.moneyr);
        mainText.setTextColor(Color.rgb(255,48,48));
    }
    private void getViews() {
        mainImg = findViewById(R.id.main_img);
        mainText = findViewById(R.id.main_text);
        messageImg = findViewById(R.id.message_img);
        messageText = findViewById(R.id.message_text);
        mineImg = findViewById(R.id.mine_img);
        mineText = findViewById(R.id.mine_text);
    }

    private void changeTeb(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (currentFragment != fragment) {
            if (!fragment.isAdded()) {
                transaction.add(R.id.tab_content, fragment);
            }
            transaction.hide(currentFragment);
            transaction.show(fragment);
            transaction.commit();
            currentFragment = fragment;
        }
    }
    /*
     * 单机事件
     * */
    public void onClicked(View view) {
        switch (view.getId()){
            case R.id.main://主页
                changeTeb(rewardFragment);
                mainImg.setImageResource(R.drawable.moneyr);
                mainText.setTextColor(Color.rgb(255,48,48));
                messageImg.setImageResource(R.drawable.messageb);
                messageText.setTextColor(Color.BLACK);
                mineImg.setImageResource(R.drawable.myinfob);
                mineText.setTextColor(Color.BLACK);
                break;
            case R.id.shop: //商城
                changeTeb(messageFragment);
                mainImg.setImageResource(R.drawable.moneyb);
                mainText.setTextColor(Color.BLACK);
                messageImg.setImageResource(R.drawable.messager);
                messageText.setTextColor(Color.rgb(255,48,48));
                mineImg.setImageResource(R.drawable.myinfob);
                mineText.setTextColor(Color.BLACK);
                break;
            case R.id.mine: //我的
                changeTeb(myFragment);
                mainImg.setImageResource(R.drawable.moneyb);
                mainText.setTextColor(Color.BLACK);
                messageImg.setImageResource(R.drawable.messageb);
                messageText.setTextColor(Color.BLACK);
                mineImg.setImageResource(R.drawable.myinfor);
                mineText.setTextColor(Color.rgb(255,48,48));
                break;
        }
    }
}