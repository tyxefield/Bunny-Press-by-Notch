package com.mojang.bunnywall;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class IntroScreen extends Screen
{
    private static BufferedImage logoScreen = Images.load("/title.png");
    private GameScreen gameScreen = new GameScreen();
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
            bunnyWall.setScreen(new LevelTitleScreen(gameScreen));
        }
    }
    
    public void render(Graphics g)
    {
        g.drawImage(logoScreen, 0, 0, null);
    }
}