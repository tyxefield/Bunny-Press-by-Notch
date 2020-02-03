package com.mojang.bunnywall;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public class Controller implements KeyListener, FocusListener
{
    public static final int BUTTON_UP = 0;
    public static final int BUTTON_LEFT = 1;
    public static final int BUTTON_RIGHT = 2;
    public static final int BUTTON_DOWN = 3;
    public static final int BUTTON_GRAB = 4;
    
    private boolean[] keys = new boolean[256];
    public boolean[] buttons = new boolean[16];

    private Map<Integer, Integer> buttonMap = new HashMap<Integer, Integer>();

    public Controller()
    {
        bind(KeyEvent.VK_UP, BUTTON_UP);
        bind(KeyEvent.VK_LEFT, BUTTON_LEFT);
        bind(KeyEvent.VK_RIGHT, BUTTON_RIGHT);
        bind(KeyEvent.VK_DOWN, BUTTON_DOWN);
        bind(KeyEvent.VK_NUMPAD8, BUTTON_UP);
        bind(KeyEvent.VK_NUMPAD4, BUTTON_LEFT);
        bind(KeyEvent.VK_NUMPAD6, BUTTON_RIGHT);
        bind(KeyEvent.VK_NUMPAD2, BUTTON_DOWN);
        bind(KeyEvent.VK_W, BUTTON_UP);
        bind(KeyEvent.VK_A, BUTTON_LEFT);
        bind(KeyEvent.VK_D, BUTTON_RIGHT);
        bind(KeyEvent.VK_S, BUTTON_DOWN);
        bind(KeyEvent.VK_SPACE, BUTTON_GRAB);
        bind(KeyEvent.VK_CONTROL, BUTTON_GRAB);
        bind(KeyEvent.VK_ALT, BUTTON_GRAB);
        bind(KeyEvent.VK_ALT_GRAPH, BUTTON_GRAB);
        bind(KeyEvent.VK_SHIFT, BUTTON_GRAB);
        bind(KeyEvent.VK_ENTER, BUTTON_GRAB);
    }

    public void bind(int keyCode, int button)
    {
        if (keyCode >= 0 && keyCode < keys.length) buttonMap.put(keyCode, button);
    }

    public void tick()
    {
        for (int i = 0; i < buttons.length; i++)
        {
            buttons[i] = false;
        }

        for (Integer key : buttonMap.keySet())
        {
            int button = buttonMap.get(key);
            if (keys[key]) buttons[button] = true;
        }
    }

    public void releaseAllKeys()
    {
        for (int i = 0; i < keys.length; i++)
        {
            keys[i] = false;
        }
    }

    public void keyPressed(KeyEvent ke)
    {
        int keyCode = ke.getKeyCode();
        if (keyCode >= 0 && keyCode < keys.length) keys[keyCode] = true;
    }

    public void keyReleased(KeyEvent ke)
    {
        int keyCode = ke.getKeyCode();
        if (keyCode >= 0 && keyCode < keys.length) keys[keyCode] = false;
    }

    public void keyTyped(KeyEvent ke)
    {
    }

    public void focusGained(FocusEvent fe)
    {
    }

    public void focusLost(FocusEvent fe)
    {
        releaseAllKeys();
    }
}