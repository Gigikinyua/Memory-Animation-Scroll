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
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.jemimah.memory_timeline.R;
import com.jemimah.memory_timeline.adapter.CustomAdapter;
import com.jemimah.memory_timeline.model.DataModel;

import java.util.ArrayList;

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

    OnSwipeTouchListener onSwipeTouchListener;

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
        onSwipeTouchListener = new OnSwipeTouchListener(context, container);
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

        ArrayList<DataModel> memories = adapter.getData();

        inflateViews(canvas, memories);

        makeRadGrad(canvas);

    }

    private void drawCurve(Canvas canvas) {
        path = new Path();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(9);
        path.moveTo(0, (float) (screenHeight * 0.9857));
        path.cubicTo(
                (float) (screenWidth * 1.7), (float) (screenHeight * 0.95),
                (float) (screenWidth * 0.0714), (float) (screenHeight * 0.6992),
                (float) (screenWidth * 0.6142), (float) (screenHeight * 0.5833)
        );

        path.cubicTo(
                (float) (screenWidth * 1.4), (float) (screenHeight * 0.4167),
                (float) (screenWidth * 0.3), (float) (screenHeight * 0.3333),
                screenWidth, (float) (screenHeight * 0.185)
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
                    context.getColor(R.color.gradient), Color.TRANSPARENT, Shader.TileMode.CLAMP);
        }
        Paint paint = new Paint();
        paint.setShader(shader);
        canvas.drawRect(new RectF(0, 0, screenWidth, screenHeight), paint);
    }

    private void inflateViews(Canvas canvas, @NonNull ArrayList<DataModel> memories) {

        for (int i = 0; i < memories.size(); i++) {
            View view = adapter.getView(i, null, null);

            Bitmap  bitmap = loadBitmapFromView(view, screenWidth_ / 2, (int) (screenHeight_ / 3.5));

            if (i == 0) {
                canvas.drawBitmap(bitmap, (float) (screenWidth * 0.1), (float) (screenHeight * 0.64), paint);
            } else if (i == 1) {
                Bitmap b2 = Bitmap.createScaledBitmap(
                        bitmap, (int) (screenWidth / (2.5)), (int) (screenHeight / (3.5)), false);

                canvas.drawBitmap(b2, (float) (screenWidth * 0.52), (float) (screenHeight * 0.67), paint);
            } else if (i == 2) {
                Bitmap b3 = Bitmap.createScaledBitmap(
                        bitmap, screenWidth / 3, screenHeight / 4, false);

                canvas.drawBitmap(b3, (float) (screenWidth * 0.395), (float) (screenHeight * 0.368), paint);
            } else if (i == 3) {
                Bitmap b4 = Bitmap.createScaledBitmap(
                        bitmap, (int) (screenWidth / 3.5), (int) (screenHeight / 4.5), false);

                canvas.drawBitmap(b4, (float) (screenWidth * 0.776), (float) (screenHeight * 0.239), paint);
            } else if (i == 4) {
                Bitmap b5 = Bitmap.createScaledBitmap(
                        bitmap, (int) (screenWidth / 4), (int) (screenHeight / 5), false);

                canvas.drawBitmap(b5, (float) (screenWidth * 0.66), (float) (screenHeight * 0.07), paint);
            }
        }
        /*View view1 = adapter.getView(0, null, null);
        View view2 = adapter.getView(1, null, null);
        View view3 = adapter.getView(2, null, null);
        View view4 = adapter.getView(3, null, null);
        View view5 = adapter.getView(4, null, null);

        Bitmap b1 = loadBitmapFromView(view1, screenWidth_ / 2, (int) (screenHeight_ / 3.5));
        Bitmap b2 = loadBitmapFromView(view2, screenWidth_ / 2, (int) (screenHeight_ / 3.5));
        Bitmap b3 = loadBitmapFromView(view3, screenWidth_ / 2, (int) (screenHeight_ / 3.5));
        Bitmap b4 = loadBitmapFromView(view4, screenWidth_ / 2, (int) (screenHeight_ / 3.5));
        Bitmap b5 = loadBitmapFromView(view5, screenWidth_ / 2, (int) (screenHeight_ / 3.5));

        b2 = Bitmap.createScaledBitmap(
                b2, (int) (screenWidth / (2.5)), (int) (screenHeight / (3.5)), false);

        b3 = Bitmap.createScaledBitmap(
                b3, screenWidth / 3, screenHeight / 4, false);

        b4 = Bitmap.createScaledBitmap(
                b4, (int) (screenWidth / 3.5), (int) (screenHeight / 4.5), false);

        b5 = Bitmap.createScaledBitmap(
                b5, (int) (screenWidth / 4), (int) (screenHeight / 5), false);

        canvas.drawBitmap(b1, (float) (screenWidth * 0.1), (float) (screenHeight * 0.64), paint);
        canvas.drawBitmap(b2, (float) (screenWidth * 0.52), (float) (screenHeight * 0.67), paint);
        canvas.drawBitmap(b3, (float) (screenWidth * 0.395), (float) (screenHeight * 0.368), paint);
        canvas.drawBitmap(b4, (float) (screenWidth * 0.776), (float) (screenHeight * 0.239), paint);
        canvas.drawBitmap(b5, (float) (screenWidth * 0.66), (float) (screenHeight * 0.07), paint);*/
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

    public static class OnSwipeTouchListener implements View.OnTouchListener {
        private final GestureDetector gestureDetector;
        Context context;

        OnSwipeTouchListener(Context ctx, View mainView) {
            gestureDetector = new GestureDetector(ctx, new GestureListener());
            mainView.setOnTouchListener(this);
            context = ctx;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        public class GestureListener extends
                GestureDetector.SimpleOnGestureListener {
            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                            result = true;
                        }
                    }
                    else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom();
                        } else {
                            onSwipeTop();
                        }
                        result = true;
                    }
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }
        void onSwipeRight() {
            Toast.makeText(context, "Swiped Right", Toast.LENGTH_SHORT).show();
            this.onSwipe.swipeRight();
        }
        void onSwipeLeft() {
            Toast.makeText(context, "Swiped Left", Toast.LENGTH_SHORT).show();
            this.onSwipe.swipeLeft();
        }
        void onSwipeTop() {
            Toast.makeText(context, "Swiped Up", Toast.LENGTH_SHORT).show();
            this.onSwipe.swipeTop();
        }
        void onSwipeBottom() {
            Toast.makeText(context, "Swiped Down", Toast.LENGTH_SHORT).show();
            this.onSwipe.swipeBottom();
        }
        interface onSwipeListener {
            void swipeRight();
            void swipeTop();
            void swipeBottom();
            void swipeLeft();
        }
        onSwipeListener onSwipe;
    }
}
