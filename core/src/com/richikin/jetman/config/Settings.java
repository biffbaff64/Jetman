package com.richikin.jetman.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.richikin.jetman.audio.AudioData;
import com.richikin.jetman.core.App;
import com.richikin.utilslib.core.ISettings;
import com.richikin.utilslib.Developer;
import com.richikin.utilslib.logging.Stats;
import com.richikin.utilslib.logging.Trace;

public class Settings implements ISettings
{
    //
    // Defaults
    public static final boolean _PREF_FALSE_DEFAULT = false;
    public static final boolean _PREF_TRUE_DEFAULT  = true;

    public static final String _DEFAULT_ON  = "default on";
    public static final String _DEFAULT_OFF = "default off";

    //
    // Development options
    public static final String _USING_ASHLEY_ECS    = "ashley ecs";         // Enables use of Ashley Entity Component System
    public static final String _SCROLL_DEMO         = "scroll demo";        // Enables Game Scroll Demo mode
    public static final String _SPRITE_BOXES        = "sprite boxes";       // Shows sprite AABB Boxes
    public static final String _TILE_BOXES          = "tile boxes";         // Shows game tile AABB Boxes
    public static final String _BUTTON_BOXES        = "button boxes";       // Shows GameButton bounding boxes
    public static final String _SHOW_FPS            = "show fps";           // Shows current FPS on-screen
    public static final String _SHOW_DEBUG          = "show debug";         // Enables on-screen debug printing
    public static final String _SPAWNPOINTS         = "spawn points";       // Shows spawn point tiles from game map
    public static final String _MENU_HEAPS          = "menu heaps";         // Show Heap Sizes on Menu Page if true
    public static final String _DISABLE_MENU_SCREEN = "disable menu";       //
    public static final String _CULL_SPRITES        = "cull sprites";       // Enables Sprite Culling when off screen
    public static final String _SHADER_PROGRAM      = "shader program";     // Enables/Disables global shader program
    public static final String _BOX2D_PHYSICS       = "using box2d";        // Enables Box2D Physics
    public static final String _B2D_RENDERER        = "b2d renderer";       // Enables/Disables the Box2D Debug Renderer
    public static final String _GL_PROFILER         = "gl profiler";        // Enables/Disables the LibGdx OpenGL Profiler
    public static final String _ANDROID_ON_DESKTOP  = "android desktop";    //
    public static final String _SHOW_HINTS          = "show hints";         // Enables/Disables In-Game Hints

    //
    // Game settings
    public static final String _INSTALLED      = "installed";          //
    public static final String _VIBRATIONS     = "vibrations";         // Enables/Disables device vibrations
    public static final String _MUSIC_ENABLED  = "music enabled";      // Enables/Disables Music
    public static final String _SOUNDS_ENABLED = "sound enabled";      // Enables/Disables Sound FX
    public static final String _FX_VOLUME      = "fx volume";          // Current Sound FX Volume
    public static final String _MUSIC_VOLUME   = "music volume";       // Current Music Volume
    public static final String _PLAY_SERVICES  = "play services";      // Enables Google Play Services
    public static final String _ACHIEVEMENTS   = "achievements";       // Enables In-Game Achievements
    public static final String _CHALLENGES     = "challenges";         // Enables In-Game challenges
    public static final String _SIGN_IN_STATUS = "sign in status";     // Google Services sign in status (Android)

    public Preferences prefs;

    public Settings()
    {
        Trace.__FILE_FUNC();
    }

    @SuppressWarnings("FeatureEnvy")
    public void initialise()
    {
        Trace.__FILE_FUNC();

        try
        {
            prefs = Gdx.app.getPreferences(AppConfig._PREFS_FILE_NAME);
        }
        catch (Exception e)
        {
            Trace.__FILE_FUNC();
            Trace.dbg(e.getMessage());

            prefs = null;
        }
    }

    public void freshInstallCheck()
    {
        if (!isEnabled(Settings._INSTALLED))
        {
            Trace.dbg("FRESH INSTALL.");

            resetToDefaults();

            Trace.dbg("Setting all Statistical logging meters to zero.");
            Stats.resetAllMeters();

            enable(Settings._INSTALLED);
        }
    }

    @Override
    public Preferences getPrefs()
    {
        return prefs;
    }

    @Override
    public boolean isEnabled(final String preference)
    {
        return (prefs != null) && prefs.getBoolean(preference);
    }

    @Override
    public void enable(final String preference)
    {
        if (prefs != null)
        {
            prefs.putBoolean(preference, true);
            prefs.flush();
        }
    }

    @Override
    public void disable(final String preference)
    {
        if (prefs != null)
        {
            prefs.putBoolean(preference, false);
            prefs.flush();
        }
    }

    @Override
    public void resetToDefaults()
    {
        if (prefs != null)
        {
            Trace.dbg("Initialising all App settings to default values.");

            prefs.putBoolean(_DEFAULT_ON, _PREF_TRUE_DEFAULT);
            prefs.putBoolean(_DEFAULT_OFF, _PREF_FALSE_DEFAULT);

            // ---------- Development Flags ----------
            prefs.putBoolean(_DISABLE_MENU_SCREEN, _PREF_FALSE_DEFAULT);
            prefs.putBoolean(_SCROLL_DEMO, _PREF_FALSE_DEFAULT);
            prefs.putBoolean(_SPRITE_BOXES, _PREF_FALSE_DEFAULT);
            prefs.putBoolean(_TILE_BOXES, _PREF_FALSE_DEFAULT);
            prefs.putBoolean(_BUTTON_BOXES, _PREF_FALSE_DEFAULT);
            prefs.putBoolean(_SHOW_FPS, _PREF_FALSE_DEFAULT);
            prefs.putBoolean(_SHOW_DEBUG, _PREF_FALSE_DEFAULT);
            prefs.putBoolean(_SPAWNPOINTS, _PREF_FALSE_DEFAULT);
            prefs.putBoolean(_MENU_HEAPS, _PREF_FALSE_DEFAULT);
            prefs.putBoolean(_CULL_SPRITES, _PREF_TRUE_DEFAULT);
            prefs.putBoolean(_B2D_RENDERER, _PREF_FALSE_DEFAULT);
            prefs.putBoolean(_GL_PROFILER, _PREF_FALSE_DEFAULT);
            prefs.putBoolean(_ANDROID_ON_DESKTOP, _PREF_FALSE_DEFAULT);

            // ---------- Configuration ----------
            prefs.putBoolean(_SHADER_PROGRAM, _PREF_FALSE_DEFAULT);
            prefs.putBoolean(_USING_ASHLEY_ECS, _PREF_FALSE_DEFAULT);
            prefs.putBoolean(_BOX2D_PHYSICS, _PREF_TRUE_DEFAULT);
            prefs.putBoolean(_INSTALLED, _PREF_FALSE_DEFAULT);
            prefs.putBoolean(_SHOW_HINTS, _PREF_FALSE_DEFAULT);
            prefs.putBoolean(_VIBRATIONS, _PREF_TRUE_DEFAULT);
            prefs.putBoolean(_MUSIC_ENABLED, _PREF_TRUE_DEFAULT);
            prefs.putBoolean(_SOUNDS_ENABLED, _PREF_TRUE_DEFAULT);
            prefs.putInteger(_FX_VOLUME, AudioData._DEFAULT_FX_VOLUME);
            prefs.putInteger(_MUSIC_VOLUME, AudioData._DEFAULT_MUSIC_VOLUME);

            // ---------- Google Services ----------
            prefs.putBoolean(_PLAY_SERVICES, _PREF_FALSE_DEFAULT);
            prefs.putBoolean(_ACHIEVEMENTS, _PREF_FALSE_DEFAULT);
            prefs.putBoolean(_CHALLENGES, _PREF_FALSE_DEFAULT);
            prefs.putBoolean(_SIGN_IN_STATUS, _PREF_FALSE_DEFAULT);

            prefs.flush();
        }
    }
}
