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
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.jemimah.memory_timeline.R;
import com.jemimah.memory_timeline.adapter.CustomAdapter;
import com.jemimah.memory_timeline.model.DataModel;

import java.util.ArrayList;

public class DrawView extends View {

    private static final String TAG = "DrawView";

    Paint paint;
    Path path;
    Context context;
    LinearLayout container;
    int screenWidth, screenWidth_, screenHeight, screenHeight_;
    public CustomAdapter adapter;

    Canvas myCanvas;

    boolean swipe = false;
    boolean isForward = false;

    GestureDetector gestureDetector;

    Handler handler;
    long delay = 200;

    ArrayList<HolderPointer> animationPoint = new ArrayList<>();
    ArrayList<DataModel> memories = new ArrayList<>();

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
        gestureDetector = new GestureDetector(context, new GestureListener());
        this.memories.addAll(adapter.getData());
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        this.myCanvas = canvas;
        drawAnimate(canvas);

        if (!swipe) {
            inflateViews(canvas, memories);
        }

        makeRadGrad(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        screenHeight = container.getHeight();
        screenWidth = container.getWidth();

        screenHeight_ = (int) (screenHeight * 1.3081);
        screenWidth_ = (int) (screenWidth * 0.9722);

        initAdd();
        drawCurve(canvas);

    }

    boolean isNotIndexed = true;

    void initAdd() {
        if (isNotIndexed) {
            animationPoint.add(new HolderPointer(
                    0,
                    0,
                    (float) (screenWidth * 0.1),
                    (float) (screenHeight * 0.64)
            ));
            animationPoint.add(new HolderPointer(
                    (int) (screenWidth / 2.5),
                    (int) (screenHeight / 3.5),
                    (float) (screenWidth * 0.52),
                    (float) (screenHeight * 0.67)
            ));
            animationPoint.add(new HolderPointer(
                    screenWidth / 3,
                    screenHeight / 4,
                    (float) (screenWidth * 0.395),
                    (float) (screenHeight * 0.368)
            ));
            animationPoint.add(new HolderPointer(
                    (int) (screenWidth / 3.5),
                    (int) (screenHeight / 4.5),
                    (float) (screenWidth * 0.776),
                    (float) (screenHeight * 0.239)
            ));
            animationPoint.add(new HolderPointer(
                    (int) (screenWidth / 4),
                    (int) (screenHeight / 5),
                    (float) (screenWidth * 0.66),
                    (float) (screenHeight * 0.07)
            ));
            isNotIndexed = false;
        }
    }

    int animationPosition = 4;

    private ArrayList<DataModel> drawAnimate(Canvas canvas) {
        if (isForward) {
            if (!swipe) {
                animationPosition = 4;
                return memories;
            }

            if (animationPosition < 0) {
                animationPosition = 4;
                swipe = false;
                return memories;
            }
        } else {
            if (!swipe) {
                animationPosition = 0;
                return memories;
            }

            if (animationPosition > 4) {
                animationPosition = 0;
                swipe = false;
                return memories;
            }
        }

        HolderPointer holderPointer = animationPoint.get(animationPosition);
        View view = adapter.getView(0, null, null);

        Log.d(TAG, "drawAnimate: " + holderPointer.dstHeight);

        Bitmap bitmap = loadBitmapFromView(view, screenWidth_ / 2, (int) (screenHeight_ / 3.5));

        if (holderPointer.dstWidth != 0 || holderPointer.dstHeight != 0) {
            bitmap = Bitmap.createScaledBitmap(
                    bitmap, holderPointer.dstWidth, holderPointer.dstHeight, false);
        }

        canvas.drawBitmap(bitmap, holderPointer.width, holderPointer.height, paint);

        Runnable runnable = () -> {
            if (isForward) {
                Log.d(TAG, "drawAnimate: pre " + animationPosition );
                animationPosition = animationPosition - 1;
                Log.d(TAG, "drawAnimate: post " + animationPosition );
            } else {
                Log.d(TAG, "drawAnimate: pre " + animationPosition );
                animationPosition = animationPosition + 1;
                Log.d(TAG, "drawAnimate: post " + animationPosition );
            }

            resetView();
        };
        handler = new Handler(context.getMainLooper());
        handler.postDelayed(runnable, delay);

        return null;
    }

    private void resetView() {
        this.postInvalidate();
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
            shader = new LinearGradient(0, (float) (screenHeight / 6) * 2,
                    (float) endX, (float) endY,
                    context.getColor(R.color.gradient), Color.TRANSPARENT, Shader.TileMode.CLAMP);
        }
        Paint paint = new Paint();
        paint.setShader(shader);
        canvas.drawRect(new RectF(0, 0, screenWidth, screenHeight), paint);
    }

    private void inflateViews(Canvas canvas, @NonNull ArrayList<DataModel> memories) {
        this.adapter = new CustomAdapter(memories, context);
        for (int i = 0; i < memories.size(); i++) {
            View view = adapter.getView(i, null, null);

            Bitmap bitmap = loadBitmapFromView(view, screenWidth_ / 2, (int) (screenHeight_ / 3.5));

            if (i == 0) {
                canvas.drawBitmap(bitmap, (float) (screenWidth * 0.1), (float) (screenHeight * 0.64), paint);
            } else if (i == 1) {
                Bitmap b2 = Bitmap.createScaledBitmap(
                        bitmap, (int) (screenWidth / 2.5), (int) (screenHeight / 3.5), false);

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

    void forward() {
        isForward = true;
        if (swipe) {
            if (memories.isEmpty()) {
                return;
            }
            DataModel selectValue = memories.get(0);

            for (int i = 0; i < memories.size() - 1; i++) {
                memories.set(i, memories.get(i + 1));
            }
            memories.set(memories.size() - 1, selectValue);

            resetView();
        }
    }

    void backward() {
        isForward = false;
        if (swipe) {
            if (memories.isEmpty()) {
                return;
            }
            DataModel selectValue = memories.get(memories.size() - 1);

            for (int i = memories.size() - 1; i > 0; i--) {
                memories.set(i, memories.get(i - 1));
            }
            memories.set(0, selectValue);

            resetView();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        gestureDetector.onTouchEvent(event);

        return true;
    }

    public class GestureListener extends GestureDetector.SimpleOnGestureListener {
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
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                    result = true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    void onSwipeRight() {
        Log.d(TAG, "onSwipeRight: Move backwards");
        swipe = true;
        backward();
    }

    void onSwipeLeft() {
        Log.d(TAG, "onSwipeLeft: Move forward");
        swipe = true;
        forward();
    }

    void onSwipeTop() {
        Log.d(TAG, "onSwipeTop: Move backward");
        swipe = true;
        backward();
    }

    void onSwipeBottom() {
        Log.d(TAG, "onSwipeBottom: Move forward");
        swipe = true;
        forward();
    }

    static class HolderPointer {
        float height;
        float width;
        int dstWidth;
        int dstHeight;

        HolderPointer(int dstWidth, int dstHeight, float width, float height) {
            this.dstWidth = dstWidth;
            this.dstHeight = dstHeight;
            this.height = height;
            this.width = width;
        }
    }
}
