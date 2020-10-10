package com.richikin.jetman.core;

import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.utils.logging.StopWatch;

public class EndgameManager
{
    private final App app;

    public EndgameManager(App _app)
    {
        this.app = _app;
    }

    public boolean update()
    {
        boolean returnFlag = false;

        if ((app.getPlayer() != null)
            && (app.getPlayer().getAction() == Actions._DEAD))
        {
            // Hide HUD Controls here ??

            //
            // Setting appState to Level Retry, but settin quitToMainMenu to true
            // will redirect flow to Game Over state after a short delay followed
            // by a 'Game Over' message.
            app.appState.set(StateID._STATE_LEVEL_RETRY);
            app.mainGameScreen.retryDelay = StopWatch.start();

            AppConfig.quitToMainMenu = true;

            returnFlag = true;
        }
        else
        {
            if (app.gameProgress.gameCompleted)
            {
                // Hide HUD Controls here ??
                // Initialise a Game Completed Panel here ??

                app.getHud().setStateID(StateID._STATE_GAME_FINISHED);
                app.appState.set(StateID._STATE_GAME_FINISHED);

                returnFlag = true;
            }
            else if (app.gameProgress.levelCompleted)
            {
                app.getHud().setStateID(StateID._STATE_LEVEL_FINISHED);
                app.appState.set(StateID._STATE_LEVEL_FINISHED);

                returnFlag = true;
            }
            //
            // Restarting due to life lost and
            // player is resetting...
            else if (app.gameProgress.isRestarting)
            {
                if ((app.getPlayer() != null)
                    && (app.getPlayer().getAction() == Actions._RESETTING))
                {
                    app.mainGameScreen.retryDelay = StopWatch.start();
                    app.appState.set(StateID._STATE_LEVEL_RETRY);
                }

                returnFlag = true;
            }
            //
            // Forcing quit to main menu screen.
            // For example, from pause menu...
            else if (AppConfig.forceQuitToMenu)
            {
                app.appState.set(StateID._STATE_END_GAME);
                returnFlag = true;
            }
        }

        return returnFlag;
    }
}
