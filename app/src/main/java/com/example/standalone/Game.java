package com.example.standalone;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

/*
    Game.java bertanggung jawab untuk meng update state dari sebuah game dan me render seluruh objek di Layar
 */

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private final Player player;
    private final Joystick joystick;
    private GameLoop gameloop;


    public Game(Context context) {
        super(context);

        //get SurfaceHolder dan Callback
        SurfaceHolder surfaceholder = getHolder();
        surfaceholder.addCallback(this);

        gameloop = new GameLoop(this, surfaceholder);

        //Inisialisasi Joystick
        joystick = new Joystick(260, 800, 70, 40);

        //Inisialisasi Player
        player = new Player(getContext(), 2*500, 500, 30);

        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //megurusi touch event dan Joystick
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(joystick.isPressed((double) event.getX(), (double) event.getY())) {
                    joystick.setIsPressed(true);
                }
                return true;

            case MotionEvent.ACTION_MOVE:
                if(joystick.getIsPressed()){
                    joystick.setActuator((double) event.getX(), (double) event.getY());
                }
                return true;

            case MotionEvent.ACTION_UP:
                joystick.setIsPressed(false);
                joystick.resetActuator();
                return true;

        }
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        gameloop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawUPS(canvas);
        drawFPS(canvas);

        joystick.draw(canvas);
        player.draw(canvas);
    }

    public void drawUPS(Canvas canvas){
        String averageUPS = Double.toString(gameloop.getAverageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.teal_200);
        paint.setColor(color);
        paint.setTextSize(30);
        canvas.drawText("UPS : " + averageUPS, 95, 95, paint);
    }

    public void drawFPS(Canvas canvas){
        String averageFPS = Double.toString(gameloop.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.yellow);
        paint.setColor(color);
        paint.setTextSize(30);
        canvas.drawText("FPS : " + averageFPS, 95, 150, paint);
    }

    public void update() {
        //update Game State
        joystick.update();
        player.update(joystick);
    }
}
