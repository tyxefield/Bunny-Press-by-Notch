package com.mojang.bunnywall.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.mojang.bunnywall.Images;

public class Splat extends Entity
{
    private static BufferedImage[][] bloodSheet = Images.cut("/blood.png", 8, 8);

    private BufferedImage image;

    private double xx, yy, zz;
    private double xa, ya, za;
    private int life = 0;

    private int color = 0;
    private int type = 0;

    public Splat(int x, int y, int type)
    {
        this.type = type;
        this.xx = this.x = x;
        this.yy = this.y = y;
        this.zz = this.z = 0;
        
        if (type==1)
        {
            xx+=(int)(Math.random()*14-7);
            yy+=(int)(Math.random()*14-7);
            zz+=(int)(Math.random()*8);
        }
        if (type==0)
        {
            xx+=(int)(Math.random()*8-4);
            yy+=(int)(Math.random()*8-4);
            zz+=(int)(Math.random()*8);
        }

        double dir = Math.random() * Math.PI * 2;
        double pow = Math.random();
        xa = Math.cos(dir) * pow;
        ya = Math.sin(dir) * pow;
        za = Math.random() * 1 + 1;
        this.life = (int) (Math.random() * Math.random() * 80 + 40);
        this.image = bloodSheet[(int) (Math.random() * 8)][type];
        canShove = false;
        health = 1;
        crushStrength = 1;
        isPhysical = false;
        this.xRad = 1;
        this.yRad = 1;
        isDragable = false;

        color = ((int) (Math.random() * 80 + 20) << 16);
    }

    public void tick()
    {
        super.tick();

        if (--life <= 0)
        {
            remove();
        }

        xx += xa;
        yy += ya;
        zz += za;

        if (zz > 0)
        {
            za -= 0.2;
        }
        if (zz < 0)
        {
            zz = 0;
            za *= -0.1;
            xa *= 0.9;
            ya *= 0.9;
            if (type == 0)
            {
                if ((int) xx != x || (int) yy != y)
                {
                    int alpha = life * 2 + 10;
                    if (alpha > 128) alpha = 128;
                    level.taint((int) xx, (int) yy, (alpha << 24) + color, 3);
                }
            }
        }

        int xt = (int) (xx);
        int yt = (int) (yy);
        attemptMove(xt - x, yt - y);
        xx += (x - xt);
        yy += (y - yt);
        //        this.x = (int)(xx);
        //        this.y = (int)(yy);
        this.z = (int) (zz);
    }

    public void render(Graphics g)
    {
        g.drawImage(shadowSheet[2][0], x - 8, y - 8, null);
        g.drawImage(image, x - 4 + crushTime / 2 % 2, y - 4 - z, null);
    }
}