package com.richikin.jetman.graphics;

import com.badlogic.gdx.math.Vector2;
import com.richikin.jetman.config.AppConfig;

public class Gfx
{
    //
    // Entity collision types
    public static final short CAT_NOTHING       = 0x0000;   // - 00 (0     )
    public static final short CAT_PLAYER        = 0x0001;   // - 01 (1     )
    public static final short CAT_MOBILE_ENEMY  = 0x0002;   // - 02 (2     )
    public static final short CAT_FIXED_ENEMY   = 0x0004;   // - 03 (4     )
    public static final short CAT_CEILING       = 0x0008;   // - 04 (8     )
    public static final short CAT_GROUND        = 0x0010;   // - 05 (16    )
    public static final short CAT_SCENERY       = 0x0020;   // - 06 (32    )
    public static final short CAT_VEHICLE       = 0x0040;   // - 07 (64    )
    public static final short CAT_PLAYER_WEAPON = 0x0080;   // - 08 (128   )
    public static final short CAT_ENEMY_WEAPON  = 0x0100;   // - 09 (256   )
    public static final short CAT_MISSILE_BASE  = 0x0200;   // - 10 (512   )
    public static final short CAT_TELEPORTER    = 0x0400;   // - 11 (1024  )
    public static final short _UNDEFINED_12     = 0x0800;   // - 12 (2048  )
    public static final short _UNDEFINED_13     = 0x1000;   // - 13 (4096  )
    public static final short _UNDEFINED_14     = 0x2000;   // - 14 (8192  )
    public static final short _UNDEFINED_15     = 0x4000;   // - 15 (16384 )

    //
    // Pixels Per Meter in the Box2D World
    // In this instance, a meter is the length of a single TiledMap tile.
    public static float _PPM       = 32.0f;
    public static float _PPM_RATIO = (1.0f / _PPM);

    //
    // Maximum Z-sorting depth for sprites
    public static final int _MAXIMUM_Z_DEPTH = 20;

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

    public static final int _HUD_WIDTH      = 1280;
    public static final int _HUD_HEIGHT     = 720;
    public static final int _DESKTOP_WIDTH  = 1280;
    public static final int _DESKTOP_HEIGHT = 720;
    public static final int _VIEW_WIDTH     = 1600;
    public static final int _VIEW_HEIGHT    = 900;

    public static final int _VIEW_HALF_WIDTH  = (_VIEW_WIDTH / 2);
    public static final int _VIEW_HALF_HEIGHT = (_VIEW_HEIGHT / 2);

    public static final float _DEFAULT_ZOOM = 1.0f;
    public static final float _LERP_SPEED   = 0.075f;

    public static final float _HUD_SCENE_WIDTH   = (_HUD_WIDTH / _PPM);
    public static final float _HUD_SCENE_HEIGHT  = (_HUD_HEIGHT / _PPM);
    public static       float _GAME_SCENE_WIDTH  = (_VIEW_WIDTH / _PPM);
    public static       float _GAME_SCENE_HEIGHT = (_VIEW_HEIGHT / _PPM);

    public static final float _PPM_SETTING = 32.0f;

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

    public static void setPPM(final float newPPM)
    {
        if (!AppConfig.camerasReady)
        {
            if (newPPM != _PPM)
            {
                _PPM               = newPPM;
                _PPM_RATIO         = (1.0f / _PPM);
                _GAME_SCENE_WIDTH  = (_VIEW_WIDTH / _PPM);
                _GAME_SCENE_HEIGHT = (_VIEW_HEIGHT / _PPM);
            }
        }
    }

    public static int visibleMapWidth()
    {
        return mapWidth - (_VIEW_HALF_WIDTH * 2);
    }

    public static int visibleMapRight()
    {
        return mapWidth - _VIEW_HALF_WIDTH;
    }
}
