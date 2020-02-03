package com.mojang.bunnywall.level;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.*;

import com.mojang.bunnywall.BunnyWall;
import com.mojang.bunnywall.GameScreen;
import com.mojang.bunnywall.Images;
import com.mojang.bunnywall.entity.*;

public class Level
{
    public static final int START_LEVEL = 1;
    private static BufferedImage[][] tileSheet = Images.cut("/tiles.png", 16, 16);
    public List<Entity> entities = new ArrayList<Entity>();

    private BufferedImage background = new BufferedImage(BunnyWall.GAME_WIDTH, BunnyWall.GAME_HEIGHT, BufferedImage.TYPE_INT_RGB);

    private Tile[][] tiles;
    private Player player;
    public final int width = 20, height = 15;
    private int dieTime = 0;
    private int winTime = 0;
    private boolean won = false;

    private BlockMap blockMap = new BlockMap(width * 16, height * 16);

    public int levelTick = 0;
    public int pauseTicks = BunnyWall.TICKS_PER_SECOND * 2 / 3;

    private String[] levelData = new String[] { "" + // 
            "Reach the goal, avoid the press",// 
            "slow_right", //
            "" + //
                    "        b         " + // 
                    "  b  ##########   " + // 
                    "     #      > #   " + // 
                    "     #        #  b" + // 
                    " c   #     b  # ! " + // 
                    "   b #        #   " + // 
                    "     #   #########" + // 
                    " b   #            " + // 
                    "     #  b     b   " + // 
                    "     #            " + // 
                    "     ##########   " + // 
                    "                 b" + // 
                    "      b           " + //
                    "",//

            "Push keys into keyholes", //
            "down", //
            "" + //
                    "            b     " + // 
                    "  !     b         " + // 
                    "                b " + // 
                    "    b             " + // 
                    "##############    " + // 
                    "     #___#___# k  " + // 
                    "     #_______     " + // 
                    "     ____#___#    " + // 
                    "##X###############" + // 
                    "              b   " + // 
                    "    b     b       " + // 
                    "         b    >   " + // 
                    "                  " + // 
                    "",//

            "Run for it!!",// 
            "fast_fast_fast_right,fast_fast_fast_left",//
            "" + //
                    "    o       o     " + // 
                    "     !   b     b  " + // 
                    "   b    o    ###  " + // 
                    "  o ######   # bo " + // 
                    "oobo#      o # o b" + // 
                    " ob #        #o oo" + // 
                    "b     b####### b  " + // 
                    " b  o       o o  b" + // 
                    "   o bo#    ####  " + // 
                    " b######    # o  o" + // 
                    " o  o #        o  " + // 
                    "  b o #  ######  b" + // 
                    "        >#    b   " + // 
                    "",//

            "Double the crush, double the fun", // 
            "fast_fast_up_half,fast_down_half_xplus", // 
            "" + //
                    "         #      b " + // 
                    "   ! b   #        " + // 
                    "           b  b   " + // 
                    "                  " + // 
                    " b    b  #  b     " + // 
                    "         # b   b  " + // 
                    "         #        " + // 
                    "   b  ############" + // 
                    "               b  " + // 
                    "##########        " + // 
                    "         X  k     " + // 
                    "   >     # b      " + // 
                    "         #        " + // 
                    "",//

            "Bunnies like carrots", // 
            "up", // 
            "" + //
                    "    #             " + // 
                    " >  x       b     " + // 
                    "    #  *    c     " + // 
                    "#####       * b   " + // 
                    "      *   b    c  " + // 
                    "    b        b    " + // 
                    "########xx########" + // 
                    " b                " + // 
                    "            c*    " + // 
                    "  c   b   b   b   " + // 
                    "  *               " + // 
                    "     b  !   b     " + // 
                    "                  " + // 
                    "",// 

            "Coaxing the bunnies", // 
            "fast_down", // 
            "" + //
                    "             #  b " + // 
                    "             # b b" + // 
                    "!        *   #b b " + // 
                    "   *         # bb " + // 
                    "            ##    " + // 
                    "           ##   b " + // 
                    "      c    #   b  " + // 
                    "           ###    " + // 
                    " *           # b> " + // 
                    "        *    #   b" + // 
                    "             #    " + // 
                    "    c        x    " + // 
                    "             #  b " + // 
                    "",//

            "Crowd control", // 
            "fast_fast_up,fast_fast_down", // 
            "" + //
                    "bbbbbbbbbbbbbbbbb " + // 
                    "bbbbbbbbbbbbbbbbb " + // 
                    "bbbbbbbbbbbbbbbbb " + // 
                    "bbbbbbbbbbbbbbbb  " + // 
                    "b   bbbbbbbbbbbb  " + // 
                    "  o bbbbbbbbbbb   " + // 
                    "                > " + // 
                    "!  bbbbbbbbbbbb   " + // 
                    "bbbbbbbbbbbbbbbb  " + // 
                    "bbbbbbbbbbbbbbbb  " + // 
                    "bbbbbbbbbbbbbbbbb " + // 
                    "bbbbbbbbbbbbbbbbb " + // 
                    "bbbbbbbbbbbbbbbbb " + // 
                    "",// 

            "Buying some time", // 
            "fast_down", // 
            "" + //
                    "  b        b      " + // 
                    "                  " + // 
                    "     o         !  " + // 
                    " o   b    b       " + // 
                    "    o         b   " + // 
                    "  b o    #########" + // 
                    "    o    #   #    " + // 
                    "     ##### # # #  " + // 
                    " b         # # #  " + // 
                    " o    b    # # #  " + // 
                    "        o  # # #  " + // 
                    "b   o      # # # >" + // 
                    "           #   #  " + // 
                    "",// 

            "Criss the cross", // 
            "fast_fast_fast_up_half_xplus,fast_fast_left_half,fast_fast_down_half,fast_fast_right_half_yplus", // 
            "" + //
                    " b    c   b       " + // 
                    "  ########      c " + // 
                    "  #    b #    b   " + // 
                    "  #  !   #        " + // 
                    "  #  b   #    c   " + // 
                    "b #      #        " + // 
                    "  #          #####" + // 
                    "  #   b   b  # b  " + // 
                    "c ###        # >  " + // 
                    "    #    c   #    " + // 
                    "  b ########## bb " + // 
                    "       c      c   " + // 
                    "   b     b     b  " + // 
                    "",// 

            "Back and forth", // 
            "slow_slow_right", // 
            "" + //
                    "   ####   b     c " + // 
                    "   #  #     x b   " + // 
                    "   #  #  b        " + // 
                    "   #  X   x  b b  " + // 
                    "  k#  #  b   x    " + // 
                    "   #  ############" + // 
                    "   #        c     " + // 
                    "   #              " + // 
                    "   ############   " + // 
                    "                  " + // 
                    "                  " + // 
                    "       xxx      ! " + // 
                    "      x > x       " + // 
                    "",// 

            "Pressure", // 
            "down_half,right_half_xplus,right_half_yplus_xplus", // 
            "" + //
                    "        #     #   " + // 
                    " b      x     #   " + // 
                    "  c  b  x     #   " + // 
                    "b       x  k      " + // 
                    "        x     #   " + // 
                    " b b    #  #####  " + // 
                    " b !    #         " + // 
                    " c    b #         " + // 
                    " b  b   ##########" + // 
                    "b       x     #   " + // 
                    "        x     X > " + // 
                    " bcb    x     #   " + // 
                    "        #     #   " + // 
                    "",// 
            "",//
    };

    private GameScreen gameScreen;
    public int levelNum;

    public Level(GameScreen gameScreen, int levelNum, Player player)
    {
        this.levelNum = levelNum;
        this.gameScreen = gameScreen;
        this.player = player;

        tiles = new Tile[width][height];
        Graphics g = background.getGraphics();
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                int xTile = 1;
                int yTile = 1;
                Tile tile = new Tile(x, y);

                if (x == 0 || y == 0 || x == width - 1 || y == height - 1)
                {
                    xTile = 4;
                    yTile = 1;
                    if (x == 0) xTile--;
                    if (x == width - 1) xTile++;
                    if (y == 0) yTile--;
                    if (y == height - 1) yTile++;
                    tile.passable = false;
                }
                else
                {
                    char ch = charAt(x, y);
                    if (ch == '_')
                    {
                        xTile = 1;
                        yTile = 0;
                        tile.slippery = true;
                    }
                    else if (ch == '>')
                    {
                        xTile = 2;
                        yTile = 1;
                        tile.exit = true;
                    }
                    else if (ch == 'X')
                    {
                        xTile = 0;
                        yTile = 0;
                        tile.lock = true;
                        tile.passable = false;
                    }
                    else if (ch == 'x')
                    {
                        xTile = 0;
                        yTile = 1;
                        tile.carrot = true;
                        tile.passable = false;
                    }
                    else if (ch == '#')
                    {
                        xTile = 6;
                        yTile = 6;
                        boolean u = charAt(x, y - 1) == '#';
                        boolean d = charAt(x, y + 1) == '#';
                        boolean l = charAt(x - 1, y) == '#';
                        boolean r = charAt(x + 1, y) == '#';
                        if (u && !d) yTile = 5;
                        if (u && d) yTile = 4;
                        if (!u && d) yTile = 3;
                        if (l && !r) xTile = 5;
                        if (l && r) xTile = 4;
                        if (!l && r) xTile = 3;
                        tile.passable = false;
                    }
                    else if (ch == '*')
                    {
                        addEntity(new Crate(x * 16 + 8, y * 16 + 8));
                    }
                    else if (ch == 'b')
                    {
                        addEntity(new Bunny(x * 16 + 8, y * 16 + 8));
                    }
                    else if (ch == 'o')
                    {
                        addEntity(new Rock(x * 16 + 8, y * 16 + 8));
                    }
                    else if (ch == 'c')
                    {
                        addEntity(new Carrot(x * 16 + 8, y * 16 + 8));
                    }
                    else if (ch == 'k')
                    {
                        addEntity(new Key(x * 16 + 8, y * 16 + 8));
                    }
                    else if (ch == '!')
                    {
                        player.x = x * 16 + 8;
                        player.y = y * 16 + 8;
                    }
                }

                tiles[x][y] = tile;
                g.drawImage(tileSheet[xTile][yTile], x * 16, y * 16, null);
            }
        }
        g.dispose();

        String crusherData[] = levelData[levelNum * 3 + 1].split(",");
        for (int i = 0; i < crusherData.length; i++)
        {
            String[] words = crusherData[i].split("_");
            int dir = 0;
            int speed = 8;
            int x = 0, y = 0, len = 0;
            for (int j = 0; j < words.length; j++)
            {
                if (words[j].equals("right"))
                {
                    x = 0;
                    y = 1;
                    dir = Crusher.DIR_RIGHT;
                    len = height - 2;
                }
                if (words[j].equals("left"))
                {
                    x = width - 1;
                    y = 1;
                    dir = Crusher.DIR_LEFT;
                    len = height - 2;
                }
                if (words[j].equals("up"))
                {
                    x = 1;
                    y = height - 1;
                    len = width - 2;
                    dir = Crusher.DIR_UP;
                }
                if (words[j].equals("down"))
                {
                    x = 1;
                    y = 0;
                    len = width - 2;
                    dir = Crusher.DIR_DOWN;
                }
                if (words[j].equals("half"))
                {
                    len /= 2;
                }
                if (words[j].equals("xplus"))
                {
                    x += width / 2 - 1;
                }
                if (words[j].equals("yplus"))
                {
                    y += height / 2;
                }
                if (words[j].equals("fast"))
                {
                    speed /= 2;
                }
                if (words[j].equals("slow"))
                {
                    speed *= 2;
                }
            }
            addEntity(new Crusher(x, y, len, dir, speed));
        }

        addEntity(player);
    }

    private char charAt(int x, int y)
    {
        x -= 1;
        y -= 1;
        if (x < 0 || y < 0 || x >= 18 || y >= 13) return ' ';
        return levelData[levelNum * 3 + 2].charAt(x + y * 18);
    }

    public String getInfoString()
    {
        return levelData[levelNum * 3];
    }

    public Tile getTileAtPixel(int x, int y)
    {
        return getTile(x / 16, y / 16);
    }

    public Tile getTile(int xt, int yt)
    {
        if (xt < 0) xt = 0;
        if (yt < 0) yt = 0;
        if (xt >= width) xt = width - 1;
        if (yt >= height) yt = height - 1;
        return tiles[xt][yt];
    }

    public void tick()
    {
        levelTick++;
        if (pauseTicks > 0)
        {
            pauseTicks--;
            return;
        }
        if (won)
        {
            if (++winTime >= BunnyWall.TICKS_PER_SECOND) gameScreen.levelWon();
        }
        else if (player.shouldRemove() && ++dieTime >= BunnyWall.TICKS_PER_SECOND)
        {
            gameScreen.levelLost();
        }
        for (int i = 0; i < entities.size(); i++)
        {
            Entity e = entities.get(i);
            e.tick();
            if (e.shouldRemove())
            {
                entities.remove(i--);
                blockMap.remove(e);
            }
        }
    }

    public void render(Graphics g)
    {
        g.drawImage(background, 0, 0, null);
        Collections.sort(entities);
        for (int i = 0; i < entities.size(); i++)
            entities.get(i).render(g);

        if (pauseTicks > 0)
        {
            g.setColor(Color.WHITE);
            for (int i = 0; i < 10; i++)
            {
                double rot = pauseTicks / 10.0 + i * Math.PI * 2 / 10;
                double dist = (pauseTicks * pauseTicks) / 60 + 8;
                int xa = (int) (Math.sin(rot) * dist);
                int ya = (int) (Math.cos(rot) * dist);
                g.drawLine(player.x + xa, player.y + ya - 16, player.x + xa * 2, player.y + ya * 2 - 16);
            }
        }
    }

    public void addEntity(Entity entity)
    {
        entities.add(entity);
        entity.init(this);
        blockMap.insert(entity);
    }

    public List<Entity> getEntities(Entity source, int x0, int y0, int x1, int y1)
    {
        List<Entity> result = blockMap.getEntities(source, x0, y0, x1, y1);
        result.remove(source);
        /*        List<Entity> result = new ArrayList<Entity>();
                for (int i = 0; i < entities.size(); i++)
                {
                    Entity e = entities.get(i);
                    if (e == source) continue;
                    if (e.intersects(x0, y0, x1, y1)) result.add(e);
                }*/
        return result;
    }

    public void taint(int xx, int yy, int color, int r)
    {
        if (!getTileAtPixel(xx, yy).passable) return;
        Graphics g = background.getGraphics();
        g.setColor(new java.awt.Color(color, true));
        g.fillRect(xx - r / 2, yy - r / 2, r, r);
        g.dispose();
    }

    public Player getPlayer()
    {
        if (player == null || player.shouldRemove()) return null;
        return player;
    }

    public void renderTile(Tile tile, int xImage, int yImage)
    {
        Graphics g = background.getGraphics();
        g.drawImage(tileSheet[xImage][yImage], tile.x * 16, tile.y * 16, null);
        g.dispose();
    }

    public void win()
    {
        won = true;
        //        gameScreen.levelWon();
    }
}