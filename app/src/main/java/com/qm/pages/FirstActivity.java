package com.qm.pages;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.qm.R;
import com.qm.music.LoveActivity;
import com.qm.music.SlidePagerAdapter;

public class FirstActivity extends AppCompatActivity {
    private ImageView button1, button2, button3, button4;
    //动画帧序列ImageView
    private ImageView mAnimationImageView;
    private ViewPager viewPager;
    private SlidePagerAdapter slidePagerAdapter;

    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        //设置适配器
        viewPager = findViewById(R.id.viewPager);
        slidePagerAdapter = new SlidePagerAdapter(this);
        viewPager.setAdapter(slidePagerAdapter);

        // 创建一个Runnable对象，用于自动切换轮播图项
        runnable = new Runnable() {
            public void run() {
                int currentItem = viewPager.getCurrentItem();
                int totalItems = slidePagerAdapter.getCount();

                // 检查当前项是否为最后一项，如果是，则切换到第一项；否则，切换到下一项
                if (currentItem == totalItems - 1) {
                    viewPager.setCurrentItem(0);
                } else {
                    viewPager.setCurrentItem(currentItem + 1);
                }

                // 延迟一定时间后再次执行自动切换
                handler.postDelayed(this, 2000); // 设置自动播放间隔时间
            }
        };

        // 开始自动播放
        handler.postDelayed(runnable, 2000); // 设置自动播放初始延迟时间


        button1 = findViewById(R.id.image1);
        button2 = findViewById(R.id.image2);
        button4 = findViewById(R.id.image4);


        //底部导航栏
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this, FirstActivity.class);
                startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });


        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this, ThirdActivity.class);
                startActivity(intent);
            }
        });




        //页面跳转到qq音乐页面
        Button button5 = findViewById(R.id.button);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this, LoveActivity.class);
                startActivity(intent);
            }
        });
    }

}

