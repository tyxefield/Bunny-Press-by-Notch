package com.mojang.bunnywall.entity;

import java.awt.Graphics;
import java.util.List;

import com.mojang.bunnywall.Sounds;

public class Rock extends Entity
{
    private int xa, ya;
    private int rollTime = 0;

    public Rock(int x, int y)
    {
        this.x = x;
        this.y = y;
        this.xRad = this.yRad = 6;
        crushStrength = 90;
        health = 20;
        this.canShove = false;
    }

    public void tick()
    {
        super.tick();

        if (xa != 0 || ya != 0)
        {
            if (!attemptMove(xa, ya))
            {
                List<Entity> crushables = level.getEntities(this, x + xa - xRad, y + ya - yRad, x + xa + xRad, y + ya + yRad);
                if (crushables.size() > 0) crushables.get(0).rockCrush();

                rollTime = 0;
                xa = ya = 0;
            }
            else
            {
                if (--rollTime <= 0)
                {
                    xa = ya = 0;
                }
            }
        }
    }

    protected boolean shove(Entity source, int xa, int ya)
    {
        if (!(source instanceof Player)) return super.shove(source, xa, ya);

        if (super.shove(source, xa, ya))
        {
            Sounds.play(Sounds.roll, x);
            this.xa = xa;
            this.ya = ya;
            rollTime = 64;
            return true;
        }
        return false;
    }

    public void render(Graphics g)
    {
        g.drawImage(objectSheet[rollTime / 4 % 3][1], x - 8 + crushTime / 2 % 2, y - 16, null);
    }

    public void crushed(Crusher crusher)
    {
        Sounds.play(Sounds.rockbreak, x);
        for (int i = 0; i < 10; i++)
            level.addEntity(new Splat(x, y, 2));
    }
}