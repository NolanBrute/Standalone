package com.example.standalone.gamepanel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.standalone.GameDisplay;
import com.example.standalone.R;
import com.example.standalone.gameobjects.Player;

/**
 *  Healthbar bertanggung jawab untuk Menampilkan Nyawa dari sang pemain, jika musuh berhasil menyentuh pemain
 *  maka nyawanya akan berkurang
 *
 */

public class HealthBar {

    private Player player;
    private int width, height, margin;
    private Paint borderPaint, healthPaint;

    public HealthBar(Context context, Player player){
        this.player = player;
        this.width = 100;
        this.height = 20;
        this.margin = 2;
        this.borderPaint = new Paint();
        int borderColor = ContextCompat.getColor(context, R.color.healthBarBorder);
        borderPaint.setColor(borderColor);

        this.healthPaint = new Paint();
        int healthColor = ContextCompat.getColor(context, R.color.healthBarHealth);
        healthPaint.setColor(healthColor);

    }
    public void draw(Canvas canvas, GameDisplay gameDisplay){
        float x = (float) player.getPositionX();
        float y = (float) player.getPositionY();
        float distanceToPlayer = 30;
        float healthPointPercentage = (float) player.getHealPoints() / player.MAX_HEALTH_POINTS;

        //Menggambar Border
        float borderLeft, borderTop, borderRight, borderBottom;
        borderLeft = x - width / 2;
        borderRight = x + width / 2;
        borderBottom = y - distanceToPlayer;
        borderTop = borderBottom - height;

        canvas.drawRect(
                (float) gameDisplay.gameToDisplayCoordinatesX (borderLeft),
                (float) gameDisplay.gameToDisplayCoordinatesY (borderTop),

                (float) gameDisplay.gameToDisplayCoordinatesX (borderRight),
                (float) gameDisplay.gameToDisplayCoordinatesY (borderBottom),
                borderPaint);

        //Menggambar Nyawa
        float healthLeft, healthTop, healthRight, healthBottom, healthWidth, healthHeight;
        healthWidth = width - 2 * margin;
        healthHeight = height - 2 * margin;
        healthLeft = borderLeft + margin;
        healthRight = healthLeft + healthWidth * healthPointPercentage;
        healthBottom = borderBottom - margin;
        healthTop = healthBottom - healthHeight;

        canvas.drawRect(
                (float) gameDisplay.gameToDisplayCoordinatesX (healthLeft),
                (float) gameDisplay.gameToDisplayCoordinatesY (healthTop),

                (float) gameDisplay.gameToDisplayCoordinatesX (healthRight),
                (float) gameDisplay.gameToDisplayCoordinatesY (healthBottom),
                healthPaint);
    }
}
