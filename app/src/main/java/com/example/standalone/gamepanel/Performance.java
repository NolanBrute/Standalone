package com.example.standalone.gamepanel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.standalone.GameLoop;
import com.example.standalone.R;

public class Performance {
    private GameLoop gameloop;
    private Context context;

    public Performance(Context context, GameLoop gameloop){
        this.context = context;
        this.gameloop = gameloop;
    }

    public void draw(Canvas canvas){
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
        canvas.drawText("FPS : " + averageFPS, 95, 150, paint);
    }
}
