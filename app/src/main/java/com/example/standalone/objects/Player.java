package com.example.standalone.objects;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.example.standalone.GameLoop;
import com.example.standalone.Joystick;
import com.example.standalone.R;
import com.example.standalone.objects.Circle;

/*
    Player adalah Karakter Utama yang akan dan bisa kita kendalikan dengan
    bantuan Joystick yag kita Buat, Player class adalah ekstensi dari Circle
 */
public class Player extends Circle {

    public static final double SPEED_PIXELS_PER_SECOND = 400.0;
    public static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private final Joystick joystick;

    public Player(Context context ,Joystick joystick, double positionX, double positionY,double radius){
        super(context, ContextCompat.getColor(context, R.color.player), positionX, positionY, radius);
        this.joystick = joystick;
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


   }
