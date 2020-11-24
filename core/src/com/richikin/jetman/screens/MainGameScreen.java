package com.richikin.jetman.screens;

import com.richikin.enumslib.ScreenID;
import com.richikin.enumslib.StateID;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.config.Settings;
import com.richikin.jetman.core.App;
import com.richikin.jetman.core.EndgameManager;
import com.richikin.jetman.core.GameControlLoop;
import com.richikin.jetman.core.LevelManager;
import com.richikin.jetman.ui.GameCompletedPanel;
import com.richikin.utilslib.AppSystem;
import com.richikin.utilslib.Developer;
import com.richikin.utilslib.graphics.camera.Shake;
import com.richikin.utilslib.input.controllers.ControllerType;
import com.richikin.utilslib.logging.StopWatch;
import com.richikin.utilslib.logging.Trace;

public class MainGameScreen extends AbstractBaseScreen
{
    public GameCompletedPanel completedPanel;
    public StopWatch          retryDelay;
    public EndgameManager     endGameManager;
    public GameControlLoop    gameControlLoop;

    /*
     * boolean firstTime - TRUE if MainGameSCreen has
     * just been entered, i.e. a NEW Game.
     *
     * Setting this to true allows initialise() to
     * be called from show(), one time only. If false, then
     * initialise() will be bypassed but the rest of show()
     * will be processed.
     */
    public boolean firstTime;

    public MainGameScreen()
    {
        super();

        this.firstTime = true;
    }

    @Override
    public void initialise()
    {
        if (firstTime)
        {
            Trace.divider();
            Trace.__FILE_FUNC("NEW GAME:");
            Trace.__FILE_FUNC("_DEVMODE: " + Developer.isDevMode());
            Trace.__FILE_FUNC("_GODMODE: " + Developer.isGodMode());
            Trace.__FILE_FUNC("prefs : " + App.settings.getPrefs());
            Trace.divider();

            endGameManager   = new EndgameManager();
            gameControlLoop  = new GameControlLoop();
            App.levelManager = new LevelManager();

            App.levelManager.prepareNewGame();
            gameControlLoop.initialise();

            App.appState.set(StateID._STATE_SETUP);
        }

        if (AppSystem.availableInputs.contains(ControllerType._VIRTUAL, true))
        {
            App.inputManager.virtualJoystick.show();
        }

        Shake.setAllowed(App.settings.isEnabled(Settings._VIBRATIONS));
    }

    @Override
    public void update()
    {
        switch (App.appState.peek())
        {
            case _STATE_MAIN_MENU:
            case _STATE_CLOSING:
            {
            }
            break;

            case _STATE_SETUP:
            case _STATE_GET_READY:
            case _STATE_DEVELOPER_PANEL:
            case _STATE_PAUSED:
            case _STATE_GAME:
            case _STATE_MESSAGE_PANEL:
            case _STATE_LEVEL_RETRY:
            case _STATE_LEVEL_FINISHED:
            case _STATE_GAME_OVER:
            case _STATE_GAME_FINISHED:
            case _STATE_ANNOUNCE_MISSILE:
            case _STATE_TELEPORTING:
            case _STATE_END_GAME:
            {
                gameControlLoop.update();
            }
            break;

            default:
            {
                Trace.__FILE_FUNC();
                Trace.dbg("Unsupported game state: " + App.appState.peek());
            }
        }
    }

    /**
     * Update and Render the game, and step the physics world.
     * Called from {@link com.badlogic.gdx.Game}
     *
     * @param delta Time since the last update.
     */
    @Override
    public void render(float delta)
    {
        super.update();

        if (AppConfig.gameScreenActive())
        {
            update();

            super.render(delta);

            App.worldModel.worldStep();
        }
    }

    public void reset()
    {
        firstTime = true;

        App.gameProgress.playerGameOver = false;
        AppSystem.gamePaused            = false;
    }

    @Override
    public void show()
    {
        super.show();

        AppSystem.currentScreenID = ScreenID._GAME_SCREEN;
        App.cameraUtils.disableAllCameras();

        initialise();

        App.appState.set(StateID._STATE_SETUP);
    }

    @Override
    public void hide()
    {
        super.hide();
    }

    @Override
    public void loadImages()
    {
        App.baseRenderer.parallaxBackground.setupLayers(App.mapData.backgroundLayers);
        App.baseRenderer.parallaxForeground.setupLayers(App.mapData.foregroundLayers);
    }

    @Override
    public void dispose()
    {
        Trace.__FILE_FUNC();

        App.entityManager.dispose();
        App.getHud().dispose();
        App.gameProgress.dispose();

        App.baseRenderer.parallaxBackground.dispose();
        App.baseRenderer.parallaxForeground.dispose();

        App.baseRenderer.gameZoom.setZoomValue(0.0f);
        App.baseRenderer.hudZoom.setZoomValue(0.0f);

        App.hud          = null;
        App.levelManager = null;
        endGameManager   = null;
        retryDelay       = null;
        gameControlLoop  = null;

        Trace.finishedMessage();
    }
}
