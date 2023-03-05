package com.example.standalone;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.PerformanceHintManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.standalone.gameobjects.Circle;
import com.example.standalone.gameobjects.Enemy;
import com.example.standalone.gameobjects.Player;
import com.example.standalone.gameobjects.Spell;
import com.example.standalone.gamepanel.GameOver;
import com.example.standalone.gamepanel.Joystick;
import com.example.standalone.gamepanel.Performance;

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
    private GameOver gameOver;
    private Performance performance;


    public Game(Context context) {
        super(context);

        //get SurfaceHolder dan Callback
        SurfaceHolder surfaceholder = getHolder();
        surfaceholder.addCallback(this);

        gameloop = new GameLoop(this, surfaceholder);

        //Inisialisasi game panel
        performance = new Performance(context, gameloop);
        gameOver = new GameOver(context);
        joystick = new Joystick(260, 800, 70, 40);

        //Inisialisasi Player
        player = new Player(context,joystick, 2*500, 500, 30);

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
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        Log.d("Game.java", "surfaceCreated()");

        if (gameloop.getState().equals(Thread.State.TERMINATED)){

                gameloop = new GameLoop(this, holder);
        }
        gameloop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.d("Game.java", "surfaceChanged()");
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        Log.d("Game.java", "surfaceDestroyed()");
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        joystick.draw(canvas);
        player.draw(canvas);

        for (Enemy enemy : enemyList){
            enemy.draw(canvas);
        }

        for (Spell spell : spellList) {
            spell.draw(canvas);
        }

        //Menggambar Game panel
        joystick.draw(canvas);
        performance.draw(canvas);

        //Menggambar Game Over
        if(player.getHealPoints() <=0 ){
            gameOver.draw(canvas);
        }
    }


    public void update() {

        //Berhenti Update Game state jika Player sudah Meninggal
        if(player.getHealPoints() <= 0){
            return;
        }

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

    public void pause() {
        gameloop.stopLoop();
    }
}


/**
        if (gameLoop.getState().equals(Thread.State.TERMINATED)) {
                SurfaceHolder surfaceHolder = getHolder();
                surfaceHolder.addCallback(this);
                gameLoop = new GameLoop(this, surfaceHolder);
                }
 **/