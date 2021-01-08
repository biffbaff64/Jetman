package com.richikin.jetman.core;

import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.developer.Developer;
import com.richikin.utilslib.maths.Item;
import com.richikin.utilslib.maths.NumberUtils;
import com.richikin.utilslib.logging.Trace;

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

    public enum Stack
    {
        _SCORE,
        _TIME,
        _FUEL
    }

    private final Item score;
    private final Item lives;

    private int scoreStack;
    private int timeStack;
    private int fuelStack;

    public GameProgress()
    {
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

        score.setRefillAmount(0);
        lives.setRefillAmount(GameConstants._MAX_LIVES);

        score.setToMinimum();
        lives.setToMaximum();

        baseDestroyed         = false;
        roverDestroyed        = false;
        activeCraterCount     = 0;
        playerLifeOver        = false;
        playerLevel           = 1;
        playerGameOver        = false;
        gameDiffculty         = 1.0f;
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

            App.getHud().getTimeBar().add(amount);
            timeStack -= amount;
        }

        if (fuelStack > 0)
        {
            amount = NumberUtils.getCount(fuelStack);

            App.getHud().getFuelBar().add(amount);
            fuelStack -= amount;
        }
    }

    public Item getScore()
    {
        return score;
    }

    public Item getLives()
    {
        return lives;
    }

    public void closeLastGame()
    {
        App.googleServices.submitScore(score.getTotal(), playerLevel);
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
