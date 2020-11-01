package com.richikin.jetman.config;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.richikin.jetman.core.App;
import com.richikin.utilslib.states.StateID;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.utilslib.input.ControllerPos;
import com.richikin.utilslib.input.ControllerType;
import com.richikin.enumslib.ScreenID;
import com.richikin.utilslib.developer.Developer;
import com.richikin.utilslib.logging.Stats;
import com.richikin.utilslib.logging.Trace;

public class AppConfig extends com.richikin.utilslib.config.AppConfig
{
    // =================================================================
    //
    public static final String _PREFS_FILE_NAME = "com.richikin.jetman.preferences";

    public static boolean               quitToMainMenu;             // Game over, back to menu screen
    public static boolean               forceQuitToMenu;            // Quit to main menu, forced via pause mode for example.
    public static boolean               gamePaused;                 // TRUE / FALSE Game Paused flag
    public static boolean               camerasReady;               // TRUE when all cameras have been created.
    public static boolean               shutDownActive;             // TRUE if game is currently processing EXIT request.
    public static boolean               entitiesExist;              // Set true when all entities have been created
    public static boolean               hudExists;                  // Set true when HUD has finished setup
    public static boolean               controllersFitted;          // TRUE if external controllers are fitted/connected.
    public static boolean               gameButtonsReady;           // TRUE When all game buttons have been defined
    public static ScreenID              currentScreenID;            // ID of the currently active screeen
    public static String                usedController;             // The name of the controller being used
    public static ControllerPos         virtualControllerPos;       // Virtual (on-screen) joystick position (LEFT or RIGHT)
    public static Array<ControllerType> availableInputs;            // ...

    // =================================================================
    //

    private static App app;

    public static void setup(App _app)
    {
        Trace.__FILE_FUNC();

        app = _app;

        quitToMainMenu    = false;
        forceQuitToMenu   = false;
        gamePaused        = false;
        camerasReady      = false;
        shutDownActive    = false;
        entitiesExist     = false;
        hudExists         = false;
        controllersFitted = false;
        gameButtonsReady  = false;
        usedController    = "None";

        Developer.setMode(_app);

        app.settings.enable(Settings._ANDROID_ON_DESKTOP);

        availableInputs = new Array<>();

        if (isAndroidApp() || Developer.isAndroidOnDesktop())
        {
            availableInputs.add(ControllerType._VIRTUAL);

            virtualControllerPos = ControllerPos._LEFT;
        }
        else
        {
            availableInputs.add(ControllerType._EXTERNAL);
            availableInputs.add(ControllerType._KEYBOARD);

            virtualControllerPos = ControllerPos._HIDDEN;
        }

        if (Developer.isDevMode())
        {
            app.settings.enable(Settings._GOD_MODE);
            app.settings.enable(Settings._DISABLE_MENU_SCREEN);
            app.settings.disable(Settings._SPRITE_BOXES);
            app.settings.disable(Settings._TILE_BOXES);
        }

        Stats.setup();

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

        if (!app.settings.isEnabled(Settings._INSTALLED))
        {
            Trace.dbg("FRESH INSTALL.");

            Trace.dbg("Initialising all App settings to default values.");
            app.settings.resetToDefaults();

            Trace.dbg("Setting all Statistical logging meters to zero.");
            Stats.resetAllMeters();

            app.settings.enable(Settings._INSTALLED);
        }
    }

    /**
     * Pause the game
     */
    public static void pause()
    {
        app.appState.set(StateID._STATE_PAUSED);
        app.getHud().hudStateID = StateID._STATE_PAUSED;
        gamePaused              = true;
    }

    /**
     * Un-pause the game
     */
    public static void unPause()
    {
        app.appState.set(StateID._STATE_GAME);
        app.getHud().hudStateID = StateID._STATE_PANEL_UPDATE;
        gamePaused              = false;
    }

    /**
     * @return TRUE if the app is running on Desktop
     */
    public static boolean isDesktopApp()
    {
        return (Gdx.app.getType() == Application.ApplicationType.Desktop);
    }

    /**
     * @return TRUE if the app is running on Android
     */
    public static boolean isAndroidApp()
    {
        return (Gdx.app.getType() == Application.ApplicationType.Android);
    }

    public static void dispose()
    {
        availableInputs.clear();

        usedController       = null;
        availableInputs      = null;
        virtualControllerPos = null;
    }
}
