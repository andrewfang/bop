package com.andrewbfang.bop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

import static com.andrewbfang.bop.Gestures.*;

/**
 * Created by Andrew
 */
public class GestureAction extends Activity
        implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener, SensorEventListener {

    private GestureDetector detector;
    private static final String TAG = "GestureExample";
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private float mLastX, mLastY, mLastZ;
    private float deltaX, deltaY, deltaZ;
    private boolean mInitialized;

    private SensorManager mSensorManager;

    private Sensor mAccelerometer;

    private final float THRESHOLD = (float) 8.5;
    private final long MAX_TIME = (long) 2000;
    private long startTime;
    private long timePassed;
    private boolean watchRunning;

    private boolean timedGame;
    private long gameStartTime;
    private long gameTimePassed;
    private long totalGameTime;


    Gestures target;
    private TextView canvas;
    private TextView score;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestures);
        detector = new GestureDetector(this, this);
        detector.setOnDoubleTapListener(this);

        mInitialized = false;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        canvas = (TextView) findViewById(R.id.canvas);
        score = (TextView) findViewById(R.id.score);
        Intent intent = getIntent();
        timedGame = intent.getBooleanExtra("TimerGame", false);
        if (timedGame) {
            gameStartTime = System.currentTimeMillis();
        }
        totalGameTime = intent.getIntExtra("PotatoTime", 10) * 1000;
        play();
    }


    public void play(){
        if (timedGame) {
            gameTimePassed = System.currentTimeMillis() - gameStartTime;
            if (gameTimePassed > totalGameTime) {
                failed("Out of Time");
            }
        }
        Gestures next = Gestures.getRandom();
        canvas.setText(next.toString());
        target = next;
        watchRunning = target.needsWatch();
        startTime = System.currentTimeMillis();
    }

    public void failed(String msg){
        Intent intent = new Intent(this, FinalScore.class);
        intent.putExtra("FANTASTICSCORE", Integer.parseInt(score.getText().toString()));
        intent.putExtra("ENDMESSAGE", msg);
        startActivity(intent);
        score.setText("0");
    }

    public boolean onTouchEvent(MotionEvent e){
        this.detector.onTouchEvent(e);
        return super.onTouchEvent(e);
    }
    public boolean onDown(MotionEvent e) {
        return true;
    }
    public boolean onFling(MotionEvent e1, MotionEvent e2,
                           float velocityX, float velocityY) {
        if ((e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE) && (Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)) {
            if (target == SWIPE_LEFT) {
                incrementScore();
                playSound();
                play();
            } else {
                failed("Game Over");
            }
            return true;
        } else if ((e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE) && (Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)) {
            if (target == SWIPE_RIGHT) {
                incrementScore();
                playSound();
                play();
            } else {
                failed("Game Over");
            }
            return true;
        } else if ((e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE) && (Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY)) {
            if (target == SWIPE_UP) {
                incrementScore();
                playSound();
                play();
            } else {
                failed("Game Over");
            }
            return true;
        }else if ((e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE) && (Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY)) {
            if (target == SWIPE_DOWN) {
                incrementScore();
                playSound();
                play();
            } else {
                failed("Game Over");
            }
            return true;
        } else {
            Log.d(TAG, "FAILED: onFling: " + e1.toString() + e2.toString());
            return false;
        }
    }
    public void onLongPress(MotionEvent e) {
        if (target == LONGPRESS) {
            incrementScore();
            playSound();
            play();
        } else {
            failed("Game Over");
        }
        Log.d(TAG, "onLongPress: " + e.toString());
    }
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        return true;
    }
    public void onShowPress(MotionEvent e) {
        Log.d(TAG, "onShowPress: " + e.toString());
    }
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d(TAG, "onSingleTapUp: " + e.toString());
        return true;
    }
    public boolean onDoubleTap(MotionEvent e) {
        Log.d(TAG, "onDoubleTap: " + e.toString());
        if (target == DTAP) {
            incrementScore();
            playSound();
            play();
        } else {
            failed("Game Over");
        }
        return true;
    }
    public boolean onDoubleTapEvent(MotionEvent e) {
        Log.d(TAG, "onDoubleTapEvent: " + e.toString());
        return true;
    }
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.d(TAG, "onSingleTapConfirmed: " + e.toString());
        if (target == TAP | target == DTAP | target == LONGPRESS) {
            incrementScore();
            playSound();
            play();
        } else {
            failed("Game Over");
        }
        return true;
    }

    public void incrementScore() {
        score.setText("" + (Integer.parseInt(score.getText().toString()) + 1));
    }

    public void playSound() {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {}
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //ignore for now
    }

    public void onSensorChanged (SensorEvent event){
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        if (!mInitialized) {
            mLastX = x;
            mLastY = y;
            mLastZ = z;
            mInitialized = true;
        } else {
            deltaX = Math.abs(mLastX - x);
            deltaY = Math.abs(mLastY - y);
            deltaZ = Math.abs(mLastZ - z);
        }


        if (watchRunning) {
            timePassed = System.currentTimeMillis() - startTime;
            if (timePassed > MAX_TIME) {
                watchRunning = false;
                timePassed = 0;
                playSound();
                play();
            }
            if (deltaX > THRESHOLD){
                onSideways();
            } else if (deltaZ > THRESHOLD){
                onUpDown();
            }
        }
    }


    public void onSideways(){
        if (target == SIDEWAYS) {
            incrementScore();
        }
    }

    public void onUpDown(){
        if (target == UP_DOWN) {
            incrementScore();
        }
    }


}