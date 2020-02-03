package com.mojang.bunnywall;

import java.applet.Applet;
import java.awt.BorderLayout;

public class BunnyWallApplet extends Applet
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private BunnyWall bunnyWall;
    
    public BunnyWallApplet()
    {
    }
    
    public void init()
    {
        bunnyWall = new BunnyWall();
        setLayout(new BorderLayout());
        add(bunnyWall, BorderLayout.CENTER);
    }
    
    public void start()
    {
        bunnyWall.start();
    }

    public void stop()
    {
        bunnyWall.stop();
    }
}