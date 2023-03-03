package com.example.standalone.gamepanel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.core.content.ContextCompat;

import com.example.standalone.R;

/**
 *  GameOver akan Menggambar Text 'Game Over' Saat Player sudah memiliki 0 nyawa ke layar.
 */

public class GameOver {

    private Context context;

    public GameOver(Context context){
        this.context =  context;

    }

    public void draw(Canvas canvas) {

        String text = "Game Over";

        float x = 500;
        float y = 500;

        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.GameOver);
        paint.setColor(color);
        float textSize = 150;
        paint.setTextSize(textSize);

        canvas.drawText(text, x, y, paint);
    }
}
