package com.richikin.jetman.core;

import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.maths.Item;
import com.richikin.jetman.utils.logging.Trace;

public class GameProgress implements Disposable
{
    public boolean isRestarting;
    public boolean levelCompleted;
    public boolean gameCompleted;
    public boolean gameSetupDone;

    public boolean newHighScoreAvailable;
    public boolean baseDestroyed;
    public boolean roverDestroyed;
    public int     activeCraterCount;
    public boolean playerLifeOver;
    public int     playerLevel;
    public boolean playerGameOver;
    public float   gameDiffculty;

    public Item score;
    public Item lives;

    private final App app;

    public GameProgress(App _app)
    {
        this.app = _app;

        score = new Item(0, GameConstants._MAX_SCORE, 0);
        lives = new Item(0, GameConstants._MAX_LIVES, GameConstants._MAX_LIVES);
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

        newHighScoreAvailable   = false;
        baseDestroyed           = false;
        roverDestroyed          = false;
        activeCraterCount       = 0;
        playerLifeOver          = false;
        playerLevel             = 1;
        playerGameOver          = false;
        gameDiffculty           = 1.0f;
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
