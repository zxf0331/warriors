package com.warriors.tool;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.warriors.R;

public class NewsDetailActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView titleTextView;
    private TextView dateTextView;
    private TextView contentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        // 获取传递过来的新闻数据
        String imageUrl = getIntent().getStringExtra("imageUrl");
        String title = getIntent().getStringExtra("title");
        String date = getIntent().getStringExtra("date");
        String content = getIntent().getStringExtra("content");

        // 初始化视图
        imageView = findViewById(R.id.imageView);
        titleTextView = findViewById(R.id.titleTextView);
        dateTextView = findViewById(R.id.dateTextView);
        contentTextView = findViewById(R.id.contentTextView);

        // 使用Glide加载图片
        Glide.with(this)
                .load(imageUrl)
                .into(imageView);

        // 设置新闻标题、日期和内容
        titleTextView.setText(title);
        dateTextView.setText(date);
        contentTextView.setText(content);
    }
}