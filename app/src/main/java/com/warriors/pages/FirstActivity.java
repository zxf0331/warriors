package com.warriors.pages;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.warriors.R;
import com.warriors.entertain.EntertainActivity;
import com.warriors.tool.SlidePagerAdapter;
import com.warriors.tool.NewsDetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import java.io.IOException;

public class FirstActivity extends AppCompatActivity {
    private ImageView button1, button2, button3, button4;
    //动画帧序列ImageView
    private ImageView mAnimationImageView;
    private ViewPager viewPager;
    private SlidePagerAdapter slidePagerAdapter;

    private Handler handler = new Handler();
    private Runnable runnable;

    private void fetchDataAndDisplay() {
        // 创建OkHttpClient实例
        OkHttpClient client = new OkHttpClient();

        // 创建请求对象
        Request request = new Request.Builder()
                .url("https://warriorsgo.oss-cn-chengdu.aliyuncs.com/data.json")
                .build();

        // 发起异步请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 处理请求失败的情况
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // 获取响应数据
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        final String responseData = responseBody.string();

                        // 在主线程更新UI
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LinearLayout newsLayout = findViewById(R.id.newsLayout);

                                try {
                                    JSONObject json = new JSONObject(responseData);
                                    JSONArray data = json.getJSONArray("data");

                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject item = data.getJSONObject(i);
                                        String title = item.getString("title");
                                        String imageUrl = item.getString("url");
                                        String date = item.getString("date");
                                        String content = item.getString("content");
                                        //final NewsItem newsItem = new NewsItem(title, imageUrl, date, content);

                                        // 创建新闻条目布局
                                        LinearLayout newsItemLayout = new LinearLayout(FirstActivity.this);
                                        newsItemLayout.setOrientation(LinearLayout.HORIZONTAL); // 设置水平方向布局
                                        newsItemLayout.setPadding(16, 16, 16, 16);

                                        // 创建ImageView并使用Glide加载图片
                                        ImageView imageView = new ImageView(FirstActivity.this);
                                        LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(
                                                400, LinearLayout.LayoutParams.MATCH_PARENT); // 调整图片宽度为200dp
                                        imageView.setLayoutParams(imageLayoutParams);
                                        Glide.with(FirstActivity.this)
                                                .load(imageUrl)
                                                .into(imageView);

                                        // 创建垂直方向的LinearLayout容纳新闻标题和日期
                                        LinearLayout textLayout = new LinearLayout(FirstActivity.this);
                                        textLayout.setOrientation(LinearLayout.VERTICAL);

                                        // 创建TextView用于显示新闻标题
                                        TextView titleTextView = new TextView(FirstActivity.this);
                                        LinearLayout.LayoutParams titleLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                        titleTextView.setLayoutParams(titleLayoutParams);
                                        titleTextView.setText(title);

                                        // 创建TextView用于显示新闻日期
                                        TextView dateTextView = new TextView(FirstActivity.this);
                                        LinearLayout.LayoutParams dateLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                        dateLayoutParams.topMargin = 16; // 调整日期与标题的间距
                                        dateTextView.setLayoutParams(dateLayoutParams);
                                        dateTextView.setText(date);

                                        // 将新闻标题和日期添加到垂直布局中
                                        textLayout.addView(titleTextView);
                                        textLayout.addView(dateTextView);

                                        // 将ImageView和垂直布局添加到新闻条目布局中
                                        newsItemLayout.addView(imageView);
                                        newsItemLayout.addView(textLayout);

                                        // 将新闻条目布局添加到主布局中
                                        newsLayout.addView(newsItemLayout);

                                        newsItemLayout.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                // 创建意图，并将新闻数据传递给NewsDetailActivity
                                                Intent intent = new Intent(FirstActivity.this, NewsDetailActivity.class);
                                                intent.putExtra("imageUrl", imageUrl);
                                                intent.putExtra("title", title);
                                                intent.putExtra("date", date);
                                                intent.putExtra("content", content);

                                                // 启动NewsDetailActivity
                                                startActivity(intent);
                                            }
                                        });

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } else {
                    // 处理响应错误的情况
                    // ...
                }

                // 关闭响应体
                if (response.body() != null) {
                    response.body().close();
                }
            }
        });
    }

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
                Intent intent = new Intent(FirstActivity.this, EntertainActivity.class);
                startActivity(intent);
            }
        });
        // 自动获取数据并展示
        fetchDataAndDisplay();
    }
}

