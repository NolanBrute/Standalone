package com.example.standalone;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.standalone.objects.Circle;
import com.example.standalone.objects.Enemy;
import com.example.standalone.objects.Player;
import com.example.standalone.objects.Spell;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
    Game.java bertanggung jawab untuk meng update state dari sebuah game dan me render seluruh objek di Layar
 */

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private final Player player;
    private final Joystick joystick;
    //private final Enemy enemy;
    private GameLoop gameloop;
    private List<Enemy> enemyList = new ArrayList<Enemy>();
    private List<Spell> spellList = new ArrayList<Spell>();
    private int joystickPointerId = 0;
    private int numberOfSpellsToCast = 0;


    public Game(Context context) {
        super(context);

        //get SurfaceHolder dan Callback
        SurfaceHolder surfaceholder = getHolder();
        surfaceholder.addCallback(this);

        gameloop = new GameLoop(this, surfaceholder);

        //Inisialisasi Joystick
        joystick = new Joystick(260, 800, 70, 40);

        //Inisialisasi Player
        player = new Player(getContext(),joystick, 2*500, 500, 30);

        //Inisialisasi Musuh
        //enemy = new Enemy(getContext(),player, 500, 200, 30);

        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //megurusi touch event dan Joystick
        switch(event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if(joystick.getIsPressed()){

                    //Joystick sudah di gunakan sebelum event ini -> Menembak / cast spell
                    numberOfSpellsToCast ++;
                }

                else if(joystick.isPressed((double) event.getX(), (double) event.getY())) {

                    //Joystick sudah digunakan saat event ini berjalan -> setIspressed(true) dan store ID joystick
                    joystickPointerId = event.getPointerId(event.getActionIndex());
                    joystick.setIsPressed(true);
                }

                else{

                    //Joystick tidak digunakan saat event ini berjalan -> Menembak / cast spell
                    numberOfSpellsToCast ++;
                }
                return true;

            case MotionEvent.ACTION_MOVE:

                //Joystick digunakan sebelumnya dan sekarang digerakkan
                if(joystick.getIsPressed()){
                    joystick.setActuator((double) event.getX(), (double) event.getY());
                }
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (joystickPointerId == event.getPointerId(event.getActionIndex())){
                    //Joystick dilepaskan dari -> setIsPressed(false) dan resetActuator
                    joystick.setIsPressed(false);
                    joystick.resetActuator();
                }
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

        for (Enemy enemy : enemyList){
            enemy.draw(canvas);
        }

        for (Spell spell : spellList) {
            spell.draw(canvas);
        }
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
        player.update();

        //Spawn Enemy jika ada Waktu
        if(Enemy.readyToSpawn()) {
            enemyList.add(new Enemy(getContext(), player));
        }

        //Update state dari Setiap Musuh
        for (Enemy enemy : enemyList){
            enemy.update();
        }

        //Update state dari Setiap Tembakan / spell
        while (numberOfSpellsToCast > 0 ){
            spellList.add(new Spell(getContext(), player));
            numberOfSpellsToCast --;
        }
        for (Spell spell : spellList){
            spell.update();
        }

        //Memeriksa jika ada Collision / Tabrakan antara Player dan Enemy
        Iterator<Enemy> iteratorEnemy = enemyList.iterator();
        while (iteratorEnemy.hasNext()){
            Circle enemy = iteratorEnemy.next();
            if (Circle.isColliding(enemy, player)){

                //Menghapus Musuh bila musuh bertabrakan dengan Player
                iteratorEnemy.remove();
                player.setHealthPoints(player.getHealPoints() - 1);

                continue;
            }

            Iterator<Spell> iteratorSpell = spellList.iterator();
            while(iteratorSpell.hasNext()){
                Circle spell = iteratorSpell.next();

                //Menghapus tembakan / spell saat sudah mengenai musuh
                if(Circle.isColliding(spell, enemy)){
                    iteratorSpell.remove();
                    iteratorEnemy.remove();

                    break;
                }
            }
        }
    }
}
