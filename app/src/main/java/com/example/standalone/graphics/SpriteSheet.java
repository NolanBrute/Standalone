package com.example.standalone.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.example.standalone.R;

public class SpriteSheet {
    private Bitmap bitmap;

    public SpriteSheet(Context context){
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inScaled = false;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite_player,bitmapOptions);
    }

    public Sprite getPlayerSprite(){
        return new Sprite(this, new Rect(0, 0, 64, 64));
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
