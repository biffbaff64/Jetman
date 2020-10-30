package com.richikin.jetman.screens;

import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.config.Settings;
import com.richikin.jetman.core.*;
import com.richikin.jetman.graphics.camera.Shake;
import com.richikin.utilslib.input.ControllerType;
import com.richikin.utilslib.developer.Developer;
import com.richikin.utilslib.logging.StopWatch;
import com.richikin.utilslib.logging.Trace;
import com.richikin.enumslib.ScreenID;
import com.richikin.utilslib.states.StateID;

public class MainGameScreen extends AbstractBaseScreen
{
//    public GameCompletedPanel completedPanel;
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

    public MainGameScreen(App _app)
    {
        super(_app);

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
            Trace.__FILE_FUNC("prefs : " + app.settings.prefs);
            Trace.divider();

            endGameManager   = new EndgameManager(app);
            gameControlLoop  = new GameControlLoop(app);
            app.levelManager = new LevelManager(app);

            gameControlLoop.initialise();
            app.levelManager.prepareNewGame();

            app.appState.set(com.richikin.utilslib.states.StateID._STATE_SETUP);
        }

        if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
        {
            app.inputManager.virtualJoystick.show();
        }

        Shake.setAllowed(app.settings.isEnabled(Settings._VIBRATIONS));
    }

    @Override
    public void update()
    {
        switch (app.appState.peek())
        {
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
            case _STATE_END_GAME:
            {
                gameControlLoop.update(app.appState);
            }
            break;

            case _STATE_PREPARE_GAME_END:
            case _STATE_CLOSING:
            {
            }
            break;

            default:
            {
                Trace.__FILE_FUNC();
                Trace.dbg("Unsupported game state: " + app.appState.peek());
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

        update();

        super.render(delta);

        app.worldModel.worldStep();
    }

    public void reset()
    {
        firstTime = true;
        app.gameProgress.playerGameOver = false;
        AppConfig.gamePaused = false;
    }

    @Override
    public void show()
    {
        super.show();

        AppConfig.currentScreenID = ScreenID._GAME_SCREEN;
        app.cameraUtils.disableAllCameras();

        initialise();

        app.appState.set(StateID._STATE_SETUP);
    }

    @Override
    public void hide()
    {
        super.hide();
    }

    @Override
    public void loadImages()
    {
        app.baseRenderer.parallaxBackground.setupLayers(app.mapData.backgroundLayers);
        app.baseRenderer.parallaxForeground.setupLayers(app.mapData.foregroundLayers);
    }

    @Override
    public void dispose()
    {
        super.dispose();

        app.entityManager.dispose();
        app.getHud().dispose();

        app.gameProgress.dispose();

        app.baseRenderer.gameZoom.setZoomValue(0.0f);
        app.baseRenderer.hudZoom.setZoomValue(0.0f);

        app.hud         = null;
        endGameManager  = null;
        retryDelay      = null;
        gameControlLoop = null;
    }
}
