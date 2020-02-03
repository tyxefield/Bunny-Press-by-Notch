package com.mojang.bunnywall.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import com.mojang.bunnywall.BunnyWall;
import com.mojang.bunnywall.Images;
import com.mojang.bunnywall.Sounds;
import com.mojang.bunnywall.level.Tile;

public class Bunny extends Entity
{
    private static final int JUMP_DURATION = 16;

    private static BufferedImage[][] bunnySheet = Images.cut("/bunnies.png", 16, 16);
    private int dir = 0;
    private int walkStep = 0;
    private int blinkDelay = 0;
    private int jumpTime = 0;
    private int color = 0;
    private boolean eatingCarrot = false;
    private int carrotTime = 0;

    public Bunny(int x, int y)
    {
        this.x = x;
        this.y = y;
        dir = (int) (Math.random() * 4);
        color = (int) (Math.random() * 8);
        canShove = false;
        health = 8;
        crushStrength = 8;
    }

    public void startDragging()
    {
        super.startDragging();
        jumpTime = 0;
    }

    protected boolean canPass(Tile tile)
    {
        if (tile.carrot && !eatingCarrot)
        {
            tile.carrot = false;
            tile.passable = true;
            level.renderTile(tile, 1, 1);
            eatCarrot();
            //            level.addEntity(new Carrot(tile.x*16+8, tile.y*16+8));
        }
        return tile.passable;
    }

    public void eatCarrot()
    {
        Sounds.play(Sounds.omnomnom, x);
        eatingCarrot = true;
        dir = 0;
    }

    public void tick()
    {
        super.tick();
        int xa = 0;
        int ya = 0;

        if (blinkDelay-- == 0)
        {
            blinkDelay = (int) (Math.random() * 300 + 100);
        }
        if (isDragged) return;
        if (eatingCarrot)
        {
            carrotTime++;
            if (carrotTime >= BunnyWall.TICKS_PER_SECOND * 3)
            {
                carrotTime = 0;
                eatingCarrot = false;
            }
        }

        if (jumpTime == 0)
        {
            if (eatingCarrot) return;
            checkVincinity();
        }
        else
        {
            jumpTime--;
            if (dir == 0) attemptMove(0, 1);
            if (dir == 1) attemptMove(0, -1);
            if (dir == 2) attemptMove(1, 0);
            if (dir == 3) attemptMove(-1, 0);
        }


        if (xa != 0 || ya != 0)
        {
            attemptMove(xa, ya);
            walkStep++;
        }
    }

    private void checkVincinity()
    {
        int r = 64;
        Entity mostInteresting = null;
        List<Entity> nearby = level.getEntities(this, x - r, y - r, x + r, y + r);
        for (int i = 0; i < nearby.size(); i++)
        {
            Entity e = nearby.get(i);
            int rr = e.bunnyInterestRange;
            if (e.distanceToSqr(this) < rr * rr)
            {
                if ((mostInteresting == null || mostInteresting.bunnyInterest < e.bunnyInterest) && e.bunnyInterest > 0)
                {
                    mostInteresting = e;
                }
            }
        }

        if (mostInteresting instanceof Player)
        {
            int xd = mostInteresting.x - x;
            int yd = mostInteresting.y - y;

            moveTowards(xd, yd);
        }
        if (mostInteresting instanceof Carrot)
        {
            int xd = mostInteresting.x - x;
            int yd = mostInteresting.y - y;
            if (xd * xd + yd * yd < 16 * 16)
            {
                eatCarrot();
                mostInteresting.remove();
                mostInteresting = null;
            }
            else
            {
                moveTowards(-xd, -yd);
            }
        }
        if (mostInteresting == null && Math.random() < 0.05)
        {
            jumpTime = JUMP_DURATION;
            dir = (int) (Math.random() * 4);
        }
    }

    private void moveTowards(int xd, int yd)
    {
        jumpTime = JUMP_DURATION;
        boolean sideWaysJump = Math.random() < 0.25;

        if ((Math.abs(yd) > Math.abs(xd)) ^ (sideWaysJump))
        {
            if ((yd > 0) ^ (sideWaysJump && Math.random() < 0.3))
                dir = 1;
            else
                dir = 0;
        }
        else
        {
            if ((xd > 0) ^ (sideWaysJump && Math.random() < 0.3))
                dir = 3;
            else
                dir = 2;
        }
    }

    public void render(Graphics g)
    {
        int yHead = 8;
        int walkAnim = (walkStep / 6) & 3;
        if (walkAnim == 3) walkAnim = 1;

        int headFrame = 0;
        if (blinkDelay < 10) headFrame = 3;

        if (jumpTime > 0) walkAnim = 2;
        if (jumpTime > JUMP_DURATION / 2) walkAnim = 1;
        if (walkAnim > 0) headFrame = walkAnim;

        if (crushTime > 0) headFrame = 4;

        z = (int) (Math.sin((jumpTime) * Math.PI / (JUMP_DURATION + 5)) * 8);
        if (isDragged) z = 4;
        g.drawImage(shadowSheet[0][0], x - 8, y - 7, null);


        if (carrotTime > 0)
        {
            yHead -= carrotTime / 4 % 2;
        }

        g.drawImage(bunnySheet[0 + walkAnim + color % 2 * 8][dir + color / 2 * 4], x - 8 + crushTime / 2 % 2, y - 10 - z, null);
        g.drawImage(bunnySheet[3 + headFrame + color % 2 * 8][dir + color / 2 * 4], x - 8 + crushTime / 2 % 2, y - 10 - yHead - z, null);

        if (eatingCarrot)
        {
            g.drawImage(objectSheet[1][0], x - 8 + crushTime / 2 % 2, y - 16 - (isDragged ? 6 : 0) + 2 - carrotTime / 4 % 2, null);
        }
    }

    public void crushed(Crusher crusher)
    {
        Sounds.play(Sounds.splat, x);
        if (crusher != null)
        {
            for (int i = 0; i < 6; i++)
            {
                crusher.addScratch(x + (int) (Math.random() * 16 - 8), y + (int) (Math.random() * 16 - 8), (int) (Math.random() * 80 + 20) << 16);
            }
        }
        for (int i = 0; i < 20; i++)
            level.addEntity(new Splat(x, y, 0));
    }


    public void rockCrush()
    {
        health = 0;
        crush(null);
    }
}