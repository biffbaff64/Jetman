package com.richikin.jetman.core;

import com.richikin.jetman.ui.GameCompletedPanel;
import com.richikin.utilslib.AppSystem;
import com.richikin.utilslib.logging.StopWatch;
import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.StateID;
import com.richikin.utilslib.logging.Trace;

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
            App.getHud().hideControls();

            //
            // Setting appState to Level Retry, but setting quitToMainMenu to true
            // will redirect flow to Game Over state after a short delay followed
            // by a 'Game Over' message.
            App.appState.set(StateID._STATE_LEVEL_RETRY);
            App.mainGameScreen.retryDelay = StopWatch.start();

            AppSystem.quitToMainMenu = true;

            returnFlag = true;
        }
        else
        {
            if (App.gameProgress.gameCompleted)
            {
                Trace.__FILE_FUNC_WithDivider("GAME COMPLETED");
                Trace.divider();

                App.getHud().hideControls();

                App.mainGameScreen.completedPanel = new GameCompletedPanel();
                App.mainGameScreen.completedPanel.setup();

                App.getHud().setStateID(StateID._STATE_GAME_FINISHED);
                App.appState.set(StateID._STATE_GAME_FINISHED);

                returnFlag = true;
            }
            else if (App.gameProgress.levelCompleted)
            {
                Trace.__FILE_FUNC_WithDivider("LEVEL COMPLETED");
                Trace.divider();

                App.getHud().setStateID(StateID._STATE_LEVEL_FINISHED);
                App.appState.set(StateID._STATE_LEVEL_FINISHED);

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
                    Trace.__FILE_FUNC_WithDivider();
                    Trace.__FILE_FUNC("LIFE LOST - TRY AGAIN");
                    Trace.divider();

                    App.mainGameScreen.retryDelay = StopWatch.start();
                    App.appState.set(StateID._STATE_LEVEL_RETRY);
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
