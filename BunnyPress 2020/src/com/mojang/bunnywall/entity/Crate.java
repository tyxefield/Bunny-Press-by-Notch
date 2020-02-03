package com.mojang.bunnywall.entity;

import java.awt.Graphics;

import com.mojang.bunnywall.Sounds;

public class Crate extends Entity
{
    public Crate(int x, int y)
    {
        this.x = x;
        this.y = y;
        this.xRad = this.yRad = 8;
        crushStrength = 20;
        health = 1;
    }

    public void render(Graphics g)
    {
        g.drawImage(objectSheet[0][0], x-8+crushTime/2%2, y-16, null);
    }

    public void crushed(Crusher crusher)
    {
        Sounds.play(Sounds.crunch, x);
        for (int i=0; i<10; i++)
            level.addEntity(new Splat(x, y, 1));
    }
}