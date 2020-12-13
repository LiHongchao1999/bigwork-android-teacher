package com.example.homeworkcorrectteacher;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.homeworkcorrectteacher.adapter.CustomImgListAdapter;
import com.wildma.pictureselector.PictureBean;
import com.wildma.pictureselector.PictureSelector;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

public class FinishedHomeworkDetailActivity extends AppCompatActivity {
    private ScrollableGridView gridView1;//自己上传的
    private CustomImgListAdapter selfResult;
    private List<String> selfSend;//自己上传的
    private static final String IMG_ADD= "add"; //添加图片
    private ImageView backBtn;
    private EditText editText;//学生注释
    private OkHttpClient okHttpClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished_homework_detail);
        backBtn = findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        gridView1 = findViewById(R.id.result_image_gv);

        selfSend = new ArrayList<>();
        selfResult = new CustomImgListAdapter(this,selfSend,R.layout.img_list_item);
        gridView1.setAdapter(selfResult);
    }
}