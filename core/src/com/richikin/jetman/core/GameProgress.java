package com.richikin.jetman.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.developer.Developer;
import com.richikin.jetman.ui.ProgressBar;
import com.richikin.utilslib.maths.Item;
import com.richikin.utilslib.maths.NumberUtils;
import com.richikin.utilslib.logging.Trace;

public class GameProgress implements Disposable
{
    public static final int _MAX_TIMEBAR_LENGTH = 1000;
    public static final int _MAX_FUELBAR_LENGTH = 1000;

    public boolean isRestarting;
    public boolean levelCompleted;
    public boolean gameCompleted;
    public boolean gameSetupDone;

    public boolean baseDestroyed;
    public boolean roverDestroyed;
    public int     activeCraterCount;
    public boolean playerLifeOver;
    public int     playerLevel;
    public boolean playerGameOver;
    public float   gameDiffculty;

    public enum Stack
    {
        _SCORE,
        _TIME,
        _FUEL
    }

    private final Item score;
    private final Item lives;

    private ProgressBar timeBar;
    private ProgressBar fuelBar;

    private int scoreStack;
    private int timeStack;
    private int fuelStack;

    public GameProgress()
    {
        score = new Item(0, GameConstants._MAX_SCORE, 0);
        lives = new Item(0, GameConstants._MAX_LIVES, GameConstants._MAX_LIVES);

        this.scoreStack = 0;
        this.timeStack  = 0;
        this.fuelStack  = 0;
    }

    public void resetProgress()
    {
        Trace.__FILE_FUNC();

        isRestarting   = false;
        levelCompleted = false;
        gameCompleted  = false;
        gameSetupDone  = false;
        gameDiffculty  = 1.0f;

        score.setRefillAmount(0);
        lives.setRefillAmount(GameConstants._MAX_LIVES);

        score.setToMinimum();
        lives.setToMaximum();

        timeBar = new ProgressBar(1, 0, _MAX_TIMEBAR_LENGTH, "bar9");
        fuelBar = new ProgressBar(1, 0, _MAX_FUELBAR_LENGTH, "bar9");

        baseDestroyed     = false;
        roverDestroyed    = false;
        activeCraterCount = 0;
        playerLifeOver    = false;
        playerLevel       = 1;
        playerGameOver    = false;
        gameDiffculty     = 1.0f;
    }

    public void update()
    {
        switch (App.appState.peek())
        {
            case _STATE_PAUSED:
            case _STATE_GAME:
            case _STATE_MESSAGE_PANEL:
            {
                updateStacks();
            }
            break;

            default:
                break;
        }
    }

    public void stackPush(Stack stack, int amount)
    {
        switch (stack)
        {
            case _SCORE:
            {
                scoreStack += amount;
            }
            break;

            case _TIME:
            {
                timeStack += amount;
            }
            break;

            case _FUEL:
            {
                fuelStack += amount;
            }
            break;

            default:
                break;
        }
    }

    /**
     * Update the fuel bar and Launch timer bar.
     * The fuel bar decrements if LJM is flying, or jumping a crater.
     * The Launch Timer bar slowly decrements down to zero. Once
     * the bar is empty the missiles are launched.
     */
    public void updateBars()
    {
        if (!App.teleportManager.teleportActive)
        {
            if (App.getPlayer() != null)
            {
                if ((App.getPlayer().getAction() == ActionStates._FLYING) || App.getPlayer().isJumpingCrater)
                {
                    fuelBar.setSpeed(App.getPlayer().isCarrying ? 2 : 1);
                    fuelBar.updateSlowDecrement();
                }
            }

            if (App.getBase().getAction() != ActionStates._WAITING)
            {
                timeBar.updateSlowDecrement();
            }

            if (timeBar.justEmptied)
            {
                if (App.getBase() != null)
                {
                    if (!App.missileBaseManager.isMissileActive)
                    {
                        App.getBase().setAction(ActionStates._SET_FIGHTING);
                    }
                }

                timeBar.refill();
            }
        }
    }

    /**
     * Updates the Fuel bar and Time bar colours, depending
     * on the length of the bars.
     */
    public void updateBarColours()
    {
        App.getHud().fuelLowWarning = false;
        App.getHud().timeLowWarning = false;

        if (fuelBar.getTotal() < (float) (_MAX_TIMEBAR_LENGTH / 2))
        {
            if (fuelBar.getTotal() < (float) (_MAX_TIMEBAR_LENGTH / 4))
            {
                fuelBar.setColor(Color.RED);
                App.getHud().fuelLowWarning = true;
            }
            else
            {
                fuelBar.setColor(Color.ORANGE);
            }
        }
        else
        {
            fuelBar.setColor(Color.GREEN);
        }

        if (timeBar.getTotal() < (float) (_MAX_TIMEBAR_LENGTH / 2))
        {
            if (timeBar.getTotal() < (float) (_MAX_TIMEBAR_LENGTH / 4))
            {
                timeBar.setColor(Color.RED);
                App.getHud().timeLowWarning = true;
            }
            else
            {
                timeBar.setColor(Color.ORANGE);
            }
        }
        else
        {
            timeBar.setColor(Color.YELLOW);
        }
    }

    private void updateStacks()
    {
        int amount;

        if (scoreStack > 0)
        {
            amount = NumberUtils.getCount(scoreStack);

            score.add(amount);
            scoreStack -= amount;
        }

        if (timeStack > 0)
        {
            amount = NumberUtils.getCount(timeStack);

            timeBar.add(amount);
            timeStack -= amount;
        }

        if (fuelStack > 0)
        {
            amount = NumberUtils.getCount(fuelStack);

            fuelBar.add(amount);
            fuelStack -= amount;
        }
    }

    public ProgressBar getTimeBar()
    {
        return timeBar;
    }

    public ProgressBar getFuelBar()
    {
        return fuelBar;
    }

    public Item getScore()
    {
        return score;
    }

    public Item getLives()
    {
        return lives;
    }

    public void toMinimum()
    {
        score.setToMinimum();
        lives.setToMinimum();

        playerLevel = 1;
    }

    public float getGameDifficulty()
    {
        return gameDiffculty;
    }

    public void closeLastGame()
    {
        App.googleServices.submitScore(score.getTotal(), playerLevel);
    }

    @Override
    public void dispose()
    {
        Trace.__FILE_FUNC();

        // DO NOT dispose of score and lives unless they have
        // been processed with regard to hiscores first.
    }
}
