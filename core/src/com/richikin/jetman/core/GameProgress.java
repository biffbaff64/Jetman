package com.richikin.jetman.core;

import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.maths.Item;
import com.richikin.jetman.utils.NumberUtils;
import com.richikin.jetman.utils.logging.Trace;

public class GameProgress implements Disposable
{
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
    public boolean newHighScoreAvailable;

    public Item score;
    public Item lives;

    public enum Stack
    {
        _SCORE,
        _TIME,
        _FUEL
    }

    private int scoreStack;
    private int timeStack;
    private int fuelStack;

    private final App app;

    public GameProgress(App _app)
    {
        this.app = _app;

        score = new Item(0, GameConstants._MAX_SCORE, 0);
        lives = new Item(0, GameConstants._MAX_LIVES, GameConstants._MAX_LIVES);

        this.scoreStack = 0;
        this.timeStack = 0;
        this.fuelStack = 0;
    }

    public void resetProgress()
    {
        Trace.__FILE_FUNC();

        isRestarting   = false;
        levelCompleted = false;
        gameCompleted  = false;
        gameSetupDone  = false;
        gameDiffculty  = 1.0f;

        resetData();
    }

    public void update()
    {
        switch (app.appState.peek())
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

            app.getHud().getTimeBar().add(amount);
            timeStack -= amount;
        }

        if (fuelStack > 0)
        {
            amount = NumberUtils.getCount(fuelStack);

            app.getHud().getFuelBar().add(amount);
            fuelStack -= amount;
        }
    }

    /**
     * Create player data for the Heads Up Display,
     * data for anything that is displayed on the HUD.
     * (Scores, Lives, Level etc.)
     */
    private void resetData()
    {
        score.setRefillAmount(0);
        lives.setRefillAmount(GameConstants._MAX_LIVES);

        score.setToMinimum();
        lives.setToMaximum();

        newHighScoreAvailable = false;
        baseDestroyed         = false;
        roverDestroyed        = false;
        activeCraterCount     = 0;
        playerLifeOver        = false;
        playerLevel           = 1;
        playerGameOver        = false;
        gameDiffculty         = 1.0f;
    }

    public void closeLastGame()
    {
        app.googleServices.submitScore(score.getTotal(), playerLevel);
    }

    public void toMinimum()
    {
        score.setToMinimum();
        lives.setToMinimum();

        playerLevel = 1;
    }

    @Override
    public void dispose()
    {
    }

    public float getGameDifficulty()
    {
        return gameDiffculty;
    }
}
