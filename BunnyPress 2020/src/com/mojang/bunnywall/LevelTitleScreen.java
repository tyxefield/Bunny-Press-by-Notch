package com.mojang.bunnywall;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class LevelTitleScreen extends Screen
{
    private static BufferedImage levelTileScreen = Images.load("/levelsign.png");
    private GameScreen gameScreen;
    private int remainingTicks = BunnyWall.TICKS_PER_SECOND*3;
    private int passedTime = 0;
    
    public LevelTitleScreen(GameScreen gameScreen)
    {
        this.gameScreen = gameScreen;
    }
    
    public void init(BunnyWall bunnyWall)
    {
        super.init(bunnyWall);
        gameScreen.init(bunnyWall);
    }

    public void tick()
    {
        if (passedTime++<BunnyWall.TICKS_PER_SECOND*2/3)
        {
            clicked = false;
            return;
        }
        if (clicked || --remainingTicks<=0 || bunnyWall.controller.buttons[Controller.BUTTON_GRAB])
        {
            bunnyWall.setScreen(gameScreen);
        }
    }
    
    public void render(Graphics g)
    {
        g.drawImage(levelTileScreen, 0, 0, null);
        String titleStr = "Level "+(gameScreen.level.levelNum+1);

        g.setFont(new Font("sans-seriff", Font.BOLD, 30));
        FontMetrics fm = g.getFontMetrics();
        g.setColor(Color.BLACK);
        g.drawString(titleStr, (320-fm.stringWidth(titleStr))/2, 120);
        g.setColor(Color.WHITE);
        g.drawString(titleStr, (320-fm.stringWidth(titleStr))/2-1, 120-2);
        
        String str = gameScreen.level.getInfoString();
        g.setFont(new Font("sans-seriff", Font.PLAIN, 15));
        fm = g.getFontMetrics();
        g.setColor(Color.BLACK);
        g.drawString(str, (320-fm.stringWidth(str))/2, 150);
        g.setColor(Color.WHITE);
        g.drawString(str, (320-fm.stringWidth(str))/2-1, 150-1);
        
    }
}