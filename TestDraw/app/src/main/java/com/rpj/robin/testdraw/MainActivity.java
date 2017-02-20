package com.rpj.robin.testdraw;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class MainActivity extends Activity {
    int colorer = 0;
    int colorcount = 10;
    float startx,starty;
    float endx,endy;
    int radius;
    int sw, sh;
    private Paint circlePaint;
    private Path circlePath;

    String shape = "Pen";
    String oldcolor = "Black";

    String colorname = "Black";
    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint paint, canvasPaint;
    //initial color
    private int paintColor = 0xFFFFFFFF;
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap;
    private MyView drawView;
    private ImageButton currPaint;
    private GestureDetector mDetector;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawPath = new Path();
        setContentView(R.layout.activity_main);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(20);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
        drawView = new MyView(this);
        MyView drawCross = new MyView(this);
        LinearLayout layout1 = (LinearLayout) findViewById(R.id.linv);

        layout1.addView(drawCross);
        circlePaint = new Paint();
        circlePath = new Path();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.BLUE);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeJoin(Paint.Join.MITER);
        circlePaint.setStrokeWidth(4f);


    }

    public class MyView extends View {
        GestureDetector gestureDetector;
        private float mX, mY;

        public MyView(Context context) {
            super(context);            // TODO Auto-generated constructor stub
            gestureDetector = new GestureDetector(context, new GestureListener());
        }

        private class GestureListener extends GestureDetector.SimpleOnGestureListener {

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                //COLOR PALETTE FOR PENCIL
                if (colorer % colorcount == 0) {
                    drawView.setColor("CYAN");
                    colorname = "CYAN";
                } else if (colorer % colorcount == 1) {
                    drawView.setColor("RED");
                    colorname = "RED";
                } else if (colorer % colorcount == 2) {
                    drawView.setColor("BLUE");
                    colorname = "BLUE";
                } else if (colorer % colorcount == 3) {
                    drawView.setColor("BLACK");
                    colorname = "BLACK";
                } else if (colorer % colorcount == 4) {
                    drawView.setColor("GREY");
                    colorname = "GREY";
                } else if (colorer % colorcount == 5) {
                    drawView.setColor("MAGENTA");
                    colorname = "MAGENTA";
                } else if (colorer % colorcount == 6) {
                    drawView.setColor("WHITE");
                    colorname = "WHITE";
                } else if (colorer % colorcount == 7) {
                    drawView.setColor("PURPLE");
                    colorname = "PURPLE";
                } else if (colorer % colorcount == 8) {
                    drawView.setColor("YELLOW");
                    colorname = "YELLOW";
                } else if (colorer % colorcount == 9) {
                    drawView.setColor("GREEN");
                    colorname = "GREEN";
                }
                colorer++;

                Toast.makeText(MainActivity.this, colorname, Toast.LENGTH_SHORT).show();
            }
        }

        public void setColor(String newColor) {

            invalidate();

            paintColor = Color.parseColor(newColor);
            paint.setColor(paintColor);
//set color
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            sw = w;
            sh = h;
            canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
            drawCanvas = new Canvas(canvasBitmap);
            drawCanvas.drawColor(Color.WHITE);

//view given size
        }

        @Override
        protected void onDraw(Canvas canvas) {
            // TODO Auto-generated method stub
            super.onDraw(canvas);

            canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
            canvas.drawPaint(paint);

            // Use Color.parseColor to define HTML colors
            canvas.drawPath(drawPath, paint);
            canvas.drawPath(circlePath, circlePaint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
//detect user touch
            float touchX = event.getX();
            float touchY = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(shape.equals("Pen")){
                        drawPath.moveTo(touchX, touchY);
                    }
                    else {
                        startx = touchX;
                        starty = touchY;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:

                    mX = touchX;
                    mY = touchY;

                    circlePath.reset();
                    circlePath.addCircle(mX, mY, 30, Path.Direction.CW);

                    if(shape.equals("Pen")){
                        drawPath.lineTo(touchX, touchY);
                    }

                    /*
                    endx = touchX;
                    endy = touchY;

                     else if (shape.equals("Rectangle")) {
                        drawCanvas.drawRect(startx, starty, endx, endy, paint);
                        invalidate();
                    }

                     */

                    break;


                case MotionEvent.ACTION_UP:
                    circlePath.reset();
                    if(!shape.equals("Pen")) {
                        endx = touchX;
                        endy = touchY;
                    }

                    switch (shape) {
                        case "Rectangle":
                            drawCanvas.drawRect(startx, starty, endx, endy, paint);
                            break;

                        case "Line":
                            drawCanvas.drawLine(startx, starty, endx, endy, paint);
                            break;

                        case "Oval":
                            radius = (int) ((endx - startx) * (endx - startx) + (endy - starty) * (endy - starty)); //DIAMETER
                            radius = ((int) Math.sqrt(radius)) / 2;
                            float centx, centy;
                            centx = (endx + startx) / 2;
                            centy = (endy + starty) / 2;
                            drawCanvas.drawCircle(centx, centy, radius, paint);
                            break;

                        case "Pen":
                            drawCanvas.drawPath(drawPath, paint);
                            drawPath.reset();
                            break;

                    }
                    break;

                default:
                    return false;
            }
            invalidate();
            return gestureDetector.onTouchEvent(event);
        }

    }

    public void ovalClick(View view){
        shape = "Oval";
        drawView.setColor(oldcolor);

        Toast.makeText(MainActivity.this, "Oval Selected", Toast.LENGTH_SHORT).show();

    }

    public void rectClick(View view){
        shape = "Rectangle";
        drawView.setColor(oldcolor);
        Toast.makeText(MainActivity.this, "Rectangle Selected", Toast.LENGTH_SHORT).show();


    }

    public void lineClick(View view){
        shape = "Line";
        drawView.setColor(oldcolor);
        Toast.makeText(MainActivity.this, "Line Selected", Toast.LENGTH_SHORT).show();


    }

    public void penClick(View view){
        shape = "Pen";
        drawView.setColor(oldcolor);
        Toast.makeText(MainActivity.this, "Pen Selected", Toast.LENGTH_SHORT).show();


    }

    public void rubClick(View view){
        shape = "Pen";
        oldcolor = colorname;
        drawView.setColor("White");
        Toast.makeText(MainActivity.this, "Eraser Selected", Toast.LENGTH_SHORT).show();

    }

    public void clearClick(View view){
        drawCanvas.drawColor(Color.WHITE);
        Toast.makeText(MainActivity.this, "Cleared All", Toast.LENGTH_SHORT).show();
    }

}




