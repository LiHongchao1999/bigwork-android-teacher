package com.example.homeworkcorrectteacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homeworkcorrectteacher.adapter.ImageAdapter;
import com.example.homeworkcorrectteacher.entity.Homework;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeworkCorrectActivity extends AppCompatActivity {
    private Button checkBtn;
    private Homework homework;
    private OkHttpClient okHttpClient = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_correct);
        final Intent intent = getIntent();
        homework  = (Homework) intent.getSerializableExtra("homework");
        checkBtn = findViewById(R.id.checkdetail);
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog.Builder builder = new CustomDialog.Builder(HomeworkCorrectActivity.this);
                builder.setMessage("点击确定即表示接单，请在规定时间内完成批改");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
//                        Toast.makeText(HomeworkCorrectActivity.this,"您已接单",Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(HomeworkCorrectActivity.this,LastCorrectActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                        finish();
                        UpdateHomeworkTag();
                        WhetherStartMarking();
                    }
                });

                builder.setNegativeButton("取消",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(HomeworkCorrectActivity.this,"您已取消",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });

                builder.create().show();
            }
        });
        TextView tvTime = findViewById(R.id.time);
        TextView tvDeadline = findViewById(R.id.deadline);
        TextView tvSubject = findViewById(R.id.subject);
        TextView tvMoney = findViewById(R.id.tv_money);
        ScrollableGridView gvImage = findViewById(R.id.gridview111);
        tvTime.setText(homework.getSubmitTime());
        tvDeadline.setText(homework.getDeadline());
        tvSubject.setText(homework.getHomeworkType());
        //tvMoney.setText(homework.getMoney()+"");
        ImageAdapter imageAdapter = new ImageAdapter(this,homework.getHomework_image(),R.layout.show_image_item_layout);
        gvImage.setAdapter(imageAdapter);
        gvImage.setVerticalSpacing(10);
        gvImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                new ShowImagesDialog(HomeworkCorrectActivity.this,homework.getHomework_image(),i).show();
            }
        });

    }

    private void UpdateHomeworkTag() {

        String url = "";
        url = IP.CONSTANT+ "UpdateHomeworkTagServlet?id="+homework.getId()+"&teacher_id=1";
        Log.e("这是地址",url);
        //RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"),new Gson().toJson(homework));
        Request request = new Request.Builder().get().url(url).build();
        //3、创建Call对象，发送请求，并且接受响应数据
        final Call call = okHttpClient.newCall(request);
        //不需要手动创建多线程
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败时回调的方法
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //请求成功时回调的方法
                Log.e("异步请求的结果",response.body().string());
                homework.setTeacher_id(1);
                homework.setTag("批改中");

            }
        });
    }


    public void WhetherStartMarking(){
        CustomDialog.Builder builder = new CustomDialog.Builder(HomeworkCorrectActivity.this);
        builder.setMessage("是否立即进行批改");
        builder.setTitle("提示");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(HomeworkCorrectActivity.this,CorrectHomeworkActivity.class);
                intent.putExtra("homework",homework);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        builder.setNegativeButton("否",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(HomeworkCorrectActivity.this,"您已取消",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

        builder.create().show();




    }

}