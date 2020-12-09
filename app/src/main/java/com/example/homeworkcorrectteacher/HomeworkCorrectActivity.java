package com.example.homeworkcorrectteacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.example.homeworkcorrectteacher.adapter.ImageAdapter;
import com.example.homeworkcorrectteacher.entity.Homework;

import org.w3c.dom.Text;

public class HomeworkCorrectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_correct);
        Intent intent = getIntent();
        final Homework homework  = (Homework) intent.getSerializableExtra("homework");
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

}