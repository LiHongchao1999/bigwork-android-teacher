package com.example.homeworkcorrectteacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.homeworkcorrectteacher.adapter.CorrectingHomeworkAdapter;
import com.example.homeworkcorrectteacher.adapter.FinishedHomeworkAdapter;
import com.example.homeworkcorrectteacher.cache.IP;
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

public class FinishedHomeworkActivity extends AppCompatActivity {
    private MyListView myListView;
    private OkHttpClient okHttpClient;
    private Context context;
    private FinishedHomeworkAdapter homeworkAdapter;
    private ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished_homework);
        myListView = findViewById(R.id.finished);
        okHttpClient = new OkHttpClient();
        backBtn = findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        context=this;
        getAllCorrectedHomeworkOfTeacher();
    }
    public void getAllCorrectedHomeworkOfTeacher(){
        Request request = new Request.Builder().url(IP.CONSTANT+"GetIsCorrectingHomeworkServlet?tag=hasCorrected&teacherId=1").build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String homeworkListJson = response.body().string();
                ArrayList<Homework> homeworkList = new Gson().fromJson(homeworkListJson,new TypeToken<ArrayList<Homework>>(){}.getType());
                homeworkAdapter = new FinishedHomeworkAdapter(context,homeworkList, R.layout.finished_homework_item);
                runOnUiThread(new Runnable() {
                    public void run() {
                        myListView.setAdapter(homeworkAdapter);
                    }
                });
            }
        });
    }
}