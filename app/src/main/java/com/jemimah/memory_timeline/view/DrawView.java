package com.jemimah.memory_timeline.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class DrawView extends View {

    Paint paint;
    Path path;

    public DrawView(Context context) {
        super(context);
        init();
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        paint = new Paint();

        paint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        path = new Path();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(20);
        path.moveTo(0, 1200);
        path.cubicTo(1200, 1200, 100, 1000, 300, 900);
        path.cubicTo(1000, 900, 300, 800, 400, 700);
        canvas.drawPath(path, paint);
        paint.setColor(Color.GREEN);
        canvas.drawRect(200, 1000, 77, 1200, paint );
        canvas.drawRect(400, 1000, 477, 1200, paint );
        canvas.drawRect(250, 740, 327, 900, paint );
        canvas.drawRect(550, 740, 627, 860, paint );
    }
}
