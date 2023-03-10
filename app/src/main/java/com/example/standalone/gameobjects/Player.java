package com.example.standalone.gameobjects;

import android.content.Context;
import android.graphics.Canvas;

import androidx.core.content.ContextCompat;

import com.example.standalone.GameDisplay;
import com.example.standalone.GameLoop;
import com.example.standalone.gamepanel.Joystick;
import com.example.standalone.R;
import com.example.standalone.gamepanel.HealthBar;
import com.example.standalone.gamepanel.Performance;
import com.example.standalone.graphics.Sprite;

/*
    Player adalah Karakter Utama yang akan dan bisa kita kendalikan dengan
    bantuan Joystick yag kita Buat, Player class adalah ekstensi dari Circle
 */
public class Player extends Circle {

    public static final double SPEED_PIXELS_PER_SECOND = 400.0;
    public static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    public static final int MAX_HEALTH_POINTS = 10;
    public final Joystick joystick;
    public HealthBar healthBar;
    public int healthPoints;
    private Sprite sprite;

    public Player(Context context ,Joystick joystick, double positionX, double positionY,double radius, Sprite sprite){
        super(context, ContextCompat.getColor(context, R.color.player), positionX, positionY, radius);
        this.joystick = joystick;
        this.healthBar = new HealthBar(context,this);
        this.sprite = sprite;
        this.healthPoints = MAX_HEALTH_POINTS;
    }

    public void update() {
        //Meng Update ecepatan Player tergantung actuator Joystick
        velocityX = joystick.getActuatorX() * MAX_SPEED;
        velocityY = joystick.getActuatorY() * MAX_SPEED;

        positionX += velocityX;
        positionY += velocityY;

        //Update arah
        if(velocityX != 0 || velocityY != 0){
            //Normalisir Kecepatan untuk mendapat direksi / arah (unit vector dari kecepatan)
                double distance = Utils.getDistanceBetweenPoints(0, 0, velocityX, velocityY);
                directionX = velocityX / distance;
                directionY = velocityY / distance;
        }
    }

    public void draw(Canvas canvas, GameDisplay gameDisplay){
        sprite.draw(
                canvas,
                (int) gameDisplay.gameToDisplayCoordinatesX(getPositionX()) - 32,
                (int) gameDisplay.gameToDisplayCoordinatesY(getPositionY()) - 32

                );

        healthBar.draw(canvas, gameDisplay);

    }

    public int getHealPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        if (healthPoints >= 0){
            this.healthPoints = healthPoints;
        }
    }
}
