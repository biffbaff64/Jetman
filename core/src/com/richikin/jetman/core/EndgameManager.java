package com.richikin.jetman.core;

import com.richikin.utilslib.config.AppSystem;
import com.richikin.utilslib.logging.StopWatch;
import com.richikin.enumslib.ActionStates;
import com.richikin.utilslib.states.StateID;

public class EndgameManager
{
    public EndgameManager()
    {
    }

    public boolean update()
    {
        boolean returnFlag = false;

        if ((App.getPlayer() != null)
            && (App.getPlayer().getAction() == ActionStates._DEAD))
        {
            // Hide HUD Controls here ??

            //
            // Setting appState to Level Retry, but settin quitToMainMenu to true
            // will redirect flow to Game Over state after a short delay followed
            // by a 'Game Over' message.
            App.appState.set(com.richikin.utilslib.states.StateID._STATE_LEVEL_RETRY);
            App.mainGameScreen.retryDelay = StopWatch.start();

            AppSystem.quitToMainMenu = true;

            returnFlag = true;
        }
        else
        {
            if (App.gameProgress.gameCompleted)
            {
                // Hide HUD Controls here ??
                // Initialise a Game Completed Panel here ??

                App.getHud().setStateID(com.richikin.utilslib.states.StateID._STATE_GAME_FINISHED);
                App.appState.set(com.richikin.utilslib.states.StateID._STATE_GAME_FINISHED);

                returnFlag = true;
            }
            else if (App.gameProgress.levelCompleted)
            {
                App.getHud().setStateID(com.richikin.utilslib.states.StateID._STATE_LEVEL_FINISHED);
                App.appState.set(com.richikin.utilslib.states.StateID._STATE_LEVEL_FINISHED);

                returnFlag = true;
            }
            //
            // Restarting due to life lost and
            // player is resetting...
            else if (App.gameProgress.isRestarting)
            {
                if ((App.getPlayer() != null)
                    && (App.getPlayer().getAction() == ActionStates._RESETTING))
                {
                    App.mainGameScreen.retryDelay = StopWatch.start();
                    App.appState.set(com.richikin.utilslib.states.StateID._STATE_LEVEL_RETRY);
                }

                returnFlag = true;
            }
            //
            // Forcing quit to main menu screen.
            // For example, from pause menu...
            else if (AppSystem.forceQuitToMenu)
            {
                App.appState.set(StateID._STATE_END_GAME);
                returnFlag = true;
            }
        }

        return returnFlag;
    }
}
