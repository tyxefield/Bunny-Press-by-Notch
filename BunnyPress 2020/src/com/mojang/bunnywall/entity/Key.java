package com.mojang.bunnywall.entity;

import java.awt.Graphics;

import com.mojang.bunnywall.Sounds;
import com.mojang.bunnywall.level.Tile;

public class Key extends Entity
{
    public Key(int x, int y)
    {
        this.x = x;
        this.y = y;
        this.xRad = this.yRad = 4;
        crushStrength = 1;
        health = 1;
    }

    public void render(Graphics g)
    {
        if (isDragged)
        {
            g.drawImage(shadowSheet[1][0], x - 8, y - 8, null);
        }
        g.drawImage(objectSheet[2][0], x-8+crushTime/2%2, y-16-(isDragged?6:0), null);
    }
    
    protected boolean canPass(Tile tile)
    {
        if (tile.lock)
        {
            tile.lock = false;
            tile.passable = true;
            level.renderTile(tile, 1, 1);
            remove();
            Sounds.play(Sounds.unlock, x);
        }
        return tile.passable;
    }
    
}