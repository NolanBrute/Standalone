package com.example.standalone;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

/*
    Game.java bertanggung jawab untuk meng update state dari sebuah game dan me render seluruh objek di Layar
 */

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private GameLoop gameloop;
    private Context context;

    public Game(Context context) {
        super(context);

        //get SurfaceHolder dan Callback
        SurfaceHolder surfaceholder = getHolder();
        surfaceholder.addCallback(this);

        this.context = context;
        gameloop = new GameLoop(this, surfaceholder);

        setFocusable(true);
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
    }

    public void drawUPS(Canvas canvas){
        String averageUPS = Double.toString(gameloop.getAverageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.teal_200);
        paint.setColor(color);
        paint.setTextSize(30);
        canvas.drawText("UPS : " + averageUPS, 95, 95, paint);
    }

    public void drawFPS(Canvas canvas){
        String averageFPS = Double.toString(gameloop.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.yellow);
        paint.setColor(color);
        paint.setTextSize(30);
        canvas.drawText("UPS : " + averageFPS, 95, 150, paint);
    }

    public void update() {
        //update Game State
    }
}
