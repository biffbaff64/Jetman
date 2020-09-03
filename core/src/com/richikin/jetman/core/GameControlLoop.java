package com.richikin.jetman.core;

import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.screens.MainGameScreen;
import com.richikin.jetman.utils.Developer;
import com.richikin.jetman.utils.logging.Trace;

import java.util.concurrent.TimeUnit;

public class GameControlLoop extends AbstractControlLoop
{
    public GameControlLoop(App _app)
    {
        super(_app);
    }

    public void initialise()
    {
    }

    public void update(StateManager gameState)
    {
        switch (gameState.peek())
        {
            //
            // Initialise the current level.
            // If the level is restarting, that will
            // also be handled here.
            case _STATE_SETUP:
            {
                stateSetup();
            }
            break;

            //
            // Display and update the 'Get Ready' message.
            case _STATE_GET_READY:
            {
                stateGetReady();
            }
            break;

            case _STATE_DEVELOPER_PANEL:
            case _STATE_PAUSED:
            case _STATE_GAME:
            {
                stateGame();
            }
            break;

            //
            // Player lost a life.
            // Trying again.
            case _STATE_LEVEL_RETRY:
            {
                stateSetForRetry();
            }
            break;

            //
            // Missile base destroyed, on to the next one
            case _STATE_LEVEL_FINISHED:
            {
                stateSetForLevelFinished();
            }
            break;

            //
            // 'GAME OVER' Message, LJM has lost all lives.
            case _STATE_GAME_OVER:
            {
                stateSetForGameOverMessage();
            }
            break;

            //
            // All Levels finished, Earth is saved, LJM is a Hero!!!
            case _STATE_GAME_FINISHED:
            {
                stateSetForGameFinished();
            }
            break;

            //
            // Update during the 'Missile Launched' message,
            // and also when LJM is teleporting
            case _STATE_ANNOUNCE_MISSILE:
            case _STATE_TELEPORTING:
            {
                app.entityManager.updateSprites();
                app.getHud().update();
            }
            break;

            //
            // Back to MainMenuScreen
            case _STATE_END_GAME:
            {
                stateSetForEndGame();
            }
            break;

            default:
            {
                Trace.__FILE_FUNC("Unsupported gameState: " + gameState.peek());
            }
            break;
        }
    }

    /**
     * Initialise the current level.
     * If the level is restarting, that will
     * also be handled here
     */
    private void stateSetup()
    {
        Trace.megaDivider("_STATE_SETUP");

        // All cameras ON
        app.cameraUtils.enableAllCameras();
        app.baseRenderer.parallaxGameCamera.isLerpingEnabled = false;
        app.baseRenderer.tiledGameCamera.isLerpingEnabled    = false;
        app.baseRenderer.spriteGameCamera.isLerpingEnabled   = false;

        app.levelManager.prepareCurrentLevel(scr().firstTime);

        if (scr().firstTime)
        {
            // This is a good place to start the game tune
            // and initialise a 'GET READY' message if needed.
            // eg:
            /*
            *  Sfx.inst().playGameTune(true);
            *
            *  app.messageManager.enable();
            *  app.messageManager.addZoomMessage
            *      (
            *          GameAssets._GETREADY_MSG_ASSET,
            *          1500
            *      );
            */
        }

        app.appState.set(StateID._STATE_GET_READY);
        app.gameProgress.gameSetupDone = true;
    }

    /**
     * Display and update the 'Get Ready' message
     */
    private void stateGetReady()
    {
        app.getHud().update();

        //
        // If there is no 'Get Ready' message on screen then setup
        // flow control to play the game.
        if (!app.panelManager.doesPanelExist(GameAssets._GETREADY_MSG_ASSET))
        {
            Trace.__FILE_FUNC("----- START GAME (GET READY) -----");

            app.appState.set(StateID._STATE_GAME);
            app.getHud().setStateID(StateID._STATE_PANEL_UPDATE);

            app.getHud().showControls();

            scr().firstTime = false;

            //
            // Re-setup the player after a death/restart
            if (app.getPlayer() != null)
            {
                app.getPlayer().setup(false);
            }
        }
    }

    /**
     * Update the game for states:-
     * _STATE_DEVELOPER_PANEL
     * _STATE_SETTINGS_PANEL
     * _STATE_PAUSED
     * _STATE_GAME
     */
    private void stateGame()
    {
        app.getHud().update();

        if (app.appState.peek() == StateID._STATE_DEVELOPER_PANEL)
        {
            if (!Developer.developerPanelActive)
            {
                app.appState.set(StateID._STATE_GAME);
                app.getHud().setStateID(StateID._STATE_PANEL_UPDATE);
            }
        }
        else
        {
            boolean isLerpingEnabled = (app.appState.peek() == StateID._STATE_GAME);

            app.baseRenderer.tiledGameCamera.isLerpingEnabled    = isLerpingEnabled;
            app.baseRenderer.spriteGameCamera.isLerpingEnabled   = isLerpingEnabled;

            app.mapUtils.update();
            app.entityManager.updateSprites();
            app.entityManager.tidySprites();

            //
            // Check for game ending
            if (!scr().endGameManager.update())
            {
                //
                // Tasks to perform if the game has not ended
                if (app.appState.peek() == StateID._STATE_PAUSED)
                {
                    if (!AppConfig.gamePaused)
                    {
                        app.appState.set(StateID._STATE_GAME);
                    }
                }
            }
        }
    }

    /**
     * Handles the preparation for retrying the current
     * level, after LJM loses a life.
     */
    private void stateSetForRetry()
    {
        app.getHud().update();
        app.mapUtils.update();

        if (scr().retryDelay.time(TimeUnit.MILLISECONDS) > 2000)
        {
            if (AppConfig.quitToMainMenu)
            {
                app.appState.set(StateID._STATE_GAME_OVER);
            }
            else
            {
                app.appState.set(StateID._STATE_SETUP);
            }

            scr().retryDelay = null;
        }
    }

    /**
     * Handles finishing the current level and
     * moving on to the next one.
     * <p>
     * NOTE: "Level finished" for this game is actually "room exit".
     */
    private void stateSetForLevelFinished()
    {
        app.levelManager.closeCurrentLevel();

        app.getHud().update();
        app.mapUtils.update();

        scr().reset();
        app.appState.set(StateID._STATE_SETUP);
    }

    /**
     * Game Over, due to losing all lives.
     * (Waits for the 'Game Over' message to disappear.
     */
    private void stateSetForGameOverMessage()
    {
        app.getHud().update();

        if (!app.panelManager.doesPanelExist(GameAssets._GAMEOVER_MSG_ASSET))
        {
            app.appState.set(StateID._STATE_END_GAME);
        }
    }

    /**
     * Game Over, due to all levels being completed.
     */
    private void stateSetForGameFinished()
    {
        app.getHud().update();

        //
        // If the game has a 'Completed Panel' this is
        // a good place to update it and check for it closing.
        //
        // app.appState should be set to _STATE_END_GAME when appropriate.
        app.appState.set(StateID._STATE_END_GAME);
    }

    /**
     * Game Ended, hand control back to MainMenuScreen.
     */
    private void stateSetForEndGame()
    {
        Trace.megaDivider("***** GAME OVER *****");

        app.gameProgress.closeLastGame();

        scr().dispose();

        app.setScreen(app.mainMenuScreen);

        app.appState.set(StateID._STATE_CLOSING);
    }

    private MainGameScreen scr()
    {
        return app.mainGameScreen;
    }
}
