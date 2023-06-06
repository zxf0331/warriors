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
    private ImageView button1, button2, button3, button4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        button1 = findViewById(R.id.image1);
        button2 = findViewById(R.id.image2);
//        button3 = findViewById(R.id.image3);
        button4 = findViewById(R.id.image4);

        //底部导航栏按钮点击事件
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, FirstActivity.class);
                startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, SecondActivity.class);
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
        //使用WebView加载网页
        webView1.loadUrl("https://highlight-video.cdn.bcebos.com/video/6s/bc025288-c30e-11ed-b34b-7cd30ac11b5a.mp4?v_from_s=bdapp-landingpage-api-hbf");
        //允许在应用程序中播放视频
        WebSettings webSettings1 = webView1.getSettings();
        webSettings1.setJavaScriptEnabled(true);
        webSettings1.setMediaPlaybackRequiresUserGesture(false);

        /**
         * 视频播放器2
         */
        WebView webView2 = findViewById(R.id.webView2);
        //使用WebView加载网页
        webView2.loadUrl("https://highlight-video.cdn.bcebos.com/video/6s/ff9222f0-e88c-11ed-a7f4-7cd30a615b70.mp4?v_from_s=bdapp-landingpage-api-tide-hbe");
        //允许在应用程序中播放视频
        WebSettings webSettings2 = webView2.getSettings();
        webSettings2.setJavaScriptEnabled(true);
        webSettings2.setMediaPlaybackRequiresUserGesture(false);

    }
}
