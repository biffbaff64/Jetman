package com.richikin.jetman.config;

import com.badlogic.gdx.utils.Array;
import com.richikin.jetman.core.App;
import com.richikin.utilslib.config.LibAppConfig;
import com.richikin.utilslib.core.AppSystem;
import com.richikin.utilslib.states.StateID;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.utilslib.input.controllers.ControllerPos;
import com.richikin.utilslib.input.controllers.ControllerType;
import com.richikin.enumslib.ScreenID;
import com.richikin.utilslib.developer.Developer;
import com.richikin.utilslib.logging.Stats;
import com.richikin.utilslib.logging.Trace;

public class AppConfig extends LibAppConfig
{
    // =================================================================
    //
    public static final String _PREFS_FILE_NAME = "com.richikin.jetman.preferences";

    // =================================================================
    //

    public static void setup()
    {
        Trace.__FILE_FUNC();

        App.settings.enable(Settings._ANDROID_ON_DESKTOP);

        if (Developer.isDevMode())
        {
            App.settings.enable(Settings._GOD_MODE);
            App.settings.disable(Settings._DISABLE_MENU_SCREEN);
            App.settings.disable(Settings._SPRITE_BOXES);
            App.settings.disable(Settings._TILE_BOXES);
        }

        if (Developer.isDevMode())
        {
            Trace.divider();
            Trace.dbg("Android App         : " + isAndroidApp());
            Trace.dbg("Desktop App         : " + isDesktopApp());
            Trace.dbg("Android On Desktop  : " + Developer.isAndroidOnDesktop());
            Trace.divider();
            Trace.dbg("isDevMode()         : " + Developer.isDevMode());
            Trace.dbg("isGodMode()         : " + Developer.isGodMode());
            Trace.divider();
            Trace.dbg("_DESKTOP_WIDTH      : " + Gfx._DESKTOP_WIDTH);
            Trace.dbg("_DESKTOP_HEIGHT     : " + Gfx._DESKTOP_HEIGHT);
            Trace.dbg("_VIEW_WIDTH         : " + Gfx._VIEW_WIDTH);
            Trace.dbg("_VIEW_HEIGHT        : " + Gfx._VIEW_HEIGHT);
            Trace.dbg("_HUD_WIDTH          : " + Gfx._HUD_WIDTH);
            Trace.dbg("_HUD_HEIGHT         : " + Gfx._HUD_HEIGHT);
            Trace.divider();
            Trace.dbg("_VIRTUAL?           : " + availableInputs.contains(ControllerType._VIRTUAL, true));
            Trace.dbg("_EXTERNAL?          : " + availableInputs.contains(ControllerType._EXTERNAL, true));
            Trace.dbg("_KEYBOARD?          : " + availableInputs.contains(ControllerType._KEYBOARD, true));
            Trace.dbg("controllerPos       : " + virtualControllerPos);
            Trace.dbg("controllersFitted   : " + controllersFitted);
            Trace.dbg("usedController      : " + usedController);
            Trace.divider();
        }
    }

    public static boolean gameScreenActive()
    {
        return currentScreenID == ScreenID._GAME_SCREEN;
    }

    public static void freshInstallCheck()
    {
        Trace.__FILE_FUNC();

        if (!App.settings.isEnabled(Settings._INSTALLED))
        {
            Trace.dbg("FRESH INSTALL.");

            Trace.dbg("Initialising all App settings to default values.");
            App.settings.resetToDefaults();

            Trace.dbg("Setting all Statistical logging meters to zero.");
            Stats.resetAllMeters();

            App.settings.enable(Settings._INSTALLED);
        }
    }

    /**
     * Pause the game
     */
    public static void pause()
    {
        App.appState.set(StateID._STATE_PAUSED);
        App.getHud().hudStateID = StateID._STATE_PAUSED;
        gamePaused              = true;
    }

    /**
     * Un-pause the game
     */
    public static void unPause()
    {
        App.appState.set(StateID._STATE_GAME);
        App.getHud().hudStateID = StateID._STATE_PANEL_UPDATE;
        gamePaused              = false;
    }
}
