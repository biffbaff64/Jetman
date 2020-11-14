package com.richikin.jetman.audio;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public abstract class AudioData
{
    /**
     * Standard Audio properties.
     */
    public static final int _SILENT                 = 0;
    public static final int _MIN_VOLUME             = 0;
    public static final int _MAX_VOLUME             = 10;
    public static final int _VOLUME_INCREMENT       = 1;
    public static final int _VOLUME_MULTIPLIER      = 10;
    public static final int _DEFAULT_MUSIC_VOLUME   = 4;
    public static final int _DEFAULT_FX_VOLUME      = 6;

    public static final int SFX_LASER             = 0;
    public static final int SFX_PLAZMA            = 1;
    public static final int SFX_EXPLOSION_1       = 2;
    public static final int SFX_EXPLOSION_2       = 3;
    public static final int SFX_EXPLOSION_3       = 4;
    public static final int SFX_EXPLOSION_4       = 5;
    public static final int SFX_EXPLOSION_5       = 6;
    public static final int SFX_THRUST            = 7;
    public static final int SFX_PICKUP            = 8;
    public static final int SFX_TELEPORT          = 9;
    public static final int SFX_EXTRA_LIFE        = 10;
    public static final int SFX_LAUNCH_WARNING    = 11;
    public static final int SFX_TEST_SOUND        = 12;
    public static final int SFX_BEEP              = 13;
    public static final int MAX_SOUND             = 14;

    public static final int MUS_TITLE             = 0;
    public static final int MUS_HISCORE           = 1;
    public static final int MUS_GAME              = 2;
    public static final int MAX_TUNES             = 3;

    public static Sound[] sounds;
    public static Music[] music;
}
