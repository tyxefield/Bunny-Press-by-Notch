package com.mojang.bunnywall;

import java.awt.Graphics;

public class Screen
{
    protected BunnyWall bunnyWall;
    public boolean clicked = false;
    
    public void tick()
    {
    }
    
    public void render(Graphics g)
    {
    }

    public void init(BunnyWall bunnyWall)
    {
        this.bunnyWall = bunnyWall;
        init();
    }
    
    public void init()
    {
    }
}