package com.qm.pages;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.qm.R;
import com.qm.draw.HeartActivity;
import com.qm.music.LoveActivity;
import com.qm.music.SlidePagerAdapter;

public class FirstActivity extends AppCompatActivity {
    private ImageView button1, button2, button3, button4;
    //动画帧序列ImageView
    private ImageView mAnimationImageView;
    private ViewPager viewPager;
    private SlidePagerAdapter slidePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        //设置适配器
        viewPager = findViewById(R.id.viewPager);
        slidePagerAdapter = new SlidePagerAdapter(this);
        viewPager.setAdapter(slidePagerAdapter);

        button1 = findViewById(R.id.image1);
        button2 = findViewById(R.id.image2);
//        button3 = findViewById(R.id.image3);
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

        //页面跳转到画爱心页面
        Button button6 = findViewById(R.id.button2);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this, HeartActivity.class);
                startActivity(intent);
            }
        });





        /**
         * 创建一个九张图的逐帧动画，并将其循环播放。
         */
        mAnimationImageView = findViewById(R.id.animationImageView);
        mAnimationImageView.post(new Runnable() {
            @Override
            public void run() {
                //将获取 ImageView 控件的 Drawable 对象强制转换为 AnimationDrawable 对象
                AnimationDrawable animation = (AnimationDrawable) mAnimationImageView.getDrawable();
                //调用 AnimationDrawable 的 start() 方法以开始播放动画
                animation.start();
            }
        });
    }

}

