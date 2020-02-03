package com.mojang.bunnywall;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class WinScreen extends Screen
{
    private static BufferedImage winScreen = Images.load("/winscreen.png");
    private int passedTime = 0;

    public void tick()
    {
        if (passedTime++<BunnyWall.TICKS_PER_SECOND*2/3)
        {
            clicked = false;
            return;
        }
        if (clicked || bunnyWall.controller.buttons[Controller.BUTTON_GRAB])
        {
            bunnyWall.setScreen(new IntroScreen());
        }
    }
    
    public void render(Graphics g)
    {
        g.drawImage(winScreen, 0, 0, null);
    }
}