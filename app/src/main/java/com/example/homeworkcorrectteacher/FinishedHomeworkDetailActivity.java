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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homeworkcorrectteacher.adapter.CustomImgListAdapter;
import com.example.homeworkcorrectteacher.adapter.ImageAdapter;
import com.example.homeworkcorrectteacher.adapter.NumberAdapter;
import com.example.homeworkcorrectteacher.adapter.ShowImagesAdapter;
import com.example.homeworkcorrectteacher.entity.Homework;
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
    private Button submitBtn;
    private List<String> ordinaryImages;
    private List<String> correctedImages;
    private List<String> resultImages;
    private MyListView numberListView;
    private MyListView beforeCorrectedListView;
    private MyListView afterCorrectedListView;
    private GridView resultImageGv;
    private TextView correctText;
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

        Intent intent = getIntent();
        Homework homework = (Homework)intent.getSerializableExtra("finishedHomework");
        ordinaryImages = homework.getHomework_image();
        ImageAdapter imageAdapter = new ImageAdapter(this,ordinaryImages,R.layout.show_image_item_layout);

        numberListView = findViewById(R.id.number);
        List<Integer> integers = new ArrayList<>();
        for(int i=1;i<=ordinaryImages.size();i++){integers.add(i);}
        NumberAdapter adapter = new NumberAdapter(this,integers,R.layout.nums_item);
        numberListView.setAdapter(adapter);


        beforeCorrectedListView = findViewById(R.id.before);
        beforeCorrectedListView.setAdapter(imageAdapter);
        beforeCorrectedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                new ShowImagesDialog(FinishedHomeworkDetailActivity.this,ordinaryImages,i).show();
            }
        });

        correctedImages = homework.getResult_image();
        ImageAdapter imageAdapter1 = new ImageAdapter(this,correctedImages,R.layout.show_image_item_layout);
        afterCorrectedListView = findViewById(R.id.after);
        afterCorrectedListView.setAdapter(imageAdapter1);
        afterCorrectedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                new ShowImagesDialog(FinishedHomeworkDetailActivity.this,correctedImages,i).show();
            }
        });

        backBtn = findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        resultImageGv = findViewById(R.id.result_image_gv);
        resultImages = homework.getResult_image_teacher();
        ImageAdapter teacherAdapter = new ImageAdapter(this,resultImages,R.layout.show_image_item_layout);
        resultImageGv.setAdapter(teacherAdapter);
        resultImageGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                new ShowImagesDialog(FinishedHomeworkDetailActivity.this,resultImages,i).show();
            }
        });

        correctText = findViewById(R.id.correct_self_text);
        correctText.setText(homework.getResult_text());
    }
}