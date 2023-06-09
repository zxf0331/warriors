package com.warriors.pages;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.warriors.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ThirdActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_SELECT = 2;

    private ImageView mProfileImageView;
    private Uri mImageUri;

    private TextView locationView;
    private final String MAPKEY = "SNZBZ-3PAWI-SW5GL-UXEBF-Y2VGK-MAF7V";
    public static final int SUCCESS = 1;
//    public static final int ERROR = 0;

    public ThirdActivity() {
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == SUCCESS){
                    //显示确切位置
                    CharSequence preText = locationView.getText();
                    locationView.setText(String.format("%s\n%s", preText, msg.obj));
                }
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        ImageView button1 = findViewById(R.id.image1);
        ImageView button2 = findViewById(R.id.image2);
        ImageView button4 = findViewById(R.id.image4);


        mProfileImageView = findViewById(R.id.profile_image_view);//头像
        Button mCameraButton = findViewById(R.id.camera_button);//相机
        Button mGalleryButton = findViewById(R.id.gallery_button);//相册
        Button mDownloadButton = findViewById(R.id.download_button);//上传
        TextView welcome = findViewById(R.id.welcome);//昵称欢迎
        locationView = findViewById(R.id.location);//定位TextView


        //添加两个Button，一个用于拍照，另一个用于从相册中选择图片。最后，添加一个Button，用于上传选择的图片。
        mCameraButton.setOnClickListener(v -> dispatchTakePictureIntent());

        mGalleryButton.setOnClickListener(v -> dispatchSelectPictureIntent());

        mDownloadButton.setOnClickListener(v -> downloadImage());


        //导航栏按钮点击事件
        button1.setOnClickListener(v -> {
            Intent intent = new Intent(ThirdActivity.this, FirstActivity.class);
            startActivity(intent);
        });

        button2.setOnClickListener(v -> {
            Intent intent = new Intent(ThirdActivity.this, SecondActivity.class);
            startActivity(intent);
        });


        button4.setOnClickListener(v -> {
            Intent intent = new Intent(ThirdActivity.this, ThirdActivity.class);
            startActivity(intent);
        });

        //获取用户名
        SharedPreferences users = getSharedPreferences("users", MODE_PRIVATE);
        String welcomeUser = "Welcome " +"\n"+ users.getString("loginName", "游客");
        welcome.setText(welcomeUser);

        //获取当前位置
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);//位置服务对象
        //0.位置点击事件 如果手机没开启定位则打开定位
        locationView.setOnClickListener(v -> {
            if(locationView.getText()=="手机未开启定位权限"){
                Toast.makeText(ThirdActivity.this,"请打开手机定位权限", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, 0);
            }
        });
        //1.检查权限
        this.checkLocationPermission(locationManager);
        //2.位置获取
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        LocationListener locationListener = new LocationListener() {
            private long count = 0L;
            @Override
            public void onLocationChanged(Location location) {
                if(count%100==0){
                    //位置变化时 获取新位置
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    getActualLocation(latitude,longitude);//子线程获取确切位置 并传递给 handler 将 locationView内容设置为地址
                    String s = String.format("经度：%s     纬度：%s", String.format("%.6f", longitude), String.format("%.6f", latitude));
                    Log.d("##",s);
                    locationView.setText(s);
                    count++;
                }
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        String provider = this.getProvider(locationManager);
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
                mImageUri = FileProvider.getUriForFile(this, "com.warriors.fileprovider", photoFile);
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
     * 随机路径：com.warriors.fileprovider/my_images/JPEG_20230507_041118_8564947155254924464.jpg
     */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //该方法使用 File.createTempFile() 方法创建一个新的临时文件，并将其保存到应用程序的外部文件目录中。
        // 这个临时文件的名称将包含刚刚生成的时间戳字符串，并以 ".jpg" 作为文件扩展名。
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        System.out.println("storageDir = " + storageDir);
        return File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
    }

    /**
     * onActivityResult() 是一个在 Android 应用程序中用于处理其他活动返回结果的方法
     * 如果请求代码等于 REQUEST_IMAGE_CAPTURE，
     * 并且结果代码等于 RESULT_OK，那么就表示拍照活动已经成功完成并返回了一个图像。
     *
     * @param requestCode ..
     * @param resultCode ..
     * @param data ..
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
     * 保存头像文件
     */
    private void downloadImage() {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        //todo：实现了文件存储里的外部存储 头像->相册
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.curry_head);
        String fileName = "Warriors_header.jpg";
        String filePath = ThirdActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + fileName;
        File file = new File(filePath);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), fileName, null);
            Toast.makeText(this, "图片保存成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "图片保存失败", Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(this, "已保存至相册", Toast.LENGTH_SHORT).show();
    }

    //检查手机是否开启定位权限
    private void checkLocationPermission(LocationManager locationManager){
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Log.d("###权限###",isGPSEnabled+" "+isNetworkEnabled);
        if (!isGPSEnabled && !isNetworkEnabled) {
            // 用户未开启定位服务
            locationView.setText("手机未开启定位权限");
        }
    }
    //获取位置provider
    private String getProvider(LocationManager locationManager){
        //设置参数
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 设置定位精度
        criteria.setAltitudeRequired(false); // 不需要获取海拔信息
        criteria.setBearingRequired(false); // 不需要获取方向信息
        criteria.setCostAllowed(true); // 允许付费定位服务
        criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗定位

        return locationManager.getBestProvider(criteria, true); // 获取最优的定位提供者
    }
    //hander
    private final Handler handler;
    //api获取位置
    private void getActualLocation(Double latitude,Double longitude){
        //不返回内容 创建子线程和主线程通信 Message
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL("https://apis.map.qq.com/ws/geocoder/v1/?location="+latitude+","+longitude+"&key="+MAPKEY);
                connection = (HttpURLConnection) url.openConnection();
                // 设置请求方法、超时时间等
                connection.setConnectTimeout(30000);//30s超时
                connection.connect();
                // 处理响应
                if(connection.getResponseCode()==200){
                    InputStream inputStream = connection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    char[] buffer = new char[3072];
                    reader.read(buffer);
                    String content = new String(buffer);
                    JSONObject json = new JSONObject(content);

                    JSONObject result = json.getJSONObject("result");
                    String addressBig = result.getString("address");
                    Message msg = new Message();
                    msg.what = SUCCESS;
                    String addressSmall;
                    if(73.5<longitude&&longitude<135&&4<latitude&&latitude<53.5){
                        addressSmall = result.getJSONObject("formatted_addresses").getString("recommend");
                    }else{
                        addressSmall = result.getJSONObject("address_component").getString("locality");
                    }
                    msg.obj = addressBig + addressSmall;

                    Log.d("##地址##",msg.obj.toString());
                    handler.sendMessage(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }

}
