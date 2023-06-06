package com.qm.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;


public class HeartView extends View {

    private Paint paint;
    private Path path;

    public HeartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
    }

    public void drawHeart() {
        // 清除之前绘制的路径
        path.reset();
        // 计算心形曲线的控制点坐标
        float x = getWidth() / 2;
        float y = getHeight() / 2 - 100;
        float r = 150;
        float c = (float) (0.551915024494 * r);
        path.moveTo(x, y);
        path.cubicTo(x + c, y - r, x + r, y - c, x + r, y);
        path.cubicTo(x + r, y + c, x + c, y + r, x, y + r);
        path.cubicTo(x - c, y + r, x - r, y + c, x - r, y);
        path.cubicTo(x - r, y - c, x - c, y - r, x, y - r);
        path.close();
        // 通知 View 进行重绘
        invalidate();
    }
}

