package com.warriors.music;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.warriors.R;

public class LoveActivity extends AppCompatActivity implements View.OnClickListener {
    //创建需要用到的控件的变量
    private TextView tv1, tv2;
    private FragmentManager fm;
    private FragmentTransaction ft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love);

        //返回FirstActivity按钮
        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(LoveActivity.this, FirstActivity.class);
//                startActivity(intent);
                finish();
            }
        });

        //绑定控件
        tv1 = (TextView) findViewById(R.id.menu1);
        tv2 = (TextView) findViewById(R.id.menu2);
        //设置监听器，固定写法
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        //若是继承FragmentActivity，fm=getFragmentManger();
        fm = getSupportFragmentManager();
        //fm可以理解为Fragment显示的管理者，ft就是它的改变者
        ft = fm.beginTransaction();
        //默认情况下就显示frag1
        ft.replace(R.id.content, new frag1());
        //提交改变的内容
        ft.commit();
    }

    @Override
    //控件的点击事件
    public void onClick(View v) {
        ft = fm.beginTransaction();
        //切换选项卡
        switch (v.getId()) {
            case R.id.menu1:
                ft.replace(R.id.content, new frag1());
                break;
            case R.id.menu2:
                ft.replace(R.id.content, new frag2());
                break;
            default:
                break;
        }
        ft.commit();
    }
}


