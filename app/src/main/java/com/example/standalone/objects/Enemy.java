package com.example.standalone.objects;

/*
    Enemy / Musuh adalah Object yang nanti akan mengejar sang player, anggap saja seperti Zombie,
    class Enemy adalah ekstensi dari class Circle
 */

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.example.standalone.GameLoop;
import com.example.standalone.R;

public class Enemy extends Circle {

    public static final double SPEED_PIXELS_PER_SECOND = Player.SPEED_PIXELS_PER_SECOND * 0.6;
    public static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private static final double SPAWNS_PER_MINUTES = 20;
    private static final double SPAWNS_PER_SECONDS = SPAWNS_PER_MINUTES / 60.0;
    private static final double UPDATES_PER_SPAWN = GameLoop.MAX_UPS / SPAWNS_PER_SECONDS;
    private static double updateUntilNextSpawn = UPDATES_PER_SPAWN;

    private final Player player;

    public Enemy(Context context, Player player, double positionX, double positionY, double radius) {
        super(context, ContextCompat.getColor(context, R.color.enemy), positionX, positionY, radius);
        this.player = player;
    }

    public Enemy(Context context, Player player) {
        super(
                context,
                ContextCompat.getColor(context, R.color.enemy),
                Math.random() * 1000,
                Math.random() * 1000,
                30);

        this.player = player;
    }

    /**
      *  readyToSpawn akan menentukan apakah musuh Baru siap ditambahkan atau tidak, tergantung dengan
       * Penentuan Batas Musuh yang Sudah di tetapkan
     */
    public static boolean readyToSpawn(){
        if(updateUntilNextSpawn <= 0){
            updateUntilNextSpawn += UPDATES_PER_SPAWN;
            return true;
        }else {
            updateUntilNextSpawn --;
            return false;
        }
    }

    @Override
    public void update() {
        //--------------------------------------------------------------------------------------
        //  |  Update kecepatan Musuh agar dapat mengejar Player                               |
        //--------------------------------------------------------------------------------------
        //Menghitung vector dari musuh ke Player (x dan y Koordinat Kartesius)
        double distanceToPlayerX = player.getPositionX() - positionX;
        double distanceToPlayerY = player.getPositionY() - positionY;

        //Menghitung Jarak absolut antara musuh dan Player
        double distanceToPlayer = GameObject.getDistanceBetweenObjects(this, player);

        //Menghitung arah dari Musuh ke Pllayer
        double directionX = distanceToPlayerX / distanceToPlayer;
        double directionY = distanceToPlayerY / distanceToPlayer;

        //Menetapkan kecepatan ke arah Player
        if(distanceToPlayer > 0){
            velocityX = directionX * MAX_SPEED;
            velocityY = directionY * MAX_SPEED;

        }else{
            velocityX = 0;
            velocityY = 0;
        }

        //Update Posisi Musuh
        positionX += velocityX;
        positionY += velocityY;
    }
}
