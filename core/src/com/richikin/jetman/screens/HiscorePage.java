
package com.richikin.jetman.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.enumslib.StateID;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.core.App;
import com.richikin.utilslib.ui.Scene2DUtils;
import com.richikin.utilslib.core.HighScore;
import com.richikin.utilslib.logging.StateManager;
import com.richikin.utilslib.logging.StopWatch;
import com.richikin.utilslib.logging.Trace;
import com.richikin.jetman.ui.IUIPage;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class HiscorePage implements IUIPage, Disposable
{
    private static final int _RANK_X             = 360;
    private static final int _LEVEL_X            = 580;
    private static final int _SCORE_X            = 760;
    private static final int _TABLE_Y            = 420;
    private static final int _SPACING            = 64;
    private static final int _FNT_SIZE           = 48;
    private static final int _DISPLAYED_HISCORES = 5;

    private final Color[] colours =
        {
            Color.CYAN,
            Color.SKY,
            Color.SKY,
            Color.SLATE,
            Color.SLATE,
            Color.ROYAL,
            Color.ROYAL,
            Color.BLUE,
            Color.BLUE,
            Color.NAVY,
            Color.NAVY,
            Color.BLUE,
            Color.BLUE,
            Color.ROYAL,
            Color.ROYAL,
            Color.SLATE,
            Color.SLATE,
            Color.SKY,
            Color.SKY,
            Color.CYAN,
        };

    private Label[] levelLabels;
    private Label[] rankLabels;
    private Label[] scoreLabels;

    private StopWatch    stopWatch;
    private StateManager state;
    private Texture      foreground;

    private int[] colorIndex;

    public HiscorePage()
    {
    }

    @Override
    public void initialise()
    {
        addItems();

        state     = new StateManager();
        stopWatch = StopWatch.start();

        showItems(false);
    }

    @Override
    public boolean update()
    {
        if (state.peek() != StateID._STATE_NEW_HISCORE)
        {
            if (stopWatch.time(TimeUnit.MILLISECONDS) >= 75)
            {
                for (int i = 0; i < _DISPLAYED_HISCORES; i++)
                {
                    rankLabels[i].setStyle(new Label.LabelStyle(rankLabels[i].getStyle().font, colours[colorIndex[i]]));
                    levelLabels[i].setStyle(new Label.LabelStyle(levelLabels[i].getStyle().font, colours[colorIndex[i]]));
                    scoreLabels[i].setStyle(new Label.LabelStyle(scoreLabels[i].getStyle().font, colours[colorIndex[i]]));

                    if (--colorIndex[i] < 0)
                    {
                        colorIndex[i] = (colours.length - 1);
                    }
                }

                stopWatch.reset();
            }
        }

        return false;
    }

    @Override
    public void show()
    {
        showItems(true);

        if (App.highScoreUtils.newHighScoreAvailable)
        {
            state.set(StateID._STATE_NEW_HISCORE);
        }
        else
        {
            state.set(StateID._STATE_PANEL_UPDATE);
        }

        stopWatch.reset();
    }

    @Override
    public void hide()
    {
        showItems(false);

        App.highScoreUtils.newHighScoreAvailable = false;
    }

    @Override
    public void draw(final SpriteBatch spriteBatch)
    {
        if (foreground != null)
        {
            spriteBatch.draw(foreground, AppConfig.hudOriginX, AppConfig.hudOriginY);
        }
    }

    /**
     * Adds the Menu items to the stage
     */
    private void addItems()
    {
        foreground = App.assets.loadSingleAsset("data/hiscore_foreground.png", Texture.class);

        rankLabels  = new Label[_DISPLAYED_HISCORES];
        levelLabels = new Label[_DISPLAYED_HISCORES];
        scoreLabels = new Label[_DISPLAYED_HISCORES];

        App.highScoreUtils.loadTableData();

        HighScore highScore = new HighScore();

        if (App.highScoreUtils.newHighScoreAvailable)
        {
            highScore.score = App.gameProgress.getScore().getTotal();
            highScore.level = App.gameProgress.playerLevel;
            highScore.rank  = App.highScoreUtils.findInsertLevel(highScore);

            App.highScoreUtils.addHighScore(highScore);
        }

        colorIndex = new int[_DISPLAYED_HISCORES];

        Scene2DUtils scene2DUtils = new Scene2DUtils();

        for (int i = 0; i < _DISPLAYED_HISCORES; i++)
        {
            int _Y_POS = (_TABLE_Y - (_SPACING * i));

            // Hiscore table rank
            rankLabels[i] = scene2DUtils.addLabel
                (
                    "" + (i + 1),
                    (int) AppConfig.hudOriginX + _RANK_X,
                    (int) AppConfig.hudOriginY + _Y_POS,
                    _FNT_SIZE,
                    Color.WHITE,
                    GameAssets._BENZOIC_FONT
                );

            // The game level achieved
            levelLabels[i] = scene2DUtils.addLabel
                (
                    "" + App.highScoreUtils.getHighScoreTable()[i].level,
                    (int) AppConfig.hudOriginX + _LEVEL_X,
                    (int) AppConfig.hudOriginY + _Y_POS,
                    _FNT_SIZE,
                    Color.WHITE,
                    GameAssets._BENZOIC_FONT
                );

            // The player score
            scoreLabels[i] = scene2DUtils.addLabel
                (
                    String.format(Locale.UK, "%8d", App.highScoreUtils.getHighScoreTable()[i].score),
                    (int) AppConfig.hudOriginX + _SCORE_X,
                    (int) AppConfig.hudOriginY + _Y_POS,
                    _FNT_SIZE,
                    Color.WHITE,
                    GameAssets._BENZOIC_FONT
                );

            colorIndex[i] = i;

            App.stage.addActor(rankLabels[i]);
            App.stage.addActor(levelLabels[i]);
            App.stage.addActor(scoreLabels[i]);
        }
    }

    private void showItems(boolean _visible)
    {
        if (rankLabels != null)
        {
            for (Label label : rankLabels)
            {
                label.setVisible(_visible);
            }
        }

        if (levelLabels != null)
        {
            for (Label label : levelLabels)
            {
                label.setVisible(_visible);
            }
        }

        if (scoreLabels != null)
        {
            for (Label label : scoreLabels)
            {
                label.setVisible(_visible);
            }
        }

        AppConfig.backButton.setVisible(_visible);
        AppConfig.backButton.setDisabled(!_visible);
        AppConfig.backButton.setChecked(false);
    }

    @Override
    public void dispose()
    {
        Trace.__FILE_FUNC();

        if (rankLabels != null)
        {
            for (Label label : rankLabels)
            {
                label.addAction(Actions.removeActor());
            }
        }

        if (levelLabels != null)
        {
            for (Label label : levelLabels)
            {
                label.addAction(Actions.removeActor());
            }
        }

        if (scoreLabels != null)
        {
            for (Label label : scoreLabels)
            {
                label.addAction(Actions.removeActor());
            }
        }

        colorIndex  = null;
        rankLabels  = null;
        levelLabels = null;
        scoreLabels = null;
        state       = null;
    }
}
