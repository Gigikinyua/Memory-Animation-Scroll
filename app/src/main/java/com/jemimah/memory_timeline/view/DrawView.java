package com.jemimah.memory_timeline.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.jemimah.memory_timeline.R;
import com.jemimah.memory_timeline.adapter.CustomAdapter;
import com.jemimah.memory_timeline.model.DataModel;
import com.kodmap.app.library.PopopDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class DrawView extends View {

    private static final String TAG = "DrawView";

    Paint paint;
    Path path;
    PathMeasure pathMeasure;

    Context context;
    LinearLayout container;
    int bm_offsetX, bm_offsetY, screenWidth, screenWidth_, screenHeight, screenHeight_;
    public CustomAdapter adapter;

    Canvas myCanvas;

    boolean swipe = false;
    boolean isForward = false;

    GestureDetector gestureDetector;

    float step, distance, pathLength, interval;

    float[] pos;
    float[] tan;

    Matrix matrix;

    List<String> url_list;

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

        path = new Path();

        step = 50;
        distance = 220;
        pos = new float[2];
        tan = new float[2];

        matrix = new Matrix();

        url_list = new ArrayList<>();
        url_list.add("https://www.sample-videos.com/img/Sample-png-image-100kb.png");
        url_list.add("https://pngimage.net/wp-content/uploads/2018/05/background-png-images-hd-2.png");
        url_list.add("https://pngimage.net/wp-content/uploads/2018/05/entardecer-png-3.png");
        url_list.add("https://www.sample-videos.com/img/Sample-png-image-100kb.png");
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        this.myCanvas = canvas;

        if (!swipe) {
            inflateViews(canvas, distance, memories);
        } else {
            methodAnimate(canvas);
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

        drawCurve(canvas);

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

        pathMeasure = new PathMeasure(path, false);
        pathLength = pathMeasure.getLength();

        canvas.drawPath(path, paint);

        interval = pathLength / 5;
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

    private void methodAnimate(Canvas canvas) {
        for (int j = 0; j < memories.size(); j++) {
            float startPoint, endpoint;
            if (isForward) {
                if (j== 0) {
                    endpoint = 250;
                    if (distance > endpoint) {
                        distance -= step;

                        inflateViews(canvas, distance, memories);
                    } else {
                        inflateViews(canvas, distance, memories);
                    }
                } else {
                    inflateViews(canvas, distance, memories);
                }
            } else {
                if (j == 0) {
                    endpoint = 220;
                    if (distance < endpoint) {
                        distance += step;

                        inflateViews(canvas, distance, memories);
                    } else {
                        inflateViews(canvas, distance, memories);
                    }
                } else {
                    inflateViews(canvas, distance, memories);
                }
            }
            resetView();
        }
    }

    private void inflateViews(Canvas canvas, float initialPosition, @NonNull ArrayList<DataModel> memories) {
        float pos2 = initialPosition + interval;
        float pos3 = pos2 + interval;
        float pos4 = pos3 + interval;
        float pos5 = pos4 + interval;

        this.adapter = new CustomAdapter(memories, context);
        for (int i = 0; i < memories.size(); i++) {
            View view = adapter.getView(i, null, null);

            Bitmap bitmap = loadBitmapFromView(view, screenWidth_ / 2, (int) (screenHeight_ / 3.5));

            if (i == 0) {
                pathMeasure.getPosTan(initialPosition, pos, tan);
                bm_offsetX = bitmap.getWidth() / 2;
                bm_offsetY = (int) (bitmap.getHeight() / 1.14);

                matrix.reset();
                float degrees = (float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI);
                matrix.postRotate(degrees, bm_offsetX, bm_offsetY);

                matrix.postTranslate(pos[0] - bm_offsetX, pos[1] - bm_offsetY);

                canvas.drawBitmap(bitmap, matrix, null);
            } else if (i == 1) {
                pathMeasure.getPosTan(pos2, pos, tan);

                Bitmap b2 = Bitmap.createScaledBitmap(
                        bitmap, (int) (screenWidth / 2.5), (int) (screenHeight / 3.5), false);
                bm_offsetX = b2.getWidth() / 2;
                bm_offsetY = (int) (b2.getHeight() / 1.12);

                matrix.reset();
                float degrees = (float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI);
                matrix.postRotate(degrees, bm_offsetX, bm_offsetY);
                matrix.setTranslate(pos[0] - bm_offsetX, pos[1] - bm_offsetY);

                canvas.drawBitmap(b2, matrix, paint);
            } else if (i == 2) {
                pathMeasure.getPosTan(pos3, pos, tan);

                Bitmap b3 = Bitmap.createScaledBitmap(
                        bitmap, screenWidth / 3, screenHeight / 4, false);
                bm_offsetX = b3.getWidth() / 2;
                bm_offsetY = (int) (b3.getHeight() / 1.09);

                matrix.reset();
                float degrees = (float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI);
                matrix.postRotate(degrees, bm_offsetX, bm_offsetY);
                matrix.setTranslate(pos[0] - bm_offsetX, pos[1] - bm_offsetY);

                canvas.drawBitmap(b3, matrix, paint);
            } else if (i == 3) {
                pathMeasure.getPosTan(pos4, pos, tan);

                Bitmap b4 = Bitmap.createScaledBitmap(
                        bitmap, (int) (screenWidth / 3.5), (int) (screenHeight / 4.5), false);
                bm_offsetX = b4.getWidth() / 2;
                bm_offsetY = (int) (b4.getHeight() / 1.09);

                matrix.reset();
                float degrees = (float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI);
                matrix.postRotate(degrees, bm_offsetX, bm_offsetY);
                matrix.setTranslate(pos[0] - bm_offsetX, pos[1] - bm_offsetY);

                canvas.drawBitmap(b4, matrix, paint);
            } else if (i == 4) {
                pathMeasure.getPosTan(pos5, pos, tan);

                Bitmap b5 = Bitmap.createScaledBitmap(
                        bitmap, screenWidth / 4, (screenHeight / 5), false);
                bm_offsetX = b5.getWidth() / 2;
                bm_offsetY = (int) (b5.getHeight() / 1.09);

                matrix.reset();
                float degrees = (float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI);
                matrix.postRotate(degrees, bm_offsetX, bm_offsetY);
                matrix.setTranslate(pos[0] - bm_offsetX, pos[1] - bm_offsetY);

                canvas.drawBitmap(b5, matrix, paint);
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

            distance = 250 + interval;
            resetView();
        }
    }

    void backward() {
        isForward = false;
        if (swipe) {
            distance = 0;
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
        public boolean onSingleTapUp(MotionEvent e) {
            View selectedView = adapter.getView(0, null, null);

            int[] location = new int[2];
            selectedView.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];

            DataModel selectValue = memories.get(0);
            Log.d(TAG, "onClick: x = " + e.getX() + " and bitmap x = " + (float) (screenWidth * 0.1));

            for (int i = 0; i < memories.size(); i++) {
                DataModel memory = memories.get(i);

                Log.d(TAG, "onSingleTapUp: " + i);

                //if (memory == selectValue) {
                Log.d(TAG, "onSingleTapUp: " + memory.getDate());
                Dialog dialog = new PopopDialogBuilder(context)
                        // Set list like as option1 or option2 or option3
                        .setList(url_list, 0)
                        // or setList with initial position that like .setList(list,position)
                        // Set dialog header color
                        .setHeaderBackgroundColor(android.R.color.black)
                        // Set dialog background color
                        .setDialogBackgroundColor(R.color.dark_grey)
                        // Set close icon drawable
                        .setCloseDrawable(R.drawable.ic_baseline_close_24)
                        // Set loading view for pager image and preview image
                        .setLoadingView(R.layout.loading_view)
                        // Set dialog style
                        .setDialogStyle(R.style.DialogStyle)
                        // Choose selector type, indicator or thumbnail
                        .showThumbSlider(false)
                        // Set image scale type for slider image
                        .setSliderImageScaleType(ImageView.ScaleType.FIT_XY)
                        // Set indicator drawable
                        .setSelectorIndicator(R.drawable.sample_indicator_selector)
                        // Enable or disable zoomable
                        .setIsZoomable(true)
                        // Build Km Slider Popup Dialog
                        .build();

                dialog.show();
                //}
            }
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
}
