package com.mojang.bunnywall.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import com.mojang.bunnywall.Images;
import com.mojang.bunnywall.level.BlockMap;
import com.mojang.bunnywall.level.Level;
import com.mojang.bunnywall.level.Tile;

public class Entity implements Comparable<Entity>
{
    protected static BufferedImage[][] objectSheet = Images.cut("/objects.png", 16, 24);
    protected static BufferedImage[][] shadowSheet = Images.cut("/shadows.png", 16, 16);

    private boolean shouldRemove = false;
    public int x, y, z;
    public int xRad = 4, yRad = 4;

    public int health = 4;
    public int crushStrength = 10;
    public int crushTime = 0;

    protected Level level;
    protected boolean canShove = true;
    protected boolean isPhysical = true;
    public boolean isDragable = true;
    protected boolean isDragged = false;
    protected int bunnyInterest = 0;
    protected int bunnyInterestRange = 64;
    public int xOld, yOld;
    public BlockMap blockMap;

    public void init(Level level)
    {
        this.level = level;
    }

    public void tick()
    {
        if (crushTime > 0) crushTime--;
    }

    public void remove()
    {
        shouldRemove = true;
    }

    public boolean shouldRemove()
    {
        return shouldRemove;
    }

    public void render(Graphics g)
    {
    }

    private int lastCrushTick = -1;

    public void getAllCrushables(int xa, int ya, List<Entity> crushables)
    {
        if (lastCrushTick == level.levelTick) return;
        lastCrushTick = level.levelTick;

        if (xa > 0) if (!isFree(x + xa + xRad - 1, y + ya - yRad + 0) || !isFree(x + xa + xRad - 1, y + ya + yRad - 1)) return;
        if (xa < 0) if (!isFree(x + xa - xRad + 0, y + ya - yRad + 0) || !isFree(x + xa - xRad + 0, y + ya + yRad - 1)) return;
        if (ya > 0) if (!isFree(x + xa - xRad + 0, y + ya + yRad - 1) || !isFree(x + xa + xRad - 1, y + ya + yRad - 1)) return;
        if (ya < 0) if (!isFree(x + xa - xRad + 0, y + ya - yRad + 0) || !isFree(x + xa + xRad - 1, y + ya - yRad + 0)) return;
        List<Entity> collisions = level.getEntities(this, x + xa - xRad, y + ya - yRad, x + xa + xRad, y + ya + yRad);

        if (collisions.size() > 0)
        {
            for (int i = 0; i < collisions.size(); i++)
            {
                Entity e = collisions.get(i);
                if (!e.couldShove(this, xa, ya))
                {
                    crushables.add(e);
                    e.getAllCrushables(xa, ya, crushables);
                }
            }
        }
    }

    public boolean couldMove(int xa, int ya, boolean shove)
    {
        boolean moved = false;
        if (xa != 0) moved |= couldMove2(xa, 0, shove);
        if (ya != 0) moved |= couldMove2(0, ya, shove);
        return moved;
    }

    public boolean couldMove2(int xa, int ya, boolean shove)
    {
        if (xa > 0) if (!isFree(x + xa + xRad - 1, y + ya - yRad + 0) || !isFree(x + xa + xRad - 1, y + ya + yRad - 1)) return false;
        if (xa < 0) if (!isFree(x + xa - xRad + 0, y + ya - yRad + 0) || !isFree(x + xa - xRad + 0, y + ya + yRad - 1)) return false;
        if (ya > 0) if (!isFree(x + xa - xRad + 0, y + ya + yRad - 1) || !isFree(x + xa + xRad - 1, y + ya + yRad - 1)) return false;
        if (ya < 0) if (!isFree(x + xa - xRad + 0, y + ya - yRad + 0) || !isFree(x + xa + xRad - 1, y + ya - yRad + 0)) return false;
        List<Entity> collisions = level.getEntities(this, x + xa - xRad, y + ya - yRad, x + xa + xRad, y + ya + yRad);

        if (collisions.size() > 0)
        {
            if (!shove) return false;
            boolean ok = true;
            for (int i = 0; i < collisions.size(); i++)
            {
                if (!collisions.get(i).couldShove(this, xa, ya)) ok = false;
            }
            if (!ok) return false;
        }

        return true;
    }

    public boolean attemptMove(int xa, int ya)
    {
        return attemptMove(xa, ya, canShove);
    }

    public boolean attemptMove(int xa, int ya, boolean shove)
    {
        boolean moved = false;
        if (xa != 0) moved |= attemptMove2(xa, 0, shove);
        if (ya != 0) moved |= attemptMove2(0, ya, shove);
        return moved;
    }

    public boolean attemptMove2(int xa, int ya, boolean shove)
    {
        if (xa > 0) if (!isFree(x + xa + xRad - 1, y + ya - yRad + 0) || !isFree(x + xa + xRad - 1, y + ya + yRad - 1)) return false;
        if (xa < 0) if (!isFree(x + xa - xRad + 0, y + ya - yRad + 0) || !isFree(x + xa - xRad + 0, y + ya + yRad - 1)) return false;
        if (ya > 0) if (!isFree(x + xa - xRad + 0, y + ya + yRad - 1) || !isFree(x + xa + xRad - 1, y + ya + yRad - 1)) return false;
        if (ya < 0) if (!isFree(x + xa - xRad + 0, y + ya - yRad + 0) || !isFree(x + xa + xRad - 1, y + ya - yRad + 0)) return false;
        List<Entity> collisions = level.getEntities(this, x + xa - xRad, y + ya - yRad, x + xa + xRad, y + ya + yRad);

        if (collisions.size() > 0)
        {
            if (!shove) return false;
            boolean ok = true;
            for (int i = 0; i < collisions.size(); i++)
            {
                if (!collisions.get(i).shove(this, xa, ya)) ok = false;
            }
            if (!ok) return false;
        }

        x += xa;
        y += ya;

        blockMap.moved(this);

        return true;
    }

//    private int lastShoveTick = -1;

    protected boolean shove(Entity source, int xa, int ya)
    {
//        if (lastShoveTick == level.levelTick) return false;
//        lastShoveTick = level.levelTick;
        return attemptMove(xa, ya, true);
    }

    protected boolean couldShove(Entity source, int xa, int ya)
    {
//        if (lastShoveTick == level.levelTick) return false;
//        lastShoveTick = level.levelTick;
        return couldMove(xa, ya, true);
    }

    protected boolean isFree(int xp, int yp)
    {
        return canPass(level.getTileAtPixel(xp, yp));
    }

    protected boolean canPass(Tile tile)
    {
        return tile.passable;
    }

    public int compareTo(Entity e)
    {
        if (e.y > y) return -1;
        if (e.y < y) return 1;
        if (e.z > z) return -1;
        if (e.z < z) return 1;
        return 0;
    }

    public boolean intersects(int x0, int y0, int x1, int y1)
    {
        if (!isPhysical) return false;
        return !(x - xRad >= x1 || x + xRad <= x0 || y - yRad >= y1 || y + yRad <= y0);
    }

    public void crush(Crusher crusher)
    {
        crushTime = 10;
        if (--health <= 0)
        {
            remove();
            crushed(crusher);
        }
    }

    public void crushed(Crusher crusher)
    {
    }

    public int distanceToSqr(Entity e)
    {
        int xd = e.x - x;
        int yd = e.y - y;
        return xd * xd + yd * yd;
    }

    public void stopDragging()
    {
        isDragged = false;
    }

    public void startDragging()
    {
        isDragged = true;
    }

    public void rockCrush()
    {
    }
}