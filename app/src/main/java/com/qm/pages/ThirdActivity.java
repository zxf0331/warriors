package com.qm.pages;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.qm.R;
import com.qm.sensor.SensorActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ThirdActivity extends AppCompatActivity {
    private ImageView button1, button2, button3, button4;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_SELECT = 2;

    private ImageView mProfileImageView;
    private Button mCameraButton;
    private Button mGalleryButton;
    private Button mUploadButton;
    private Uri mImageUri;
    private Button button_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        button1 = findViewById(R.id.image1);
        button2 = findViewById(R.id.image2);
        button4 = findViewById(R.id.image4);


        mProfileImageView = findViewById(R.id.profile_image_view);//头像
        mCameraButton = findViewById(R.id.camera_button);//相机
        mGalleryButton = findViewById(R.id.gallery_button);//相册
        mUploadButton = findViewById(R.id.upload_button);//上传
        button_info = findViewById(R.id.button);//传感器信息



        //添加两个Button，一个用于拍照，另一个用于从相册中选择图片。最后，添加一个Button，用于上传选择的图片。
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        mGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchSelectPictureIntent();
            }
        });

        mUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });


        //导航栏按钮点击事件
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThirdActivity.this, FirstActivity.class);
                startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThirdActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });



        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThirdActivity.this, ThirdActivity.class);
                startActivity(intent);
            }
        });


        /**
         * 获取到手机上所有可用的传感器列表，并循环遍历该列表,打印到日志中
         */
        //获取 Android 手机上的传感器类型：
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor : sensorList) {
            Log.d("SENSOR", "名称: " + sensor.getName());
            Log.d("SENSOR", "类型: " + sensor.getType());
            Log.d("SENSOR", "供应商: " + sensor.getVendor());
            Log.d("SENSOR", "版本: " + sensor.getVersion());
            Log.d("SENSOR", "功耗: " + sensor.getPower());
            Log.d("SENSOR", "分辨率: " + sensor.getResolution());
            Log.d("SENSOR", "最大测量范围: " + sensor.getMaximumRange());
            Log.d("SENSOR", "最小延迟: " + sensor.getMinDelay());
        }


        //点击跳转至传感器页面
        button_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThirdActivity.this, SensorActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * 启动拍照意图（Intent）的方法
     */
    private void dispatchTakePictureIntent() {
        //创建了一个 ACTION_IMAGE_CAPTURE 意图，并将其传递给 startActivityForResult() 方法。
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //如果有一个相机应用程序可用于处理这个意图，它将打开相机应用程序并让用户拍摄照片。
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                //this 表示当前的 Context 对象
                // "fileprovider" 是在 AndroidManifest.xml 文件中定义的 FileProvider 的 authorities 属性的值，
                // photoFile 是要共享的本地文件。
                mImageUri = FileProvider.getUriForFile(this, "com.qm.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                //原理里面添加了动态权限，不能与本地权限有冲突
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    /**
     * 打开一个图库应用程序，允许用户选择图片，并将所选的图片返回给您的应用程序。
     */
    private void dispatchSelectPictureIntent() {
        Intent selectPictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(selectPictureIntent, REQUEST_IMAGE_SELECT);
    }

    /**
     * 创建图像文件的方法。这个方法的作用是创建一个新的 JPEG 格式的图像文件，并返回该文件的 File 对象
     * 随机路径：com.qm.fileprovider/my_images/JPEG_20230507_041118_8564947155254924464.jpg
     */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //该方法使用 File.createTempFile() 方法创建一个新的临时文件，并将其保存到应用程序的外部文件目录中。
        // 这个临时文件的名称将包含刚刚生成的时间戳字符串，并以 ".jpg" 作为文件扩展名。
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        System.out.println("storageDir = " + storageDir);
        File imageFile = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        return imageFile;
    }

    /**
     * onActivityResult() 是一个在 Android 应用程序中用于处理其他活动返回结果的方法
     * 如果请求代码等于 REQUEST_IMAGE_CAPTURE，
     * 并且结果代码等于 RESULT_OK，那么就表示拍照活动已经成功完成并返回了一个图像。
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            mProfileImageView.setImageURI(mImageUri);
        } else if (requestCode == REQUEST_IMAGE_SELECT && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            mProfileImageView.setImageURI(mImageUri);
        }
    }

    /**
     * 上传图像文件
     */
    private void uploadImage() {
        //检查成员变量 mImageUri 是否为 null
        //如果 mImageUri 为 null，则表示用户尚未选择要上传的图像文件
        if (mImageUri == null) {
            Toast.makeText(this, "请先选择照片", Toast.LENGTH_SHORT).show();
            return;
        }
        // 传照片至服务器
        Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show();
    }

}
