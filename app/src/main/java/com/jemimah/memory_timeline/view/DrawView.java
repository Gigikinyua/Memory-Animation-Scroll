package com.jemimah.memory_timeline.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.jemimah.memory_timeline.R;
import com.jemimah.memory_timeline.adapter.CustomAdapter;

public class DrawView extends View {

    Paint paint;
    Path path;
    Context context;
    LinearLayout container;
    int screenWidth;
    int screenWidth_;
    int screenHeight;
    int screenHeight_;
    CustomAdapter adapter;

    float firstX_point, firstY_point;

    public DrawView(Context context) {
        super(context);
        init(context);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public DrawView(Context context, CustomAdapter adapter, LinearLayout container) {
        super(context);
        this.context = context;
        this.adapter = adapter;
        this.container = container;
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();

        paint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        screenHeight = container.getHeight();
        screenHeight_ = (int) (screenHeight * 1.3081);
        screenWidth = container.getWidth();
        screenWidth_ = (int) (screenWidth * 0.9722);

        super.onDraw(canvas);

        drawCurve(canvas);

        View view1 = adapter.getView(0, null, null);
        View view2 = adapter.getView(1, null, null);
        View view3 = adapter.getView(2, null, null);
        View view4 = adapter.getView(3, null, null);

        Bitmap b1 = loadBitmapFromView(view1, screenWidth_ / 2, screenHeight_ / 3);
        Bitmap b2 = loadBitmapFromView(view2, screenWidth_ / 2, screenHeight_ / 3);
        Bitmap b3 = loadBitmapFromView(view4, screenWidth_ / 2, screenHeight_ / 3);
        Bitmap b4 = loadBitmapFromView(view3, screenWidth_ / 2, screenHeight_ / 3);

        b3 = Bitmap.createScaledBitmap(
                b3, screenWidth / (4 * 1), screenHeight / (5 * 1), false);

        b4 = Bitmap.createScaledBitmap(
                b4, (int) (screenWidth / (4)), (int) (screenHeight / (5)), false);

        b2 = Bitmap.createScaledBitmap(
                b2, (int) (screenWidth / (3)), screenHeight / (4), false);
        canvas.drawBitmap(b1, (float) (screenWidth * 0.0012), (float) (screenHeight * 0.58), paint);
        canvas.drawBitmap(b2, (float) (screenWidth * 0.4142), (float) (screenHeight * 0.70), paint);
        canvas.drawBitmap(b3, (float) (screenWidth * 0.614), (float) (screenHeight * 0.14), paint);
        canvas.drawBitmap(b4, (float) (screenWidth * 0.3642), (float) (screenHeight * 0.4357), paint);

        makeRadGrad(canvas);

    }

    private void drawCurve(Canvas canvas) {
        path = new Path();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(10);
        path.moveTo(0, (float) (screenHeight * 0.9857));
        path.cubicTo(
                (float) (screenWidth * 1.7), (float) (screenHeight * 0.95),
                (float) (screenWidth * 0.0714), (float) (screenHeight * 0.6992),
                (float) (screenWidth * 0.6142), (float) (screenHeight * 0.5833)
        );

        path.cubicTo(
                (float) (screenWidth * 1.4), (float) (screenHeight * 0.4167),
                (float) (screenWidth * 0.3), (float) (screenHeight * 0.3333),
                screenWidth, (float) (screenHeight * 0.2)
        );
        canvas.drawPath(path, paint);

        /* path.cubicTo(1200, 1200, 100, 1000, 300, 900);
        path.cubicTo(1000, 900, 300, 800, 400, 700);
        canvas.drawPath(path, paint);*/
    }

    private void makeRadGrad(Canvas canvas) {
        double angleInRadians = Math.toRadians(90);


        double endX = Math.cos(angleInRadians) * screenWidth;
        double endY = Math.sin(angleInRadians) * screenHeight;
        Shader shader = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            shader = new LinearGradient(0, (float) (screenHeight / 4) * 2,
                    (float) endX, (float) endY,
                    context.getColor(R.color.dark_grey), Color.TRANSPARENT, Shader.TileMode.CLAMP);
        }
        Paint paint = new Paint();
        paint.setShader(shader);
        canvas.drawRect(new RectF(0, 0, screenWidth, screenHeight), paint);
    }

    public static Bitmap loadBitmapFromView(View view, int width, int height) {

        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
        view.measure(measuredWidth, measuredHeight);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);

        view.draw(c);

        return b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();

        switch (action) {

            case MotionEvent.ACTION_DOWN:
                firstX_point = event.getRawX();
                firstY_point = event.getRawY();
                break;

            case MotionEvent.ACTION_UP:

                float finalX = event.getRawX();
                float finalY = event.getRawY();

                int distanceX = (int) (finalX - firstX_point);
                int distanceY = (int) (finalY - firstY_point);

                if (Math.abs(distanceX) > Math.abs(distanceY)) {
                    if ((firstX_point < finalX)) {
                        Log.d("Test", "Left to Right swipe performed");
                        Toast.makeText(context, "Left to Right swipe performed", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("Test", "Right to Left swipe performed");
                        Toast.makeText(context, "Right to Left swipe performed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if ((firstY_point < finalY)) {
                        Log.d("Test", "Up to Down swipe performed");
                        Toast.makeText(context, "Up to Down swipe performed", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("Test", "Down to Up swipe performed");
                        Toast.makeText(context, "Down to Up swipe performed", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }

        return true;
    }

}
