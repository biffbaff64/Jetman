package com.richikin.jetman.core;

import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.ui.GameCompletedPanel;
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
            // All lives lost...
            //
            // Setting appState to Level Retry, but setting quitToMainMenu to true
            // will redirect flow to Game Over state after a short delay followed
            // by a 'Game Over' message.
            App.appState.set(StateID._STATE_LEVEL_RETRY);
            App.mainGameScreen.retryDelay = StopWatch.start();

            AppConfig.quitToMainMenu = true;

            returnFlag = true;
        }
        else
        {
            //
            // Rover destroyed, Earth destroyed, the apocalypse is here...
            // The Human Race is no more, you failed...!!!
            if (App.gameProgress.roverDestroyed)
            {
                Trace.__FILE_FUNC_WithDivider("ROVER DESTROYED");
                Trace.divider();

                App.getPlayer().setAction(ActionStates._DEAD);
            }
            //
            // Waheyy!! All levels completed!
            else if (App.gameProgress.gameCompleted)
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
            //
            // LJM Successfully destroyed the missile base!
            else if (App.gameProgress.baseDestroyed)
            {
                if (!App.getHud().messageManager.doesPanelExist("base_destroyed"))
                {
                    App.gameProgress.levelCompleted = true;

                    Trace.__FILE_FUNC_WithDivider("LEVEL COMPLETED");
                    Trace.dbg("app.gameProgress.levelCompleted: " + App.gameProgress.levelCompleted);
                    Trace.divider();

                    App.getHud().setStateID(StateID._STATE_LEVEL_FINISHED);
                    App.appState.set(StateID._STATE_LEVEL_FINISHED);

                    returnFlag = true;
                }
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
            else if (AppConfig.forceQuitToMenu)
            {
                App.appState.set(StateID._STATE_GAME_OVER);
                returnFlag = true;
            }
        }

        return returnFlag;
    }
}
