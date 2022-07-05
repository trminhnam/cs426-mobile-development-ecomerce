package com.example.findandbuy;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Sprite2D {
    public int nBMPs;
    public int iBMP;
    public Bitmap[] BMPs;
    public int left;
    public int top;
    public int width;
    public int height;
    public int State;

    public Sprite2D(Bitmap[] bmps, int left, int top, int width, int height)
    {
        BMPs = bmps;
        nBMPs = bmps.length;
        iBMP = 0;
        this.left = left;
        this.top = top;
        if (width == 0 && height==0)
        {
            width = bmps[0].getWidth();
            height = bmps[0].getHeight();
        }
        this.width = width;
        this.height = height;
    }
    int d1  = 0 , d2 = 1;
    public void update()
    {
        iBMP = (iBMP+1) % nBMPs;
        if (State==1) // selected
        {
            d1+=d2;
            if (Math.abs(d1)>10)
                d2 *= -1;
        }
    }

    public void draw(Canvas canvas)
    {
        if (State==0) // unselected
            canvas.drawBitmap(BMPs[iBMP], left, top, null);
        else // selected
        {
            Rect sourceRect = new Rect(0, 0, width-1, height-1);
            Rect destRect = new Rect(left-d1, top-d1, left+width-1+d1, top+height-1+d1);

            canvas.drawBitmap(BMPs[iBMP], sourceRect, destRect, null);
        }
    }

    public boolean isSelected(float x, float y) {
        return (x>=left && x < left+width && y>=top && y <top+height);
    }
}
