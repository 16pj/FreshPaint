package com.rpj.robin.freshpaint;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.inputmethodservice.Keyboard;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends Activity {

    String user_name = "Robber";

    // Storage Permissions variables
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //persmission method.
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }




    int colorer = 0;
    int colorcount = 10;
    float startx, starty;
    float endx, endy;
    int radius;
    float centx, centy;


    int sw, sh;

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
    String image_name;
    private Paint circlePaint;
    private Path circlePath;
    private Path outPath;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toast.makeText(this,"Press and Hold Screen To Change Color!",Toast.LENGTH_LONG).show();
        drawPath = new Path();
        setContentView(R.layout.activity_main);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(15);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
        drawView = new MyView(this);
        MyView drawCross = new MyView(this);
        RelativeLayout layout1 = (RelativeLayout) findViewById(R.id.linv);
        layout1.addView(drawCross);

        verifyStoragePermissions(this);

        circlePaint = new Paint();
        circlePath = new Path();
        outPath = new Path();
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
            super(context);
            gestureDetector = new GestureDetector(context, new GestureListener());
            setDrawingCacheEnabled(true);
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
            super.onDraw(canvas);

            canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
            canvas.drawPaint(paint);

            // Use Color.parseColor to define HTML colors
            canvas.drawPath(drawPath, paint);
            canvas.drawPath(circlePath, circlePaint);
            canvas.drawPath(outPath, circlePaint);


        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
//detect user touch
            float touchX = event.getX();
            float touchY = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (shape.equals("Pen")) {
                        drawPath.moveTo(touchX, touchY);
                    } else {
                        startx = touchX;
                        starty = touchY;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:

                    if (shape.equals("Pen")) {
                        drawPath.lineTo(touchX, touchY);
                    }

                    mX = touchX;
                    mY = touchY;

                    circlePath.reset();
                    circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
                    switch(shape) {
                        case "Rectangle":
                            outPath.reset();
                            outPath.addRect(startx, starty, mX, mY, Path.Direction.CW);

                            break;


                        case "Line":
                            outPath.reset();
                            outPath.addCircle(startx, starty, 30, Path.Direction.CW);
                            break;


                        case "Oval":
                            outPath.reset();
                            radius = ((int) Math.sqrt(((mX - startx) * (mX - startx) + (mY - starty) * (mY - starty)))) / 2;
                            centx = (mX + startx) / 2;
                            centy = (mY + starty) / 2;
                            outPath.addCircle(centx, centy, radius, Path.Direction.CW);
                            break;

                        default:
                            break;
                    }

                    break;


                case MotionEvent.ACTION_UP:
                    circlePath.reset();
                    outPath.reset();
                    if (!shape.equals("Pen")) {
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
                            radius = ((int) Math.sqrt(((endx - startx) * (endx - startx) + (endy - starty) * (endy - starty)))) / 2;
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

    public void ovalClick(View view) {
        shape = "Oval";
        drawView.setColor(oldcolor);
        paint.setStrokeWidth(15);
        Toast.makeText(MainActivity.this, "Oval Selected", Toast.LENGTH_SHORT).show();

    }

    public void rectClick(View view) {
        shape = "Rectangle";
        paint.setStrokeWidth(15);
        drawView.setColor(oldcolor);
        Toast.makeText(MainActivity.this, "Rectangle Selected", Toast.LENGTH_SHORT).show();


    }

    public void lineClick(View view) {
        shape = "Line";
        paint.setStrokeWidth(15);
        drawView.setColor(oldcolor);
        Toast.makeText(MainActivity.this, "Line Selected", Toast.LENGTH_SHORT).show();


    }

    public void penClick(View view) {
        shape = "Pen";
        paint.setStrokeWidth(15);
        drawView.setColor(oldcolor);
        Toast.makeText(MainActivity.this, "Pen Selected", Toast.LENGTH_SHORT).show();

    }


    public void saveAll(View view){

        try {
            Cursor c = getApplication().getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
            c.moveToFirst();
            user_name = c.getString(c.getColumnIndex("display_name"));
            c.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyMMdd-HH-mm-ss");
        String formattedDate = df.format(c.getTime());
// Now formattedDate have current date/time

        image_name =  user_name + formattedDate + ".JPEG";

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + image_name);
            canvasBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored

            Toast.makeText(MainActivity.this, "Saved to SD card", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Couldn't Save for some reason", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        new Backgrounder().execute();

    }

    public void rubClick(View view) {
        shape = "Pen";
        paint.setStrokeWidth(20);
        oldcolor = colorname;
        drawView.setColor("White");
        Toast.makeText(MainActivity.this, "Eraser Selected", Toast.LENGTH_SHORT).show();

    }

    public void clearClick(View view) {
        drawCanvas.drawColor(Color.WHITE);
        paint.setStrokeWidth(15);
        Toast.makeText(MainActivity.this, "Cleared All", Toast.LENGTH_SHORT).show();
    }


    public class Backgrounder extends AsyncTask<Void, Void, String> {


        private ProgressDialog pd = new ProgressDialog(MainActivity.this);
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Wait image uploading!");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+ image_name);
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(f);//You can get an inputStream using any IO API
            } catch (Exception e) {
                e.printStackTrace();
            }

            byte[] bytes;
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            try {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            bytes = output.toByteArray();
            String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);
//            Toast.makeText(MainActivity.this,encodedString,Toast.LENGTH_SHORT).show();

            String url_add = "http://robindustbin.comli.com/imagecatcher.php";

            try {
                java.net.URL url = new URL(url_add);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("image_name", "UTF-8") + "=" + URLEncoder.encode(image_name, "UTF-8") + "&"
                        + URLEncoder.encode("encoded_string", "UTF-8") + "=" + URLEncoder.encode(encodedString, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream1 = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream1, "iso-8859-1"));
                String result = "";
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }

                bufferedReader.close();
                inputStream1.close();
                httpURLConnection.disconnect();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.hide();
            pd.dismiss();
            Toast.makeText(MainActivity.this,"DONE!!",Toast.LENGTH_SHORT).show();
        }
    }

    public void showAll(View view){
        Intent i = new Intent(this, Listed.class);
        startActivity(i);
    }
}
