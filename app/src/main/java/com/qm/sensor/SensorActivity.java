package com.qm.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.qm.R;

import java.util.List;

public class SensorActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        /**
         * 返回FourthActivity按钮
         */
        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        /**
         * 显示手机内置传感器类型及其相关属性信息
         */
        //使用 SensorManager 和 LinearLayout 控件来动态添加 TextView 控件，并将传感器信息显示在其中。
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        //对于每个传感器对象，将创建一个新的 TextView 控件
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor : sensorList) {
            String sensorName = sensor.getName();
            String sensorVendor = sensor.getVendor();
            String sensorType = String.valueOf(sensor.getType());
            String sensorResolution = String.valueOf(sensor.getResolution());
            String sensorPower = String.valueOf(sensor.getPower());

            //传感器的名称、制造商、类型、分辨率和功耗信息设置为该 TextView 控件的文本
            TextView textView = new TextView(this);
            textView.setText("传感器的名称: " + sensorName + "\n" +
                    "制造商: " + sensorVendor + "\n" +
                    "类型: " + sensorType + "\n" +
                    "分辨率: " + sensorResolution + "\n" +
                    "功耗信息: " + sensorPower + "\n");
            //将该 TextView 控件添加到 LinearLayout 控件中，以便将其显示在 ScrollView 中
            linearLayout.addView(textView);
        }


    }
}
