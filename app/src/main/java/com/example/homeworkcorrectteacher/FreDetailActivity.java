package com.example.homeworkcorrectteacher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class FreDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fre_detail);
    }
    /*
    * 单机事件
    * */
    public void onClicked(View view) {
        switch (view.getId()){
            case R.id.fre_back:
                finish();
                break;
            case R.id.delete_fre://删除好友
                //通过数据库删除
                Intent intent = new Intent();
                setResult(200,intent);
                finish();
                break;
        }
    }
}