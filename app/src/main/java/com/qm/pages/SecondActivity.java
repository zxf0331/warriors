package com.qm.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.qm.R;

public class SecondActivity extends AppCompatActivity {
    private ImageView button1,button4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        button1 = findViewById(R.id.image1);

        button4 = findViewById(R.id.image4);

        //底部导航栏按钮点击事件
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, FirstActivity.class);
                startActivity(intent);
            }
        });



        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                startActivity(intent);
            }
        });


        /**
         * 视频播放器1
         */
        WebView webView1 = findViewById(R.id.webView1);
        webView1.getSettings().setMediaPlaybackRequiresUserGesture(true);

        //使用WebView加载网页
        webView1.loadUrl("https://warriorsgo.oss-cn-chengdu.aliyuncs.com/vdo/GoldenStateChampionSeason%20.mp4");
        //允许在应用程序中播放视频
//        WebSettings webSettings1 = webView1.getSettings();
//        webSettings1.setJavaScriptEnabled(true);
//        webSettings1.setMediaPlaybackRequiresUserGesture(false);

        /**
         * 视频播放器2
         */
        WebView webView2 = findViewById(R.id.webView2);
        webView2.getSettings().setMediaPlaybackRequiresUserGesture(true);
        //使用WebView加载网页
        webView2.loadUrl("https://warriorsgo.oss-cn-chengdu.aliyuncs.com/vdo/GetTheChampion.mp4");
        //允许在应用程序中播放视频
//        WebSettings webSettings2 = webView2.getSettings();
//        webSettings2.setJavaScriptEnabled(true);
//        webSettings2.setMediaPlaybackRequiresUserGesture(true);

    }
}
