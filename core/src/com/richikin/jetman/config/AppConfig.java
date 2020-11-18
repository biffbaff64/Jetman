package com.richikin.jetman.config;

import com.richikin.jetman.core.App;
import com.richikin.utilslib.AppSystem;
import com.richikin.enumslib.StateID;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.utilslib.input.controllers.ControllerType;
import com.richikin.enumslib.ScreenID;
import com.richikin.utilslib.Developer;
import com.richikin.utilslib.logging.Stats;
import com.richikin.utilslib.logging.Trace;

public class AppConfig
{
    public static final String _PREFS_FILE_NAME = "com.richikin.jetman.preferences";

    private AppConfig() {}

    public static void setup()
    {
        Trace.__FILE_FUNC();

        if (App.settings.getPrefs() == null)
        {
            App.settings.createPreferencesObject();
        }

        Developer.setMode();

        // ------------------------------------------------
        // Temporary development settings
        Developer.setAndroidOnDesktop(true);
        Developer.setGodMode(true);

        App.settings.disable(Settings._DISABLE_MENU_SCREEN);
        App.settings.disable(Settings._SPRITE_BOXES);
        App.settings.disable(Settings._TILE_BOXES);
        // ------------------------------------------------

        AppSystem.initialise();

        if (Developer.isDevMode())
        {
            Trace.divider();
            Trace.dbg("Android App         : " + AppSystem.isAndroidApp());
            Trace.dbg("Desktop App         : " + AppSystem.isDesktopApp());
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
            Trace.dbg("_VIRTUAL?           : " + AppSystem.availableInputs.contains(ControllerType._VIRTUAL, true));
            Trace.dbg("_EXTERNAL?          : " + AppSystem.availableInputs.contains(ControllerType._EXTERNAL, true));
            Trace.dbg("_KEYBOARD?          : " + AppSystem.availableInputs.contains(ControllerType._KEYBOARD, true));
            Trace.dbg("controllerPos       : " + AppSystem.virtualControllerPos);
            Trace.dbg("controllersFitted   : " + AppSystem.controllersFitted);
            Trace.dbg("usedController      : " + AppSystem.usedController);
            Trace.divider();
        }
    }

    public static boolean gameScreenActive()
    {
        return AppSystem.currentScreenID == ScreenID._GAME_SCREEN;
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
        AppSystem.gamePaused    = true;
    }

    /**
     * Un-pause the game
     */
    public static void unPause()
    {
        App.appState.set(StateID._STATE_GAME);
        App.getHud().hudStateID = StateID._STATE_PANEL_UPDATE;
        AppSystem.gamePaused    = false;
    }
}
