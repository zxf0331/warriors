package com.qm.pages;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.PermissionChecker;

import com.qm.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private TextView welcome;

    private TextView locationView;

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
        welcome = findViewById(R.id.welcome);//昵称欢迎
        locationView = findViewById(R.id.location);


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

        //获取用户名
        SharedPreferences users = getSharedPreferences("users", MODE_PRIVATE);
        String welcomeUser = "Welcome " + users.getString("loginName", "游客");
        welcome.setText(welcomeUser);

        //获取当前位置
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);//位置服务对象
        //1.检查权限
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Log.d("###权限###",isGPSEnabled+" "+isNetworkEnabled);
        if (!isGPSEnabled && !isNetworkEnabled) {
            // 用户未开启定位服务
            locationView.setText("手机未开启定位权限");
            //GPS未打开，跳转到GPS设置界面
            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 0);
        }
        //2.位置获取
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //位置变化时 获取新位置
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                String s = "经度：" + longitude + "\n" + "维度：" + latitude;
                locationView.setText(s);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
        //设置参数
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 设置定位精度
        criteria.setAltitudeRequired(false); // 不需要获取海拔信息
        criteria.setBearingRequired(false); // 不需要获取方向信息
        criteria.setCostAllowed(true); // 允许付费定位服务
        criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗定位

        String provider = locationManager.getBestProvider(criteria, true); // 获取最优的定位提供者
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        if (provider != null) {
            locationManager.requestLocationUpdates(provider, 1000, 0, locationListener); // 设置位置更新条件和监听器
        }

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
