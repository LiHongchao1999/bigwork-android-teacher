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
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.homeworkcorrectteacher.entity.Homework;
import com.xinlan.imageeditlibrary.editimage.EditImageActivity;
import com.xinlan.imageeditlibrary.editimage.utils.BitmapUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;

public class CorrectHomeworkActivity extends AppCompatActivity {
    private Homework homework;
    private List<String> homework_image;//作业图片
    private List<String> result_image;//结果图片
    private int image_size;//图片总张数
    private int image_current = 1;//当前是第几张
    private String newFilePath;

    private ImageView imgView;
    private String url = "";
    private Button editImage;//
    private String path;//图片路径
    private Bitmap myResource;
    private Bitmap mainBitmap;
    private int imageWidth, imageHeight;//
    public static final int ACTION_REQUEST_EDITIMAGE = 9;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correct_homework);
        initView();

    }

    private void initView() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        imageWidth = metrics.widthPixels;
        imageHeight = metrics.heightPixels;

        Intent intent = getIntent();
        homework = (Homework) intent.getSerializableExtra("homework");
        Log.e("homework", homework.toString());
        homework_image = homework.getHomework_image();
        image_size = homework_image.size();
        Log.e("url", url);

        detailImage();

    }


    public void detailImage() {
        if (image_current > image_size) {
            Toast.makeText(this, "作业批改完成",
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            url = IP.CONSTANT + "images/" + homework_image.get(image_current - 1);
            Glide.with(CorrectHomeworkActivity.this)
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // System.out.println("RESULT_OK");
            switch (requestCode) {
                case ACTION_REQUEST_EDITIMAGE://
                    handleEditorImage(data);
                    break;
            }// end switch
        }
    }

    private void handleEditorImage(Intent data) {
        newFilePath = data.getStringExtra(EditImageActivity.EXTRA_OUTPUT);
        boolean isImageEdit = data.getBooleanExtra(EditImageActivity.IMAGE_IS_EDIT, false);

        if (isImageEdit) {
            //Toast.makeText(this, getString(R.string.save_path, newFilePath), Toast.LENGTH_LONG).show();
            Log.e("保存的路径newFilePath", newFilePath);
            image_current ++;
            detailImage();
        } else {//未编辑  还是用原来的图片
            newFilePath = data.getStringExtra(EditImageActivity.FILE_PATH);
            Log.e("未编辑还是用原来的图片path", newFilePath);
        }


        //System.out.println("newFilePath---->" + newFilePath);
        //File file = new File(newFilePath);
        //System.out.println("newFilePath size ---->" + (file.length() / 1024)+"KB");
//        Log.d("image is edit", isImageEdit + "");
//        LoadImageTask loadTask = new LoadImageTask();
//        loadTask.execute(newFilePath);
    }


    private final class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            return BitmapUtils.getSampledBitmap(params[0], imageWidth / 4, imageHeight / 4);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        protected void onCancelled(Bitmap result) {
            super.onCancelled(result);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if (mainBitmap != null) {
                mainBitmap.recycle();
                mainBitmap = null;
                System.gc();
            }
            mainBitmap = result;
            //imgView.setImageBitmap(mainBitmap);
        }
    }// end inner class


}