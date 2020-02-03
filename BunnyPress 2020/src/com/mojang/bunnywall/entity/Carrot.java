package com.mojang.bunnywall.entity;

import java.awt.Graphics;

public class Carrot extends Entity
{
    public Carrot(int x, int y)
    {
        this.x = x;
        this.y = y;
        this.xRad = this.yRad = 4;
        crushStrength = 1;
        health = 1;
        bunnyInterest = 50;
    }

    public void render(Graphics g)
    {
        if (isDragged)
        {
            g.drawImage(shadowSheet[1][0], x - 8, y - 8, null);
        }
        g.drawImage(objectSheet[1][0], x-8+crushTime/2%2, y-16-(isDragged?6:0), null);
    }
}