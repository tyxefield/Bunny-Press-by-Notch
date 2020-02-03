package com.mojang.bunnywall;

import javax.sound.sampled.*;

public class Sounds
{
    public static Clip crunch = loadClip("/res/crunch.wav");
    public static Clip death = loadClip("/res/death.wav");
    public static Clip omnomnom = loadClip("/res/omnomnom.wav");
    public static Clip rockbreak = loadClip("/res/rockbreak.wav");
    public static Clip roll = loadClip("/res/roll.wav");
    public static Clip splat = loadClip("/res/splat.wav");
    public static Clip unlock = loadClip("/res/unlock.wav");
    public static Clip woohoo = loadClip("/res/woohoo.wav");
    public static Clip step1 = loadClip("/res/step1.wav");
    public static Clip step2 = loadClip("/res/step2.wav");
    
    public static void touch()
    {
    }

    private static Clip loadClip(String resourceName)
    {
        try
        {
            AudioInputStream sound = AudioSystem.getAudioInputStream(Sounds.class.getResource(resourceName));

            DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(sound);

            return clip;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void play(Clip clip, int x)
    {
        if (clip!=null)
        {
                clip.stop();
                clip.setFramePosition(0);
                clip.start();
                
//                float pan = (x-160)/160.0f;
//                ((FloatControl)clip.getControl(FloatControl.Type.BALANCE)).setValue(pan);
//                ((FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue((float)(Math.random()*0.5+0.5));
//                ((FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue((float)(Math.random()*0.5+0.5));
            

        }
    }
}