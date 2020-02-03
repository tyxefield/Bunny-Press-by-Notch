package com.mojang.bunnywall;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class BunnyWall extends Canvas implements Runnable, MouseListener
{
    private static final long serialVersionUID = -5818397486183413269L;

    public static final int GAME_WIDTH = 320;
    public static final int GAME_HEIGHT = 240;
    public static final int SCALE = 2;
    public static final int TICKS_PER_SECOND = 60;

    private Thread thread;
    private boolean running;
    private long lastTime;

    private BufferedImage offscreenImage = new BufferedImage(GAME_WIDTH, GAME_HEIGHT, BufferedImage.TYPE_INT_RGB);
    private Graphics offscreenGraphics = offscreenImage.getGraphics();
    public Controller controller = new Controller();
    private Screen screen;
    
    public BunnyWall()
    {
        int w = GAME_WIDTH * SCALE;
        int h = GAME_HEIGHT * SCALE;
        setPreferredSize(new Dimension(w, h));
        setMaximumSize(new Dimension(w, h));
        setMinimumSize(new Dimension(w, h));
        setBackground(Color.BLACK);

        addKeyListener(controller);
        addFocusListener(controller);
        addMouseListener(this);
        this.setFocusable(true);
        setScreen(new IntroScreen());
    }

    public void paint(Graphics g)
    {
    }

    public void update(Graphics g)
    {
    }

    public synchronized void start()
    {
        running = true;
        lastTime = System.nanoTime();

        thread = new Thread(this);
        thread.start();

        requestFocus();
    }

    public synchronized void stop()
    {
        running = false;
    }

    public void run()
    {
        Sounds.touch();
        long unprocessedTime = 0;
        long nsPerTick = 1000000000 / TICKS_PER_SECOND;
        int frames = 0;
        long lastFpsTime = System.currentTimeMillis();

        while (running)
        {
            long now = System.nanoTime();
            long passedTime = now - lastTime;
            if (passedTime > 0)
            {
                unprocessedTime += passedTime;
                while (unprocessedTime >= nsPerTick)
                {
                    unprocessedTime -= nsPerTick;
                    tick();
                }
                lastTime = now;
            }

            render(offscreenGraphics);
            frames++;
            
            if (System.currentTimeMillis()-lastFpsTime>1000)
            {
                lastFpsTime+=1000;
                System.out.println(frames+" fps");
                frames = 0;
            }

            Graphics g = getGraphics();
            g.drawImage(offscreenImage, 0, 0, GAME_WIDTH * SCALE, GAME_HEIGHT * SCALE, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
            g.dispose();

            try
            {
                Thread.sleep(2);
            }
            catch (InterruptedException e)
            {
            }
        }
    }
    
    public void setScreen(Screen screen)
    {
        this.screen = screen;
        screen.init(this);
    }

    public void tick()
    {
        controller.tick();
        screen.tick();
    }

    public void render(Graphics g)
    {
        screen.render(g);
    }

    public static void main(String[] args)
    {
        BunnyWall bunnyWall = new BunnyWall();

        JFrame frame = new JFrame("BunnyWall");
        frame.add(bunnyWall);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        bunnyWall.start();
    }

    public void mouseClicked(MouseEvent arg0)
    {
    }

    public void mouseEntered(MouseEvent arg0)
    {
    }

    public void mouseExited(MouseEvent arg0)
    {
    }

    public void mousePressed(MouseEvent arg0)
    {
        screen.clicked = true;
    }

    public void mouseReleased(MouseEvent arg0)
    {
    }
}