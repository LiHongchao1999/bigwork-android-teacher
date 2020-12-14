package com.example.homeworkcorrectteacher;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.homeworkcorrectteacher.adapter.CustomImgListAdapter;
import com.example.homeworkcorrectteacher.adapter.ImageAdapter;
import com.example.homeworkcorrectteacher.adapter.NumberAdapter;
import com.example.homeworkcorrectteacher.entity.Homework;
import com.wildma.pictureselector.PictureBean;
import com.wildma.pictureselector.PictureSelector;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class CorrectingHomeworkDetailActivity extends AppCompatActivity {
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
    private MyListView numberListView;
    private MyListView beforeCorrectedListView;
    private MyListView afterCorrectedListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correcting_homework_detail);
        Intent intent = getIntent();
        Homework homework = (Homework)intent.getSerializableExtra("correctingHomework");
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
                new ShowImagesDialog(CorrectingHomeworkDetailActivity.this,ordinaryImages,i).show();
            }
        });

        correctedImages = homework.getResult_image();
        List<String> imgs = new ArrayList<>();
        for(int i=0;i<ordinaryImages.size();i++){
            if(i<correctedImages.size()){
                imgs.add(correctedImages.get(i));
            }else{
                imgs.add(null);
            }
        }
        ImageAdapter imageAdapter1 = new ImageAdapter(this,imgs,R.layout.show_image_item_layout);
        afterCorrectedListView = findViewById(R.id.after);
        afterCorrectedListView.setAdapter(imageAdapter1);
        afterCorrectedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i<correctedImages.size()){
                    new ShowImagesDialog(CorrectingHomeworkDetailActivity.this,correctedImages,i).show();
                }
            }
        });

        backBtn = findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        gridView1 = findViewById(R.id.result_image_gv);
        submitBtn = findViewById(R.id.submit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDialog infoDialog = new InfoDialog.Builder(CorrectingHomeworkDetailActivity.this,R.layout.info_dialog_green)
                        .setTitle("Done")
                        .setMessage("提交成功")
                        .setButton("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(CorrectingHomeworkDetailActivity.this,MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                        ).create();
                infoDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_white);
                infoDialog.show();
            }
        });
        selfSend = new ArrayList<>();
        selfSend.add(IMG_ADD);//添加
        selfResult = new CustomImgListAdapter(this,selfSend,R.layout.img_list_item);
        gridView1.setAdapter(selfResult);
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(selfSend.get(position).equals("add")){//进行选择
                    PictureSelector.create(CorrectingHomeworkDetailActivity.this,
                            PictureSelector.SELECT_REQUEST_CODE)
                            .selectPicture(true);
                }else{
                    List<String> urls = new ArrayList<String>();
                    for(int j=0;j<selfSend.size()-1;j++){
                        urls.add(selfSend.get(j));
                    }
                    new ShowLocalImageDialog(CorrectingHomeworkDetailActivity.this,urls,position).show();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== PictureSelector.SELECT_REQUEST_CODE){
            if (data!=null){
                PictureBean pictureBean = data.getParcelableExtra(PictureSelector.PICTURE_RESULT);
                removeItem();
                if(pictureBean.isCut()){
                    selfSend.add(pictureBean.getPath());
                    Log.e("路径",pictureBean.getPath());
                    selfSend.add(IMG_ADD);
                    selfResult.notifyDataSetChanged();
                }else {
                    String path = ImageTool.getRealPathFromUri(this,pictureBean.getUri());
                    Log.e("路径1",pictureBean.getPath());
                    selfSend.add(IMG_ADD);
                    selfResult.notifyDataSetChanged();
                }
            }else {
                Toast.makeText(this,"您没有选择图片",Toast.LENGTH_LONG).show();
            }
            Log.e("123213",selfSend.toString());
        }
    }
    private void removeItem() {
        if (selfSend.size() != 9) {
            if (selfSend.size() != 0) {
                selfSend.remove(selfSend.size() - 1);
            }
        }
    }
    public void correctedHomework(Homework homework){
        Homework correctedHomework = homework;
        correctedHomework.setResult_image(correctedImages);

        Request request = new Request.Builder().url(IP.CONSTANT).build();
    }
}