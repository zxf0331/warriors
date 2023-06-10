package com.warriors.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.warriors.R;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        ImageView button1 = findViewById(R.id.image1);
        ImageView button3 = findViewById(R.id.image3);

        //底部导航栏按钮点击事件
        button1.setOnClickListener(v -> {
            Intent intent = new Intent(SecondActivity.this, FirstActivity.class);
            startActivity(intent);
        });

        button3.setOnClickListener(v -> {
            Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
            startActivity(intent);
        });


        //视频播放器 WebView1
        WebView webView1 = findViewById(R.id.webView1);
        webView1.loadUrl("https://warriorsgo.oss-cn-chengdu.aliyuncs.com/vdo/GoldenStateChampionSeason%20.mp3");

        //视频播放器 WebView2
        WebView webView2 = findViewById(R.id.webView2);
        webView2.loadUrl("https://warriorsgo.oss-cn-chengdu.aliyuncs.com/vdo/warriorsGO.mp3");

    }
}
