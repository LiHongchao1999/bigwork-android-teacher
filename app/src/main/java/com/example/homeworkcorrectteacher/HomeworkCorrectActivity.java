package com.example.homeworkcorrectteacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homeworkcorrectteacher.adapter.ImageAdapter;
import com.example.homeworkcorrectteacher.entity.Homework;

import org.w3c.dom.Text;

public class HomeworkCorrectActivity extends AppCompatActivity {
    private Button checkBtn;
    private Homework homework;
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