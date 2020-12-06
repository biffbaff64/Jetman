package com.richikin.jetman.config;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.richikin.jetman.core.App;
import com.richikin.enumslib.StateID;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.utilslib.input.GameButtonRegion;
import com.richikin.utilslib.input.Switch;
import com.richikin.utilslib.input.controllers.ControllerPos;
import com.richikin.utilslib.input.controllers.ControllerType;
import com.richikin.enumslib.ScreenID;
import com.richikin.jetman.developer.Developer;
import com.richikin.utilslib.logging.Stats;
import com.richikin.utilslib.logging.Trace;

public class AppConfig
{
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
    public static GameButtonRegion      fullScreenButton;           // ...
    public static Switch                systemBackButton;           // ...
    public static ImageButton           backButton;                 // ...

    public static float hudOriginX;
    public static float hudOriginY;

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
        Developer.setGodMode(false);

        App.settings.enable(Settings._DISABLE_MENU_SCREEN);
        App.settings.disable(Settings._SCROLL_DEMO);
        App.settings.disable(Settings._SPRITE_BOXES);
        App.settings.disable(Settings._TILE_BOXES);
        // ------------------------------------------------

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

        Stats.setup();

        hudOriginX = 0;
        hudOriginY = 0;

        fullScreenButton = new GameButtonRegion(0, 0, Gfx._HUD_WIDTH, Gfx._HUD_HEIGHT);
        systemBackButton = new Switch();

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

    public static void addBackButton(String _default, String _pressed)
    {
        // TODO: 11/11/2020 - Use Scene2DUtils instead
        Image imageUp   = new Image(App.assets.getButtonRegion(_default));
        Image imageDown = new Image(App.assets.getButtonRegion(_pressed));
        backButton = new ImageButton(imageUp.getDrawable(), imageDown.getDrawable());
        backButton.setPosition(0, 0);
        backButton.setVisible(false);
        App.stage.addActor(backButton);

        backButton.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                backButton.setChecked(true);
            }
        });
    }

    public static void setBackButtonState(boolean _visible, boolean _enabled)
    {
        backButton.setVisible(_visible);
        backButton.setDisabled(!_enabled);
    }

    public static void showAndEnableBackButton()
    {
        backButton.setVisible(true);
        backButton.setDisabled(false);
    }

    public static void hideAndDisableBackButton()
    {
        backButton.setVisible(false);
        backButton.setDisabled(true);
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
        gamePaused    = true;

        if (App.getHud().hudStateID != StateID._STATE_SETTINGS_PANEL)
        {
            App.getHud().hudStateID = StateID._STATE_PAUSED;
        }
    }

    /**
     * Un-pause the game
     */
    public static void unPause()
    {
        App.appState.set(StateID._STATE_GAME);
        gamePaused    = false;
        App.getHud().hudStateID = StateID._STATE_PANEL_UPDATE;
    }

    public static void dispose()
    {
        availableInputs.clear();

        usedController       = null;
        availableInputs      = null;
        virtualControllerPos = null;
    }
}
