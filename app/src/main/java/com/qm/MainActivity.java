package com.qm;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

import com.qm.database.DBHelper;
import com.qm.database.SPHelper;
import com.qm.pages.FirstActivity;


/**
 * 登录用户名：zhangsan
 * 登录密码：123456
 */
public class MainActivity extends AppCompatActivity {

    //申明变量
    private static final String TAG = "MainActivity";

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;
    private Button mRegisterButton;
    private CheckBox cb_save;//记住密码
    private DBHelper mDBHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //实例化变量
        mDBHelper = new DBHelper(this);
        mUsernameEditText = findViewById(R.id.username_edit_text);
        mPasswordEditText = findViewById(R.id.password_edit_text);
        mLoginButton = findViewById(R.id.login_button);
        mRegisterButton = findViewById(R.id.register_button);
        cb_save = findViewById(R.id.cb_save);//保存密码

        //获取是否存在记住密码过的账号密码 如果没有 默认返回空字符串(账号&密码)/false(记住密码)
        SharedPreferences sp = getSharedPreferences("users", MODE_PRIVATE);

        String loginName = sp.getString("loginName", "");
        String loginPassword = sp.getString("loginPassword", "");
        boolean isSave = sp.getBoolean("isSave", false);
        //用上面获取的先前保存数据填充账号密码输入框和记住密码checkbox
        mUsernameEditText.setText(loginName);
        mPasswordEditText.setText(loginPassword);
        cb_save.setChecked(isSave);



        //点击登录
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取输入框用户名密码和 为空则提醒 以及记住密码状态
                String username = mUsernameEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();
                boolean checked = cb_save.isChecked();
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(MainActivity.this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                //调用登录函数 登录成功跳转主页
                boolean success = login(username, password);
                SharedPreferences.Editor editor = getSharedPreferences("users", MODE_PRIVATE).edit();
                if (success) {
                    //todo：设置SharedPreference自动登录 如果登录成功 保存账号密码信息到对象
                    editor.putString("loginName",username);
                    editor.putString("loginPassword",checked?password:"");//勾选记住，保存密码。不勾选，保存空字符串。
                    editor.putBoolean("isSave",checked);
                    editor.apply();//保存数据
                    Toast.makeText(MainActivity.this, "欢迎"+username+"登录", Toast.LENGTH_SHORT).show();
                    //跳转
                    Intent intent = new Intent(MainActivity.this, FirstActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //点击注册
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsernameEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(MainActivity.this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                //调用注册函数
                boolean success = register(username, password);
                if (success) {
                    Toast.makeText(MainActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "用户名已存在", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 用户登录方法
     * @param username 用户名
     * @param password 密码
     * @return 登录是否成功
     */
    private boolean login(String username, String password) {
        //创建SQLiteDatabase示例
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        //1.需要查询的表列结构
        String[] projection = {
                DBHelper.UserEntry._ID,
                DBHelper.UserEntry.COLUMN_NAME_USERNAME,
                DBHelper.UserEntry.COLUMN_NAME_PASSWORD
        };
        //2.定义条件查询语句结构
        String selection = DBHelper.UserEntry.COLUMN_NAME_USERNAME + " = ?";
        //3.定义结构中所需参数替换占位符
        String[] selectionArgs = {username};
        //执行查询 db传入123 返回结果集
        Cursor cursor = db.query(
                DBHelper.UserEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor != null && cursor.moveToFirst()) {
            String storedPassword = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.UserEntry.COLUMN_NAME_PASSWORD));
            cursor.close();
            //获取返回”用户的密码“并比较输入密码 返回布尔值表示登录成功与否
            return storedPassword.equals(password);
        } else {
            if (cursor != null) {
                cursor.close();
            }
            return false;
        }
    }

    /**
     * 用户注册方法
     * @param username 用户名
     * @param password 密码
     * @return 是否注册成功
     * 此方法用于向 SQLite 数据库中插入一条新用户数据，包括用户名和密码。
     * 如果插入成功，则返回 true，否则返回 false。
     */
    private boolean register(String username, String password) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.UserEntry.COLUMN_NAME_USERNAME, username);
        values.put(DBHelper.UserEntry.COLUMN_NAME_PASSWORD, password);
        long newRowId = db.insert(DBHelper.UserEntry.TABLE_NAME, null, values);
        return newRowId != -1;
    }
}
