package com.example.standalone;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Joystick {

    private int outerCircleCenterPositionX;
    private int outerCircleCenterPositionY;
    private int innerCircleCenterPositionX;
    private int innerCircleCenterPositionY;
    private int outerCircleRadius;
    private int innerCircleRadius;
    private final Paint outerCirclePaint;
    private final Paint innerCirclePaint;
    private double joystickCenterTouchDistance;
    private boolean isPressed;
    private double actuatorX;
    private double actuatorY;

    public Joystick(int centerPositionX, int centerPositionY, int outerCircleRadius, int innerCircleRadius){

        //Inner Circle joystick Kecil, Outer Circle Joystick Besar
        outerCircleCenterPositionX = centerPositionX;
        outerCircleCenterPositionY = centerPositionY;
        innerCircleCenterPositionX = centerPositionX;
        innerCircleCenterPositionY = centerPositionY;

        //Radius Lingkaran
        this.outerCircleRadius = outerCircleRadius;
        this.innerCircleRadius = innerCircleRadius;

        //Menggambar Lingkaran
        outerCirclePaint = new Paint();
        outerCirclePaint.setColor(Color.GRAY);
        outerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        innerCirclePaint = new Paint();
        innerCirclePaint.setColor(Color.BLUE);
        innerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);

    }
    public void draw(Canvas canvas) {
        //menggambar outer circle / Lingkaran Luar
        canvas.drawCircle(outerCircleCenterPositionX,
                outerCircleCenterPositionY,
                outerCircleRadius,
                outerCirclePaint
        );

        //Menggambar Inner Circle / Lingkaran Dalam
        canvas.drawCircle(innerCircleCenterPositionX,
                innerCircleCenterPositionY,
                innerCircleRadius,
                innerCirclePaint
        );
    }

    public void update() {
        updateInnerCirclePosition();
    }
    private void updateInnerCirclePosition() {
        innerCircleCenterPositionX = (int) (outerCircleCenterPositionX + actuatorX* outerCircleRadius);
        innerCircleCenterPositionY = (int) (outerCircleCenterPositionY + actuatorY* outerCircleRadius);

    }

    public boolean isPressed(double touchPositionX, double touchPositionY) {
        joystickCenterTouchDistance = Math.sqrt(
                Math.pow(outerCircleCenterPositionX - touchPositionX, 2) +
                Math.pow(outerCircleCenterPositionY - touchPositionY, 2)
        );
        return joystickCenterTouchDistance < outerCircleRadius;
    }

    public void setIsPressed(boolean isPressed) {
        this.isPressed = isPressed;
    }


    public boolean getIsPressed() {
        return isPressed;
    }

    public void setActuator(double touchPositionX, double touchPositionY) {
        double deltaX = touchPositionX - outerCircleCenterPositionX;
        double deltaY = touchPositionY - outerCircleCenterPositionY;
        double deltaDistance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

        if(deltaDistance < outerCircleRadius) {
            actuatorX = deltaX / outerCircleRadius;
            actuatorY = deltaY / outerCircleRadius;
            
        }else {
            actuatorX = deltaX / deltaDistance;
            actuatorY = deltaY / deltaDistance;
        }
    }


    public void resetActuator() {
        actuatorX = 0.0;
        actuatorY = 0.0;
    }

    public double getActuatorX() {
        return actuatorX;
    }

    public double getActuatorY() {
        return actuatorY;
    }
}
