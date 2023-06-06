package com.qm.draw;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.qm.R;

public class HeartActivity extends AppCompatActivity {
    private Button drawHeartButton;
    private HeartView heartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actiivity_heart);
        drawHeartButton = findViewById(R.id.draw_heart_button);
        heartView = findViewById(R.id.heart_view);
        drawHeartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heartView.drawHeart();
            }
        });


        //返回到FirstActivity按钮
        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
