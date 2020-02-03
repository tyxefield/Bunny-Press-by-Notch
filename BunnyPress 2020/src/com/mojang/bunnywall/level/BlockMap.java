package com.mojang.bunnywall.level;

import java.awt.Color;
import java.awt.Graphics;
import java.util.*;

import com.mojang.bunnywall.entity.Entity;


public class BlockMap
{
    private class Slot
    {
        private int xSlot, ySlot;

        public Slot init(int x, int y)
        {
            xSlot = x / SCALE;
            ySlot = y / SCALE;
            if (xSlot < 0) xSlot = 0;
            if (ySlot < 0) ySlot = 0;
            if (xSlot >= width) xSlot = width - 1;
            if (ySlot >= height) ySlot = height - 1;
            return this;
        }

        public void add(Entity entity)
        {
            if (xSlot>=0 && ySlot>=0)
                entityGrid[xSlot][ySlot].add(entity);
        }

        public void remove(Entity entity)
        {
            if (xSlot>=0 && ySlot>=0)
                entityGrid[xSlot][ySlot].remove(entity);
        }
    }

    private static final int SCALE = 16 * 2;
    private int width, height;
    private Slot slot = new Slot();
    private Slot slot2 = new Slot();
    
    private int lx0, ly0, lx1, ly1;

    public List<Entity>[][] entityGrid;
    
    public List<Entity> huge = new ArrayList<Entity>();

    @SuppressWarnings("unchecked")
    public BlockMap(int newWidth, int newHeight)
    {
        this.width = newWidth / SCALE;
        this.height = newHeight / SCALE;
        
        if (width==0) width=1;
        if (height==0) height=1;
        entityGrid = new ArrayList[width][height];
        
        for (int x=0; x<width; x++)
            for (int y=0; y<height; y++)
                entityGrid[x][y] = new ArrayList<Entity>();
    }

    public void insert(Entity entity)
    {
        if (entity.xRad>16 || entity.yRad>16)
        {
            huge.add(entity);
        }
        else
        {
            getSlot(entity.x, entity.y).add(entity);
        }
        entity.xOld = entity.x;
        entity.yOld = entity.y;
        entity.blockMap = this;
    }

    public void remove(Entity entity)
    {
        if (entity.xRad>16 || entity.yRad>16)
        {
            huge.remove(entity);
        }
        else
        {
            getSlot(entity.xOld, entity.yOld).remove(entity);
        }
    }

    public void moved(Entity entity)
    {
        Slot s1 = slot.init(entity.xOld, entity.yOld);
        Slot s2 = slot2.init(entity.x, entity.y);

        if (s1.equals(s2)) return;

        s1.remove(entity);
        s2.add(entity);
        entity.xOld = entity.x;
        entity.yOld = entity.y;
    }

    private Slot getSlot(int x, int y)
    {
        return slot.init(x, y);
    }

    public List<Entity> getEntities(Entity source, int x0, int y0, int x1, int y1)
    {
        return getEntities(source, x0, y0, x1, y1, new ArrayList<Entity>());
    }

    public List<Entity> getEntities(Entity source, int x0, int y0, int x1, int y1, List<Entity> entities)
    {
        
        Slot s1 = slot.init(x0, y0);
        Slot s2 = slot2.init(x1, y1);

        lx0 = s1.xSlot;
        ly0 = s1.ySlot;
        lx1 = s2.xSlot;
        ly1 = s2.ySlot;

        for (int x = s1.xSlot-1; x <= s2.xSlot+1; x++)
            for (int y = s1.ySlot-1; y <= s2.ySlot+1; y++)
            {
                if (x>=0 && y>=0 && x<width && y<height)
                {
                    for (int i=0; i<entityGrid[x][y].size(); i++)
                    {
                        Entity e = entityGrid[x][y].get(i);
                        if (e == source) continue;
                        if (e.intersects(x0, y0, x1, y1)) entities.add(e);
                    }
                }
            }
        
        for (int i=0; i<huge.size(); i++)
        {
            Entity e = huge.get(i);
            if (e == source) continue;
            if (e.intersects(x0, y0, x1, y1)) entities.add(e);
        }
/*        for (int i=0; i<entities.size(); i++)
        {
            Entity e = entities.get(i);
            if (e.x+e.xRad<x0 || e.y+e.yRad<y0 || e.x-e.xRad>x1 || e.y-e.yRad>y1)
                entities.remove(i--);
        }*/ 

        return entities;
    }
    
    /*public List<Entity> getIntersecting(int x, int y, int xRadius, int yRadius)
    {
        int x0 = x-xRadius;
        int y0 = y-yRadius;
        int x1 = x+xRadius;
        int y1 = y+yRadius;
        
        List<Entity> entities = getEntities(null, x0-16, y0-16, x1+16, y1+16);
        for (int i=0; i<entities.size(); i++)
        {
            Entity e = entities.get(i);
            if (e.x+e.xRad<x0 || e.y+e.yRad<y0 || e.x-e.xRad>x1 || e.y-e.yRad>y1)
                entities.remove(i--);
        }
        
        return entities;
    }*/
    
    public void render(Graphics gr)
    {
        for (int x=0; x<width; x++)
            for (int y=0; y<height; y++)
            {
                gr.setColor(Color.WHITE);
                
                if (x>=lx0 && y>=ly0 && x<=lx1 && y<=ly1)
                {
                    gr.setColor(Color.YELLOW);
                }
                
                gr.drawRect(x*SCALE, y*SCALE, SCALE-1, SCALE-1);
                gr.drawString("Count: "+entityGrid[x][y].size(), x*SCALE+3, y*SCALE+12);
            }
   }

/*    public List<Entity> getIntersecting(Entity e)
    {
        return getIntersecting(e.x, e.y, e.xRad, e.yRad);
    }*/

    public void clear()
    {
        for (int x=0; x<width; x++)
            for (int y=0; y<height; y++)
                entityGrid[x][y].clear();
   }
}