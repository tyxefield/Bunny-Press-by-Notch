package com.mojang.bunnywall.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.mojang.bunnywall.Images;
import com.mojang.bunnywall.level.Tile;

public class Crusher extends Entity
{
    private class Scratch
    {
        public int p;
        public int color;
        public int life;
        private int r;
        private int alpha;

        public Scratch(int p, int col, int life, int alpha, int r)
        {
            this.p = p;
            this.color = col;
            this.life = life;
            this.alpha = alpha;
            this.r = r;
        }

        public void tick()
        {
            int xp = x;
            int yp = y;

            int rr = 6;
            if (dir == DIR_LEFT || dir == DIR_RIGHT)
            {
                yp += -length * 8 + p;
                if (dir == DIR_LEFT) xp -= rr;
                if (dir == DIR_RIGHT) xp += rr;
            }
            else
            {
                xp += -length * 8 + p;
                if (dir == DIR_UP) yp -= rr;
                if (dir == DIR_DOWN) yp += rr;
            }

            int alpha = life * 2;
            if (alpha > 128) alpha = 128;
            p+=(int)((Math.random()*3-1.5)*Math.random());
            if (p<0) p = 0;
            if (p>length*16) p = length*16;
            if (Math.random() < alpha / 128.0)
            {
                int rrr = (r * alpha / 128 + 1);
                alpha = alpha * this.alpha / 256;
                level.taint(xp, yp, (alpha << 24) | color, rrr);
            }
            life--;
        }
    }
    public static final int DIR_RIGHT = 0;
    public static final int DIR_LEFT = 1;
    public static final int DIR_UP = 2;
    public static final int DIR_DOWN = 3;

    public static BufferedImage[][] crusherSheet = Images.cut("/crusher.png", 16, 16);
    private int length;
    private int dir;
    private int tick = 0;
    private int speed;

    private List<Scratch> scratches = new ArrayList<Scratch>();

    public Crusher(int x, int y, int length, int dir, int speed)
    {
        this.speed = speed;
        this.x = x * 16;
        this.y = y * 16;

        if (dir == DIR_LEFT || dir == DIR_RIGHT)
        {
            yRad = length * 8;
            xRad = 8;
            this.x += 8;
            this.y += length * 8;
        }
        else
        {
            yRad = 8;
            xRad = length * 8;
            this.x += length * 8;
            this.y += 8;
        }

        this.dir = dir;
        this.length = length;
        isDragable = false;
        
        for (int i=0; i<200; i++)
        {
            scratches.add(new Scratch((int) (Math.random() * length * 16), 0x000000, (int) (Math.random()*Math.random() * 128 + 32), (int) (Math.random() * 100 + 40), (int) (Math.random() * 2)));
        }
        this.crushStrength = 1000000000;
    }

    public void render(Graphics g)
    {
        for (int tile = -1; tile <= length; tile++)
        {
            int xImg = 0;
            int yImg = 0;
            int imgStep = 0;
            if (tile == -1) imgStep = -1;
            if (tile == length) imgStep = 1;
            if (dir == DIR_LEFT || dir == DIR_RIGHT)
            {
                yImg = 1;
                xImg = dir == DIR_RIGHT ? 0 : 1;
                g.drawImage(crusherSheet[xImg][yImg + imgStep], x - 8, y + tile * 16 - length * 8, null);
            }
            else
            {
                xImg = 1;
                yImg = dir == DIR_DOWN ? 3 : 4;
                g.drawImage(crusherSheet[xImg + imgStep][yImg], x + tile * 16 - length * 8, y - 8, null);
            }
        }
    }

    protected boolean canPass(Tile tile)
    {
        return true;
    }

    protected boolean isFree(int xp, int yp)
    {
        return true;
    }

    public void tick()
    {
        if (++tick<speed) return;
        tick = 0;

        int xa = 0;
        int ya = 0;

        if (dir == DIR_RIGHT) xa = 1;
        if (dir == DIR_LEFT) xa = -1;
        if (dir == DIR_UP) ya = -1;
        if (dir == DIR_DOWN) ya = 1;

        boolean moved = attemptMove(xa, ya);
        if (!moved)
        {
            List<Entity> crushables = new ArrayList<Entity>();
            getAllCrushables(xa, ya, crushables);

            Entity weakest = null;
            for (int i = 0; i < crushables.size(); i++)
            {
                Entity e = crushables.get(i);
                if (weakest == null || e.crushStrength < weakest.crushStrength) weakest = e;
            }
            if (weakest != null) weakest.crush(this);
        }
        else
        {
            for (int i = 0; i < scratches.size(); i++)
            {
                Scratch scratch = scratches.get(i);
                scratch.tick();
                if (scratch.life <= 0)
                {
                    scratches.remove(i--);
                }
            }

            scratches.add(new Scratch((int) (Math.random() * length * 16), 0x000000, (int) (Math.random() * 64 + 32), (int) (Math.random() * 100 + 40), (int) (Math.random() * 2)));
        }
    }

    public void addScratch(int x, int y, int col)
    {
        int p = 0;
        if (dir == DIR_LEFT || dir == DIR_RIGHT)
        {
            p = y - (this.y - length * 8);
        }
        else
        {
            p = x - (this.x - length * 8);
        }
        scratches.add(new Scratch(p, col, (int) (Math.random()*Math.random() * 128 + 32), 128, (int) (Math.random() * 4+2 )));
    }
    
    protected boolean shove(Entity source, int xa, int ya)
    {
        return false;
    }

    protected boolean couldShove(Entity source, int xa, int ya)
    {
        return false;
    }

    public void crush(Crusher crusher)
    {
    }
}