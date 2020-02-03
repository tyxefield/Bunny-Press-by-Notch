package com.mojang.bunnywall;

import java.awt.Graphics;

import com.mojang.bunnywall.entity.Player;
import com.mojang.bunnywall.level.Level;

public class GameScreen extends Screen
{
    public int levelNum = Level.START_LEVEL-1;
    public Level level;
    
    public void tick()
    {
        level.tick();
    }
    
    public void render(Graphics g)
    {
        level.render(g);
    }
    
    public void init()
    {
        newLevel();
    }

    public void newLevel()
    {
       level = new Level(this, levelNum, new Player(bunnyWall.controller));
    }

    public void levelWon()
    {
        if (levelNum==10) 
        {
            bunnyWall.setScreen(new WinScreen());
            return;
        }
        levelNum++;
        newLevel();
        bunnyWall.setScreen(new LevelTitleScreen(this));
    }

    public void levelLost()
    {
        newLevel();
        bunnyWall.setScreen(new LevelTitleScreen(this));
    }
}