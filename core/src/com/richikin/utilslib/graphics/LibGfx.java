package com.richikin.utilslib.graphics;

import com.badlogic.gdx.math.Vector2;

public class LibGfx
{
    //
    // Pixels Per Meter in the Box2D World
    // In this instance, a meter is the length of a single TiledMap tile.
    public static float _PPM       = 32.0f;
    public static float _PPM_RATIO = (1.0f / _PPM);

    //
    // Maximum Z-sorting depth for sprites
    public static int _MAXIMUM_Z_DEPTH = 20;

    //
    // The desired Frame Rate
    public static final float _FPS     = 60f;
    public static final float _MIN_FPS = 30f;

    //
    // Values for Box2D.step()
    public static final float   _STEP_TIME           = (1.0f / 60f);
    public static final int     _VELOCITY_ITERATIONS = 8;
    public static final int     _POSITION_ITERATIONS = 3;
    public static final Vector2 _WORLD_GRAVITY       = new Vector2(0, -9.8f);
    public static final int     _FALL_GRAVITY        = 10;
    public static final int     _TERMINAL_VELOCITY   = (int) (_PPM * _FALL_GRAVITY);

    //
    // Initialised in MapData
    public static int mapWidth;
    public static int mapHeight;
    public static int tileWidth;
    public static int tileHeight;

    public static int getTileWidth()
    {
        return tileWidth;
    }

    public static int getTileHeight()
    {
        return tileHeight;
    }

    public static int getMapWidth()
    {
        return mapWidth;
    }

    public static int getMapHeight()
    {
        return mapHeight;
    }
}
