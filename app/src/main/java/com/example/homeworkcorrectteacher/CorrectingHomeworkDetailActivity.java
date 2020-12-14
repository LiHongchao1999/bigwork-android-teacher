package com.example.homeworkcorrectteacher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.homeworkcorrectteacher.adapter.CustomImgListAdapter;
import com.example.homeworkcorrectteacher.adapter.ImageAdapter;
import com.example.homeworkcorrectteacher.adapter.NumberAdapter;
import com.example.homeworkcorrectteacher.entity.Homework;
import com.google.gson.Gson;
import com.wildma.pictureselector.PictureBean;
import com.wildma.pictureselector.PictureSelector;
import com.xinlan.imageeditlibrary.editimage.EditImageActivity;
import com.xinlan.imageeditlibrary.editimage.utils.BitmapUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.homeworkcorrectteacher.CorrectHomeworkActivity.ACTION_REQUEST_EDITIMAGE;

public class CorrectingHomeworkDetailActivity extends AppCompatActivity {
    private ScrollableGridView gridView1;//自己上传的
    private CustomImgListAdapter selfResult;
    private List<String> selfSend;//自己上传的
    private static final String IMG_ADD = "add"; //添加图片
    private ImageView backBtn;
    private EditText editText;//学生注释
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Button submitBtn;
    private Button continueBtn;
    private List<String> ordinaryImages;
    private List<String> correctedImages = new ArrayList<>();
    private MyListView numberListView;
    private MyListView beforeCorrectedListView;
    private MyListView afterCorrectedListView;
    private Homework homework;
    private int image_size;//图片z总张数
    private int image_current = 1;//当前是第几张
    private int z;//循环次数
    private int i = 1;
    private int o = 1;
    private int k = 0;
    private int p = 0;
    private String url = "";
    private String newFilePath;
    private String path;//图片路径
    private ImageAdapter imageAdapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correcting_homework_detail);
        Intent intent = getIntent();
        homework = (Homework) intent.getSerializableExtra("correctingHomework");
        ordinaryImages = homework.getHomework_image();
        image_size = homework.getHomework_image().size();
        ImageAdapter imageAdapter = new ImageAdapter(this, ordinaryImages, R.layout.show_image_item_layout);

        numberListView = findViewById(R.id.number);
        List<Integer> integers = new ArrayList<>();
        for (int i = 1; i <= ordinaryImages.size(); i++) {
            integers.add(i);
        }
        NumberAdapter adapter = new NumberAdapter(this, integers, R.layout.nums_item);
        numberListView.setAdapter(adapter);


        beforeCorrectedListView = findViewById(R.id.before);
        beforeCorrectedListView.setAdapter(imageAdapter);
        beforeCorrectedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                new ShowImagesDialog(CorrectingHomeworkDetailActivity.this, ordinaryImages, i).show();
            }
        });

        if (homework.getResult_image() != null){
            correctedImages = homework.getResult_image();
        }
        image_current = correctedImages.size();
        if (image_size == image_current){
            p = 1;
        }
        z = image_size - image_current - 1;
        List<String> imgs = new ArrayList<>();
        for (int i = 0; i < ordinaryImages.size(); i++) {
            if (i < correctedImages.size()) {
                imgs.add(correctedImages.get(i));
            } else {
                imgs.add(null);
            }
        }
        imageAdapter1 = new ImageAdapter(this, imgs, R.layout.show_image_item_layout);
        afterCorrectedListView = findViewById(R.id.after);
        afterCorrectedListView.setAdapter(imageAdapter1);
        afterCorrectedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i < correctedImages.size()) {
                    new ShowImagesDialog(CorrectingHomeworkDetailActivity.this, correctedImages, i).show();
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
        continueBtn = findViewById(R.id.continuebtn);

        //继续批改按钮
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    detailImage(image_current);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDialog infoDialog = new InfoDialog.Builder(CorrectingHomeworkDetailActivity.this, R.layout.info_dialog_green)
                        .setTitle("Done")
                        .setMessage("提交成功")
                        .setButton("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(CorrectingHomeworkDetailActivity.this, MainActivity.class);
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
        selfResult = new CustomImgListAdapter(this, selfSend, R.layout.img_list_item);
        gridView1.setAdapter(selfResult);
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selfSend.get(position).equals("add")) {//进行选择
                    PictureSelector.create(CorrectingHomeworkDetailActivity.this,
                            PictureSelector.SELECT_REQUEST_CODE)
                            .selectPicture(true);
                } else {
                    List<String> urls = new ArrayList<String>();
                    for (int j = 0; j < selfSend.size() - 1; j++) {
                        urls.add(selfSend.get(j));
                    }
                    new ShowLocalImageDialog(CorrectingHomeworkDetailActivity.this, urls, position).show();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                PictureBean pictureBean = data.getParcelableExtra(PictureSelector.PICTURE_RESULT);
                removeItem();
                if (pictureBean.isCut()) {
                    selfSend.add(pictureBean.getPath());
                    Log.e("路径", pictureBean.getPath());
                    selfSend.add(IMG_ADD);
                    selfResult.notifyDataSetChanged();
                } else {
                    String path = ImageTool.getRealPathFromUri(this, pictureBean.getUri());
                    Log.e("路径1", pictureBean.getPath());
                    selfSend.add(IMG_ADD);
                    selfResult.notifyDataSetChanged();
                }
            } else {
                Toast.makeText(this, "您没有选择图片", Toast.LENGTH_LONG).show();
            }
            Log.e("123213", selfSend.toString());
        }

        if (resultCode == RESULT_OK) {
            // System.out.println("RESULT_OK");
            switch (requestCode) {
                case ACTION_REQUEST_EDITIMAGE://
                    handleEditorImage(data);
                    break;
            }// end switch
        }
    }

    private void removeItem() {
        if (selfSend.size() != 9) {
            if (selfSend.size() != 0) {
                selfSend.remove(selfSend.size() - 1);
            }
        }
    }

    public void correctedHomework(Homework homework) {
        Homework correctedHomework = homework;
        correctedHomework.setResult_image(correctedImages);

        Request request = new Request.Builder().url(IP.CONSTANT).build();
    }

    public void detailImage(int current) {
        if (p ==1){
            Toast.makeText(this, "作业批改完成",
                    Toast.LENGTH_SHORT).show();
        }
        else if (image_current > image_size ) {
            Toast.makeText(this, "作业批改完成",
                    Toast.LENGTH_SHORT).show();
        } else {
            if (i == 1 && (current!= 0)) {//第一次修改
                url = IP.CONSTANT + "images/" + correctedImages.get(image_current - 1);
                i++;
            } else if (i == 1 && (current== 0)){
                url = IP.CONSTANT + "images/" + ordinaryImages.get(current);
                k =1;
                image_current++;
                i++;
            }
            else {
                url = IP.CONSTANT + "images/" + ordinaryImages.get(image_current - 1);
                Log.e("这是image的值",image_current+"");
            }
            Glide.with(CorrectingHomeworkDetailActivity.this)
                    .asBitmap()
                    .load(url)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            requestAllPower();
                            path = saveImage(resource);
                            Log.e("save_path", path);
                            //编辑图片
                            editImageClick();
                        }
                    });

        }

    }

    private String saveImage(Bitmap image) {
        String saveImagePath = null;
        Random random = new Random();
        String imageFileName = System.currentTimeMillis() + ".jpg";
        String dirName = "images";
        File storageDir = new File(this.getFilesDir(), dirName);
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
            Log.e("尝试创建文件夹", success + "");
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            saveImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fout = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fout);
                fout.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return saveImagePath;
    }

    private void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }


    public void requestAllPower() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    /**
     * 编辑选择的图片
     *
     * @author panyi
     */
    private void editImageClick() {
        String out_imageFileName = System.currentTimeMillis() + ".jpg";
        String out_dirName = "out_images";
        File storageDir = new File(this.getFilesDir(), out_dirName);
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
            Log.e("尝试创建文件夹", success + "");
        }
        File imageFile = new File(storageDir, out_imageFileName);
        File outputFile = FileUtils.genEditFile();
        EditImageActivity.start(this, path, imageFile.getAbsolutePath(), ACTION_REQUEST_EDITIMAGE);
    }


    private void handleEditorImage(Intent data) {
        newFilePath = data.getStringExtra(EditImageActivity.EXTRA_OUTPUT);
        boolean isImageEdit = data.getBooleanExtra(EditImageActivity.IMAGE_IS_EDIT, false);
        if (isImageEdit) {
            //Toast.makeText(this, getString(R.string.save_path, newFilePath), Toast.LENGTH_LONG).show();
            Log.e("保存的路径newFilePath", newFilePath);
        } else {//未编辑  还是用原来的图片
            newFilePath = data.getStringExtra(EditImageActivity.FILE_PATH);
            Log.e("未编辑还是用原来的图片path", newFilePath);
        }
        uploadImagesOfHomework(image_current);
        image_current++;
        detailImage(image_current);
    }


    //上传批改后的作业图片
    private void uploadImagesOfHomework(int current) {
        long time = Calendar.getInstance().getTimeInMillis();
        Log.e("获取到的时间", time + "");
        RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream"), new File(newFilePath));
        Log.e("list的内容", newFilePath);
        Request request = new Request.Builder().post(body).url(IP.CONSTANT + "UploadHomeworkResultImageServlet?imgName=" + time + ".jpg").build();
        if (o ==1 && (k!=1)){
            correctedImages.remove(current-1);
            correctedImages.add(time + ".jpg");
            o++;
        }else {
            correctedImages.add(time + ".jpg");
        }
        homework.setResult_image(correctedImages);

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            //图片上传完成
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                UpdateWorkInfo();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<String> imgs = new ArrayList<>();
                        for (int i = 0; i < ordinaryImages.size(); i++) {
                            if (i < correctedImages.size()) {
                                imgs.add(correctedImages.get(i));
                            } else {
                                imgs.add(null);
                            }
                        }
                        imageAdapter1 = new ImageAdapter(CorrectingHomeworkDetailActivity.this, imgs, R.layout.show_image_item_layout);
                        afterCorrectedListView.setAdapter(imageAdapter1);
                    }
                });

            }
        });
    }


    private void UpdateWorkInfo() {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), new Gson().toJson(homework));
        Request request = new Request.Builder().post(requestBody).url(IP.CONSTANT + "UpdateWorkInfoServlet?tag=1").build();
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
                Log.e("异步请求的结果", response.body().string());
            }
        });
    }


}