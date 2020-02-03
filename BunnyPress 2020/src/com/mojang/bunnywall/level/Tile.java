package com.mojang.bunnywall.level;

public class Tile
{
    public final int x, y;
    
    public Tile(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    
    public boolean passable = true;
    public boolean slippery = false;
    public boolean exit = false;
    public boolean lock = false;
    public boolean carrot = false;
}