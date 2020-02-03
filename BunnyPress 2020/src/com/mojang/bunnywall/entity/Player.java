package com.mojang.bunnywall.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import com.mojang.bunnywall.Controller;
import com.mojang.bunnywall.Images;
import com.mojang.bunnywall.Sounds;
import com.mojang.bunnywall.level.Tile;

public class Player extends Entity
{
    private Controller controller;
    private static BufferedImage[][] playerSheet = Images.cut("/char.png", 16, 16);
    private int dir = 0;
    private int walkStep = 0;
    private int blinkDelay = 0;
    private boolean grabbing = false;
    private Entity grabbed = null;
    //private float speed = 2;
    int xa = 0;
    int ya = 0;

    public Player(Controller controller)
    {
        this.controller = controller;
        x = 160;
        y = 120;
        this.crushStrength = 50;
        this.health = 8;
        bunnyInterest = 100;
        bunnyInterestRange = 32;
    }

    public void tick()
    {
        super.tick();
        boolean up = controller.buttons[Controller.BUTTON_UP];
        boolean left = controller.buttons[Controller.BUTTON_LEFT];
        boolean right = controller.buttons[Controller.BUTTON_RIGHT];
        boolean down = controller.buttons[Controller.BUTTON_DOWN];
        boolean grab = false;
        //        boolean grab = controller.buttons[Controller.BUTTON_GRAB];

        if (blinkDelay-- == 0)
        {
            blinkDelay = (int) (Math.random() * 300 + 100);
        }

        Tile tile = level.getTileAtPixel(x, y);
        if (tile.exit)
        {
            remove();
            level.win();
            Sounds.play(Sounds.woohoo, x);
            return;
        }

        if (tile.slippery && (xa != 0 || ya != 0))
        {
            walkStep = walkStep / 3 * 3;
            walkStep += 2;
        }
        else
        {
            xa = 0;
            ya = 0;

            if (left)
            {
                if (!grabbing) dir = 3;
                //xa-= speed;
                xa--;
            }
            else if (right)
            {
                if (!grabbing) dir = 2;
                //xa+= speed;
                xa++;
            }
            if (up)
            {
                if (!grabbing) dir = 1;
                //ya-= speed;
                ya--;
            }
            else if (down)
            {
                if (!grabbing) dir = 0;
                //ya+=speed;
                ya++;
            }
        }

        if (grab && !grabbing)
        {
            attemptGrab();
        }

        if ((grabbed != null && (grabbed.shouldRemove() || !isStillGrabbed())) || !grab)
        {
            if (grabbed != null) grabbed.stopDragging();
            grabbing = false;
            grabbed = null;
        }

        if (xa != 0 || ya != 0)
        {
            int xOld = x;
            int yOld = y;
            if (!attemptMove(xa, ya))
            {
                xa = 0;
                ya = 0;
            }
            if (grabbed != null)
            {
                grabbed.attemptMove(x - xOld, y - yOld);
            }
            walkStep++;
            if (walkStep % 24 == 0)
            {
                Sounds.play(Sounds.step1, x);
            }
            if (walkStep % 24 == 12)
            {
                Sounds.play(Sounds.step2, x);
            }
        }
    }

    private boolean isStillGrabbed()
    {
        int xd = 0;
        int yd = 0;
        if (dir == 0) yd = +(yRad + 4);
        if (dir == 1) yd = -(yRad + 4);
        if (dir == 2) xd = +(xRad + 4);
        if (dir == 3) xd = -(xRad + 4);

        List<Entity> entities = level.getEntities(this, x + xd, y + yd, x + xd, y + yd);
        for (int i = 0; i < entities.size(); i++)
            if (entities.get(i) == grabbed) return true;
        return false;
    }

    private void attemptGrab()
    {
        int xd = 0;
        int yd = 0;
        if (dir == 0) yd = +(yRad + 4);
        if (dir == 1) yd = -(yRad + 4);
        if (dir == 2) xd = +(xRad + 4);
        if (dir == 3) xd = -(xRad + 4);

        List<Entity> entities = level.getEntities(this, x + xd, y + yd, x + xd, y + yd);
        for (int i = 0; i < entities.size(); i++)
        {
            Entity e = entities.get(i);
            if (e.isDragable)
            {
                grabbed = e;
                grabbed.startDragging();
                grabbing = true;
            }
        }
    }

    public void render(Graphics g)
    {
        int yHead = 12;
        int walkAnim = (walkStep / 6) & 3;
        if (walkAnim == 3) walkAnim = 1;
        if (walkAnim == 1) yHead = 13;
        if (grabbing) walkAnim += 3;

        int headFrame = 0;
        if (blinkDelay < 10) headFrame = 1;

        if (crushTime > 0) headFrame = 2;

        g.drawImage(shadowSheet[0][0], x - 8, y - 8, null);
        g.drawImage(playerSheet[0 + walkAnim][dir], x - 8 + crushTime / 2 % 2, y - 15, null);
        g.drawImage(playerSheet[6 + headFrame][dir], x - 8 + crushTime / 2 % 2, y - 15 - yHead, null);
    }

    public void crushed(Crusher crusher)
    {
        Sounds.play(Sounds.splat, x);
        Sounds.play(Sounds.death, x);
        if (crusher != null)
        {
            for (int i = 0; i < 6; i++)
            {
                crusher.addScratch(x + (int) (Math.random() * 16 - 8), y + (int) (Math.random() * 16 - 8), (int) (Math.random() * 80 + 20) << 16);
            }
        }
        for (int i = 0; i < 60; i++)
            level.addEntity(new Splat(x, y, 0));
    }
}