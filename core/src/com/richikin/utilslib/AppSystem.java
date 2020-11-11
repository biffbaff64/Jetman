package com.richikin.utilslib;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.richikin.enumslib.ScreenID;
import com.richikin.jetman.core.App;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.ui.Scene2DUtils;
import com.richikin.utilslib.Developer;
import com.richikin.utilslib.input.GameButtonRegion;
import com.richikin.utilslib.input.Switch;
import com.richikin.utilslib.input.controllers.ControllerPos;
import com.richikin.utilslib.input.controllers.ControllerType;
import com.richikin.utilslib.logging.Stats;

// TODO: 05/11/2020
@SuppressWarnings("UtilityClassCanBeEnum")
public final class AppSystem
{
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

    private AppSystem()
    {
    }

    public static void initialise()
    {
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
    }

    public static void addBackButton()
    {
        // TODO: 11/11/2020 - Use Scene2DUtils instead when its has been moved to UtilsLib
        Image imageUp   = new Image(App.assets.getButtonRegion("new_back_button"));
        Image imageDown = new Image(App.assets.getButtonRegion("new_back_button_pressed"));
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
